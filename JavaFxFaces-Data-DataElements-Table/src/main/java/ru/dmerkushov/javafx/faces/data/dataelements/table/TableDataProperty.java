/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author dmerkushov
 */
public final class TableDataProperty extends SimpleObjectProperty<TableData> {

	TableDataProperty (TableData tableData) {
		super (tableData);
	}

	void valueChanged () {
		this.fireValueChangedEvent ();
	}
}
