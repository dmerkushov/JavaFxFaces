/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.panel;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ru.dmerkushov.javafx.faces.FacesUtil;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DataElementPagePanelView extends FacesPanelView {

	DataElementPagePanel depPanel;

	public DataElementPagePanelView (DataElementPagePanel panel) {
		super (panel);

		this.depPanel = panel;

		initialize ();
	}

	private void initialize () {

		final VBox vb = new VBox ();

		Label titleLabel = new Label (depPanel.getDisplayName ());
		titleLabel.getStyleClass ().add ("page-title");
		vb.getChildren ().add (titleLabel);

		GridPane grid = new GridPane ();
		grid.getStyleClass ().add ("gridpane-prop");

		ArrayList<DataElement> dataElements = depPanel.dataElementList.getDataElements ();
		for (int i = 0; i < dataElements.size (); i++) {
			DataElement dataElement = dataElements.get (i);

			grid.add (dataElement.getNameFxNode (), 0, i);
			grid.add (dataElement.getValueFxNode (), 1, i);
		}

		ScrollPane scrollPane = new ScrollPane (grid);
		scrollPane.setVbarPolicy (ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy (ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setBorder (Border.EMPTY);

		FacesUtil.bindWidthHeight (scrollPane, vb.widthProperty (), vb.heightProperty ().subtract (titleLabel.heightProperty ()));

		vb.getChildren ().add (scrollPane);

		FacesUtil.bindWidthHeight (vb, this.widthProperty (), this.heightProperty ());

		this.getChildren ().add (vb);
	}

}
