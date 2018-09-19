/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.util.Iterator;
import javafx.beans.property.SimpleObjectProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author dmerkushov
 */
public final class TableDataProperty extends DataElementValueProperty<TableData> {

	TableDataProperty (TableData tableData) {
		super (tableData);
	}

	void valueChanged () {
		this.fireValueChangedEvent ();
	}

	@Override
	public void updateValue (TableData newVal) {
		TableData current = get ();

		if (current != null && newVal != null && !current.getRowPattern ().equals (newVal.getRowPattern ())) {
			throw new IllegalArgumentException ("Row patterns for the current and new table datas don't match: current is " + current.getRowPattern () + ", the new is " + newVal.getRowPattern ());
		}

		if (current == null || newVal == null) {
			((SimpleObjectProperty) this).setValue (newVal);
			return;
		}

		Iterator<TableDataRow> iter = current.getRows ().iterator ();
		while (iter.hasNext ()) {
			iter.next ();
			iter.remove ();
		}

		for (TableDataRow row : newVal.getRows ()) {
			current.getRows ().add (row);
		}
	}
}
