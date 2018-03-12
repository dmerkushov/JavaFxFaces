/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.table;

import com.sun.javafx.collections.TrackableObservableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.dmerkushov.javafx.faces.FacesDock;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import static ru.dmerkushov.javafx.faces.threads.FxThreadChecker.checkOnAppThread;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class TableDock extends FacesDock {

	UUID uuid;
	String displayName;
	String toolTip;
	TableDockView myView;
	DoubleProperty eachPaneWidth = new SimpleDoubleProperty ();
	DoubleProperty eachPaneHeight = new SimpleDoubleProperty ();
	ListProperty<FacesPanel> panelsList;
	int tableWidth;

	public TableDock (UUID uuid, String displayName, String toolTip, int tableWidth, FacesPanel... panels) {
		super ();

		if (tableWidth < 1) {
			throw new IllegalArgumentException ("tableWidth < 1: " + tableWidth);
		}

		Objects.requireNonNull (panels, "panels");
		for (int i = 0; i < panels.length; i++) {
			if (panels[i] == null) {
				throw new NullPointerException ("panels[" + i + "]");
			}
		}

		this.uuid = (uuid != null ? uuid : UUID.randomUUID ());
		this.displayName = (displayName != null ? displayName : "");
		this.toolTip = (toolTip != null ? toolTip : "");
		this.tableWidth = tableWidth;
		myView = new TableDockView ();

		this.panelsList = new SimpleListProperty<> (new TrackableObservableList<FacesPanel> () {
			@Override
			protected void onChanged (ListChangeListener.Change<FacesPanel> c) {
				myView.panelListChanged (c);
			}
		});

		eachPaneWidth.bind (myView.widthProperty ().divide (tableWidth));
		eachPaneHeight.bind (myView.heightProperty ().divide (panelsList.sizeProperty ().add (tableWidth - 1).divide (tableWidth)));

		this.panelsList.addAll (panels);
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

	@Override
	public List<FacesPanel> getPanels () {
		return new ArrayList (panelsList);
	}

	@Override
	protected Image prepareIcon (int width, int height) {
		Canvas canvas = new Canvas (width, height);

		GraphicsContext gc = canvas.getGraphicsContext2D ();
		gc.setFill (Color.GREEN);
		gc.fillRect (0, 0, width, height);

		return canvas.snapshot (null, null);
	}

	@Override
	public void addPanel (int panelIndex, FacesPanel panel) {
		panelsList.add (panelIndex, panel);
	}

	@Override
	public void removePanel (int panelIndex) {
		panelsList.remove (panelIndex);
	}

	@Override
	public boolean containsPanel (FacesPanel panel) {
		return panelsList.contains (panel);
	}

	public final class TableDockView extends FacesPanelView {

		public TableDockView () {
			super (TableDock.this);
		}

		void panelListChanged (ListChangeListener.Change<FacesPanel> c) {
			checkOnAppThread ();

			while (c.next ()) {
				c.getAddedSubList ().forEach ((panel) -> {
					getChildren ().add (panel.getView ());
				});
				c.getRemoved ().forEach ((panel) -> {
					getChildren ().remove (panel.getView ());
				});
			}
			refreshView ();
		}

		private void refreshView () {
			int paneX = 0;
			int paneY = 0;

			for (int panelIndex = 0; panelIndex < panelsList.size (); panelIndex++) {
				FacesPanelView panelView = panelsList.get (panelIndex).getView ();

				panelView.layoutXProperty ().bind (eachPaneWidth.multiply (paneX));
				panelView.layoutYProperty ().bind (eachPaneHeight.multiply (paneY));

				paneX++;
				if (paneX >= tableWidth) {
					paneX = 0;
					paneY++;
				}

				panelView.minWidthProperty ().bind (eachPaneWidth);
				panelView.maxWidthProperty ().bind (eachPaneWidth);
				panelView.minHeightProperty ().bind (eachPaneHeight);
				panelView.maxHeightProperty ().bind (eachPaneHeight);
			}
		}

	}

}
