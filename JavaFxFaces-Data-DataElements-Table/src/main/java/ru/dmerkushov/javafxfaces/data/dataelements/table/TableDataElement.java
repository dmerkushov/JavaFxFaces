/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.Objects;
import javafx.beans.property.Property;
import javafx.scene.Node;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 */
public class TableDataElement extends DataElement<TableData> {

	private TableDataElementView view = null;
	private TableData data;

	public TableDataElement (String elementTitle, String elementId, TableData defaultData, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, TableData.class, defaultData, persistenceProvider);

		Objects.requireNonNull (defaultData, "defaultData");

		this.data = defaultData;
	}

	@Override
	public String valueToStoredString (TableData val) {
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TableData storedStringToValue (String str) {
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Node getValueFxNode () {
		if (view == null) {
			view = new TableDataElementView (this);
		}

		return view;
	}

	@Override
	public Property<TableData> getCurrentValueProperty () {
		return data.getTableDataProperty ();
	}

}
