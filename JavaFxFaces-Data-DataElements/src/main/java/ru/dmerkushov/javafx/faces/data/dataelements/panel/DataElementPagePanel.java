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

	final DataElementSet dataElementSet;
	private final String displayName;
	private final DataElementPageType pageType;

	public DataElementPagePanel (String displayName) {
		this (displayName, null);
	}

	public DataElementPagePanel (String displayName, DataElementPageType pageType) {
		super ();
		this.displayName = displayName;
		this.dataElementSet = new DataElementSet ();
		this.pageType = (pageType == null ? DataElementPageType.GRID : pageType);
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

	public DataElementPageType getPageType () {
		return pageType;
	}

	public DataElementSet getDataElementSet () {
		return dataElementSet;
	}

}
