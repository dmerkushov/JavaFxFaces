/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.icontabbed;

import com.sun.javafx.collections.TrackableObservableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import ru.dmerkushov.javafx.faces.FacesDock;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import static ru.dmerkushov.javafx.faces.threads.FxThreadChecker.checkOnAppThread;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class IconTabbedDock extends FacesDock {

	UUID uuid;
	ListProperty<FacesPanel> panelsList;
	FacesPanel activePanel;
	String displayName;
	String toolTip;
	IconTabbedDockView myView;
	int iconWidth;
	int iconHeight;

	public IconTabbedDock (UUID uuid, String displayName, String toolTip, int iconWidth, int iconHeight, FacesPanel... panels) {
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

		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;

		myView = new IconTabbedDockView ();

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
			facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + IconTabbedDock.this);
			return;
		}

		panelsList.remove (panelIndex);
	}

	@Override
	public boolean containsPanel (FacesPanel panel) {
		return panelsList.contains (panel);
	}

	public class IconTabbedDockView extends FacesPanelView {

		Map<FacesPanel, ImageView> icons = new HashMap<> ();

		public IconTabbedDockView () {
			super (IconTabbedDock.this);
		}

		void panelListChanged (ListChangeListener.Change<FacesPanel> c) {
			checkOnAppThread ();

			final Color iconBg = Color.WHITESMOKE;	//TODO Implement usage of the currently used stylesheet background color, or at least preferences usage

			while (c.next ()) {
				c.getAddedSubList ().stream ().filter ((p) -> (p != null)).forEachOrdered (new Consumer<FacesPanel> () {
					@Override
					public void accept (FacesPanel panel) {
						getChildren ().add (panel.getView ());
						panel.getView ().setVisible (false);
						ImageView iconView = new ImageView (FacesPanel.getIconOnBackgroundColor (panel.getIcon (iconWidth, iconHeight), iconBg));

						icons.put (panel, iconView);
						iconView.setOnMouseClicked ((e) -> showPanel (panel));

						Tooltip tooltip = new Tooltip (panel.getDisplayName () + "\n\n" + panel.getToolTip ());
						Tooltip.install (iconView, tooltip);

						getChildren ().add (iconView);

						facesLoggerWrapper.finest ("Added panel " + panel + ", iconView " + iconView);
					}
				});
				c.getRemoved ().stream ().filter ((p) -> (p != null)).forEachOrdered ((panel) -> {
					if (panel.equals (activePanel)) {
						facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + IconTabbedDock.this);
						return;
					}

					getChildren ().remove (panel.getView ());
					getChildren ().remove (icons.get (panel));

					System.err.println ("Removed panel " + panel + ", iconView " + icons.get (panel));

					icons.remove (panel);
				});
			}
			refreshView ();
		}

		private void refreshView () {
			checkOnAppThread ();

			int iconIndex = 0;
			for (FacesPanel panel : panelsList) {
				if (panel == null) {
					continue;
				}
				ImageView iconView = icons.get (panel);

				iconView.layoutXProperty ().set (5.0);
				iconView.layoutYProperty ().set (iconIndex * (iconHeight + 5.0));

				FacesPanelView panelView = panel.getView ();
				panelView.layoutXProperty ().set (iconWidth + 10.0);
				panelView.layoutYProperty ().set (0);
				panelView.minWidthProperty ().bind (this.widthProperty ().subtract (iconWidth + 10.0));
				panelView.maxWidthProperty ().bind (this.widthProperty ().subtract (iconWidth + 10.0));
				panelView.minHeightProperty ().bind (this.heightProperty ());
				panelView.maxHeightProperty ().bind (this.heightProperty ());

				iconIndex++;
			}
		}

		private void showPanel (FacesPanel toShow) {
			checkOnAppThread ();

			Objects.requireNonNull (toShow, "toShow");
			if (!panelsList.contains (toShow)) {
				throw new IllegalArgumentException ("Panel " + toShow + " is not on the panel list");
			}

			if (activePanel != null && !toShow.equals (activePanel)) {
				activePanel.getView ().setVisible (false);
			}
			toShow.getView ().setVisible (true);
			activePanel = toShow;
		}

	}

}
