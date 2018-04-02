/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.wizard;

import com.sun.javafx.collections.TrackableObservableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ru.dmerkushov.javafx.faces.FacesDock;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import static ru.dmerkushov.javafx.faces.threads.FxThreadChecker.checkOnAppThread;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class WizardDock extends FacesDock {

	UUID uuid;
	ListProperty<FacesPanel> panelsList;
	FacesPanel activePanel;
	String displayName;
	String toolTip;
	WizardDockView myView;
	int iconWidth;
	int iconHeight;
	boolean showIcons;
	boolean showText;

	/**
	 *
	 * @param uuid
	 * @param displayName
	 * @param toolTip
	 * @param iconWidth
	 * @param iconHeight
	 * @param showIcons Show icons on the buttons or not?
	 * @param showText Show text on the buttons or not? The value will be
	 * ignored if <code>showIcons</code> is <code>false</code>
	 * @param panels
	 */
	public WizardDock (UUID uuid, String displayName, String toolTip, int iconWidth, int iconHeight, boolean showIcons, boolean showText, FacesPanel... panels) {
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
		this.showIcons = showIcons;
		this.showText = showText;

		myView = new WizardDockView ();

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
			facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + WizardDock.this);
			return;
		}

		panelsList.remove (panelIndex);
	}

	@Override
	public boolean containsPanel (FacesPanel panel) {
		return panelsList.contains (panel);
	}

	public class WizardDockView extends FacesPanelView {

		Map<FacesPanel, HBox> iconHBoxes = new HashMap<> ();
		HBox iconsHBox = new HBox ();
		Pane panelsPane = new Pane ();
		Label panelNameLabel = new Label ();

		public WizardDockView () {
			super (WizardDock.this);

			VBox vb = new VBox ();
			vb.getChildren ().add (iconsHBox);
			iconsHBox.setMinHeight (iconHeight + 10.0);

			Separator sep = new Separator (Orientation.HORIZONTAL);
			vb.getChildren ().add (sep);
			sep.minHeightProperty ().set (10.0);

			panelsPane.minWidthProperty ().bind (vb.widthProperty ());
			panelsPane.maxWidthProperty ().bind (vb.widthProperty ());
			panelsPane.minHeightProperty ().bind (vb.heightProperty ().subtract (iconsHBox.heightProperty ()).subtract (sep.heightProperty ()));
			panelsPane.maxHeightProperty ().bind (vb.heightProperty ().subtract (iconsHBox.heightProperty ()).subtract (sep.heightProperty ()));

			vb.getChildren ().add (panelsPane);

			vb.minWidthProperty ().bind (this.widthProperty ());
			vb.maxWidthProperty ().bind (this.widthProperty ());
			vb.minHeightProperty ().bind (this.heightProperty ());
			vb.maxHeightProperty ().bind (this.heightProperty ());

			getChildren ().add (vb);

		}

		void panelListChanged (ListChangeListener.Change<FacesPanel> c) {
			checkOnAppThread ();

			while (c.next ()) {
				c.getAddedSubList ().stream ().filter ((p) -> (p != null)).forEachOrdered ((FacesPanel panel) -> {
					FacesPanelView panelView = panel.getView ();

					panelsPane.getChildren ().add (panelView);
					panelView.minWidthProperty ().bind (panelsPane.widthProperty ());
					panelView.maxWidthProperty ().bind (panelsPane.widthProperty ());
					panelView.minHeightProperty ().bind (panelsPane.heightProperty ());
					panelView.maxHeightProperty ().bind (panelsPane.heightProperty ());
					panelView.setVisible (false);

					Button btn;
					if (showIcons) {
						ImageView iconView = new ImageView (panel.getIcon (iconWidth, iconHeight));
						if (showText) {
							btn = new Button (splitToTwo (panel.getDisplayName ()), iconView);
						} else {
							btn = new Button (null, iconView);
							btn.setMinSize (iconWidth + 10.0, iconHeight + 10.0);
						}
					} else {
						btn = new Button (splitToTwo (panel.getDisplayName ()));
						btn.setTextAlignment (TextAlignment.CENTER);
					}

					btn.getStylesheets ().add ("wizard-button");
					btn.setOnAction ((ActionEvent e) -> showPanel (panel));
					Tooltip tooltip = new Tooltip (panel.getDisplayName () + "\n\n" + panel.getToolTip ());
					Tooltip.install (btn, tooltip);
					btn.minHeightProperty ().bind (iconsHBox.heightProperty ());

					HBox iconHB = new HBox ();
					if (!iconsHBox.getChildren ().isEmpty ()) {
						Label arrow = new Label (" -> ");
						arrow.minHeightProperty ().bind (iconsHBox.heightProperty ());
						iconHB.getChildren ().add (arrow);
					}
					iconHB.getChildren ().add (btn);
					iconHB.getStyleClass ().add ("wizard-buttonHbox");

					iconHBoxes.put (panel, iconHB);
					iconsHBox.getChildren ().add (iconHB);

//					facesLoggerWrapper.finest ("Added panel " + panel + ", iconNode " + iconNode);
				});
				c.getRemoved ().stream ().filter ((p) -> (p != null)).forEachOrdered ((panel) -> {
					if (panel.equals (activePanel)) {
						facesLoggerWrapper.warning ("Attempt to remove the active panel: " + activePanel + " on dock " + WizardDock.this);
						return;
					}

					panelsPane.getChildren ().remove (panel.getView ());
					iconsHBox.getChildren ().remove (iconHBoxes.get (panel));

//					System.err.println ("Removed panel " + panel + ", iconNode " + iconHBoxes.get (panel));
					iconHBoxes.remove (panel);
				});
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
				iconHBoxes.get (activePanel).getStyleClass ().remove ("wizard-buttonHbox-selected");
			}
			toShow.getView ().setVisible (true);
			activePanel = toShow;
			iconHBoxes.get (activePanel).getStyleClass ().add ("wizard-buttonHbox-selected");
		}

	}

	private static String splitToTwo (String in) {
		if (in == null) {
			return null;
		}
		if (!in.contains (" ")) {
			return in;
		}

		int inlen = in.length ();
		int halfInlen = inlen / 2;

		int splitAt = halfInlen;
		for (int i = 0; i < halfInlen; i++) {
			splitAt = halfInlen + i;
			char ch = in.charAt (splitAt);
			if (ch == ' ') {
				break;
			}
			splitAt = halfInlen - i;
			ch = in.charAt (splitAt);
			if (ch == ' ') {
				break;
			}
		}

		StringBuilder sb = new StringBuilder ();
		sb.append (in.substring (0, splitAt).trim ());
		sb.append ("\n");
		sb.append (in.substring (splitAt).trim ());
		return sb.toString ();
	}

}
