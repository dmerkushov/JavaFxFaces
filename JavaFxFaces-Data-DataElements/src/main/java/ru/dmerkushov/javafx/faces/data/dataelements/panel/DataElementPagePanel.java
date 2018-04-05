/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.panel;

import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public abstract class DataElementPagePanel extends FacesPanel {

	private DataElementPagePanelView view = null;

	final DataElementSet dataElementList;
	private final String displayName;

	public DataElementPagePanel (String displayName) {
		super ();
		this.displayName = displayName;
		this.dataElementList = new DataElementSet ();
	}

	@Override
	public String getDisplayName () {
		return displayName;
	}

	@Override
	public FacesPanelView getView () {
		if (view == null) {
			view = new DataElementPagePanelView (this);
		}

		return view;
	}

	public DataElementSet getDataElementList () {
		return dataElementList;
	}

}
