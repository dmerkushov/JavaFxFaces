/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.tabbed;

import com.sun.javafx.collections.TrackableObservableList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import ru.dmerkushov.javafx.faces.FacesDock;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;
import ru.dmerkushov.javafx.faces.FacesUtil;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import static ru.dmerkushov.javafx.faces.threads.FxThreadChecker.checkOnAppThread;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class TabbedDock extends FacesDock {

	UUID uuid;
	ListProperty<FacesPanel> panelsList;
	FacesPanel activePanel;
	String displayName;
	String toolTip;
	TabbedDockView myView;

	public TabbedDock (UUID uuid, String displayName, String toolTip, FacesPanel... panels) {
		super ();

		Objects.requireNonNull (panels, "panels");
		for (int i = 0; i < panels.length; i++) {
			if (panels[i] == null) {
				throw new NullPointerException ("panels[" + i + "]");
			}
		}

		this.uuid = (uuid != null ? uuid : UUID.randomUUID ());
		this.displayName = (displayName != null ? displayName : "");
		this.toolTip = (toolTip != null ? toolTip : "");

		myView = new TabbedDockView (this);

		this.panelsList = new SimpleListProperty<> (new TrackableObservableList<FacesPanel> () {
			@Override
			protected void onChanged (ListChangeListener.Change<FacesPanel> c) {
				myView.panelListChanged (c);
			}
		});
		panelsList.sizeProperty ().addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			if (oldValue.equals (0) && !newValue.equals (0)) {
				setActivePanel (panelsList.get (0));
			}
		});

		panelsList.addAll (panels);
	}

	@Override
	public UUID getPanelInstanceUuid () {
		return uuid;
	}

	@Override
	public String getDisplayName () {
		return displayName;
	}

	@Override
	public String getToolTip () {
		return toolTip;
	}

	@Override
	public FacesPanelView getView () {
		return myView;
	}

	public FacesPanel getActivePanel () {
		return activePanel;
	}

	public void setActivePanel (FacesPanel panel) {
		myView.showPanel (panel);
	}

	@Override
	public List<FacesPanel> getPanels () {
		return panelsList.get ();
	}

	@Override
	protected Image prepareIcon (int width, int height) {
		return new WritableImage (width, height);
	}

	@Override
	public void addPanel (int panelIndex, FacesPanel panel) {
		panelsList.add (panelIndex, panel);
	}

	@Override
	public void removePanel (int panelIndex) {
		if (panelsList.get (panelIndex).equals (activePanel)) {
			facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + TabbedDock.this);
			return;
		}

		panelsList.remove (panelIndex);
	}

	@Override
	public boolean containsPanel (FacesPanel panel) {
		return panelsList.contains (panel);
	}

	public class TabbedDockView extends FacesPanelView {

		final TabPane tabPane;

		public TabbedDockView (FacesPanel panel) {
			super (panel);

			tabPane = new TabPane ();

			getChildren ().add (tabPane);

			FacesUtil.bindWidthHeight (tabPane, this.widthProperty (), this.heightProperty ());
		}

		void panelListChanged (ListChangeListener.Change<FacesPanel> c) {
			checkOnAppThread ();

			ObservableList<Tab> tabs = tabPane.getTabs ();

			while (c.next ()) {
				c.getAddedSubList ().stream ().filter ((p) -> (p != null)).forEachOrdered (new Consumer<FacesPanel> () {
					@Override
					public void accept (FacesPanel panel) {
						Tab tab = new Tab ();
						tab.setContent (panel.getView ());
						tab.setText (panel.getDisplayName ());
						tab.setTooltip (new Tooltip (panel.getToolTip ()));
						tabs.add (tab);

						facesLoggerWrapper.finest ("Added panel " + panel);
					}
				});
				c.getRemoved ().stream ().filter ((p) -> (p != null)).forEachOrdered ((panel) -> {
					if (panel.equals (activePanel)) {
						facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + TabbedDock.this);
						return;
					}

					Tab foundTab = null;
					for (Tab tab : tabs) {
						if (tab.getContent () == panel.getView ()) {
							foundTab = tab;
							break;
						}
					}
					if (foundTab != null) {
						tabs.remove (foundTab);
					}
				});
			}
		}

		void showPanel (FacesPanel panel) {

			ObservableList<Tab> tabs = tabPane.getTabs ();

			for (Tab tab : tabs) {
				FacesPanelView tabView;
				try {
					tabView = (FacesPanelView) tab.getContent ();
				} catch (ClassCastException ex) {
					facesLoggerWrapper.warning ("Non-faces panel view in a tabbed dock: " + tab.getContent () + " on tab " + tab.getText (), ex);
					continue;
				}
				if (panel == tabView.getPanel ()) {
					tabPane.selectionModelProperty ().get ().select (tab);
					break;
				}
			}

		}

	}

}
