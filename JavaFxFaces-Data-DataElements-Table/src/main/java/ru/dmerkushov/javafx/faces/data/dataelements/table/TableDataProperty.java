/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.util.Iterator;
import javax.json.JsonObject;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author dmerkushov
 */
public class TableDataProperty extends DataElementValueProperty<TableData> {

	public TableDataProperty (TableData tableData) {
		super (TableData.class);

		getValueProperty ().set (tableData);
	}

	@Override
	public void updateValue (TableData newVal) {
		TableData current = getValueProperty ().get ();

		if (current != null && newVal != null && !current.getRowPattern ().equals (newVal.getRowPattern ())) {
			throw new IllegalArgumentException ("Row patterns for the current and new table datas don't match: current is " + current.getRowPattern () + ", the new is " + newVal.getRowPattern ());
		}

		if (current == null || newVal == null) {
			getValueProperty ().set (newVal);
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

	@Override
	public JsonObject valueToJson (TableData value) {
		return value.toStoredJson ().build ();
	}

	@Override
	public TableData jsonToValue (JsonObject json) {
		try {
			return TableData.fromStoredJson (json);
		} catch (ClassNotFoundException ex) {
			throw new TableDataElementException (ex);
		}
	}

	@Override
	public String valueToDisplayedString (TableData value) {
		return "(TABLE)";
	}
}
