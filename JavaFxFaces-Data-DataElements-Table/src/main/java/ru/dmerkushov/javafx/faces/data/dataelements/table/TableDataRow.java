/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.util.Objects;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

public final class TableDataRow {

	private DataElement[] dataElements;
	private final TableData tableData;

	TableDataRow (final TableData _tableData, DataElement... _dataElements) {
		Objects.requireNonNull (_tableData, "tableData");
		Objects.requireNonNull (_dataElements, "dataElements");

		this.tableData = _tableData;

		TableDataRowPattern rp = tableData.getRowPattern ();
		if (_dataElements.length != rp.dataClasses.length) {
			throw new IllegalArgumentException ("Data element list for table row must have the same length as the row model: " + rp.dataClasses.length + ", but is " + _dataElements.length);
		}
		for (int i = 0; i < _dataElements.length; i++) {
			Objects.requireNonNull (_dataElements[i], "dataElements[" + i + "]");
			if (!rp.dataClasses[i].isAssignableFrom (_dataElements[i].getClass ())) {
				throw new IllegalArgumentException ("Data element class in position " + i + " must be assignable to " + rp.dataClasses[i].getCanonicalName () + ", but is " + _dataElements[i].getClass ().getCanonicalName ());
			}
		}
		this.dataElements = _dataElements;
	}

	public DataElement getDataElement (int index) {
		return dataElements[index];
	}

	public int getDataElementCount () {
		return dataElements.length;
	}

}
