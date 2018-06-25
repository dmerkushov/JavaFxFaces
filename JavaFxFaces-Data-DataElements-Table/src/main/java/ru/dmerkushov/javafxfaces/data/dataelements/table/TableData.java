/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.ArrayList;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author dmerkushov
 */
public class TableData {

	private final RowPattern rp;
	private final ArrayList<TableDataRow> rows = new ArrayList<> ();
	private TableDataProperty tdp = null;

	public TableData (RowPattern rp) {
		Objects.requireNonNull (rp, "rm");

		this.rp = rp;
	}

	public TableDataProperty getTableDataProperty () {
		if (tdp == null) {
			tdp = new TableDataProperty ();
		}

		return tdp;
	}

	public void addRow (TableDataRow tdr) {
		Objects.requireNonNull (tdr, "tdr");

		insertRow (rows.size (), tdr);

		getTableDataProperty ().valueChanged ();
	}

	public void insertRow (int rowIndex, TableDataRow tdr) {
		Objects.requireNonNull (tdr, "tdr");

		rows.add (rowIndex, tdr);

		tdr.addListeners ();

		getTableDataProperty ().valueChanged ();
	}

	public void removeRow (int rowIndex) {
		TableDataRow tdr = rows.remove (rowIndex);

		tdr.removeListeners ();

		getTableDataProperty ().valueChanged ();
	}

	public TableDataRow getRow (int rowIndex) {
		return rows.get (rowIndex);
	}

	public RowPattern getRowPattern () {
		return rp;
	}

	////////////////////////////////////////////////////////////////////////////
	public class TableDataProperty extends SimpleObjectProperty<TableData> {

		void valueChanged () {
			this.fireValueChangedEvent ();
		}
	}

	////////////////////////////////////////////////////////////////////////////
	public class TableDataRow {

		DataElement[] dataElements;

		public TableDataRow (DataElement... dataElements) {
			Objects.requireNonNull (dataElements, "dataElements");

			if (dataElements.length != TableData.this.rp.dataClasses.length) {
				throw new IllegalArgumentException ("Data element list for table row must have the same length as the row model: " + TableData.this.rp.dataClasses.length + ", but is " + dataElements.length);
			}

			for (int i = 0; i < dataElements.length; i++) {
				Objects.requireNonNull (dataElements[i], "dataElements[" + i + "]");

				if (!TableData.this.rp.dataClasses[i].isAssignableFrom (dataElements[i].getClass ())) {
					throw new IllegalArgumentException ("Data element class in position " + i + " must be assignable to " + TableData.this.rp.dataClasses[i].getCanonicalName () + ", but is " + dataElements[i].getClass ().getCanonicalName ());
				}
			}

			this.dataElements = dataElements;
		}

		public DataElement getDataElement (int index) {
			return dataElements[index];
		}

		private ChangeListener cl = new ChangeListener () {
			@Override
			public void changed (ObservableValue observable, Object oldValue, Object newValue) {
				TableData.this.tdp.valueChanged ();
			}
		};

		void addListeners () {
			for (DataElement dataElement : dataElements) {
				dataElement.getCurrentValueProperty ().addListener (cl);
			}
		}

		void removeListeners () {
			for (DataElement dataElement : dataElements) {
				dataElement.getCurrentValueProperty ().removeListener (cl);
			}
		}

	}

	////////////////////////////////////////////////////////////////////////////
	public static class RowPattern {

		public final Class<DataElement>[] dataClasses;
		public final String[] columnTitles;

		public RowPattern (String[] columnTitles, Class<DataElement>[] dataClasses) {
			Objects.requireNonNull (columnTitles, "columnTitles");
			Objects.requireNonNull (dataClasses, "dataClasses");

			if (columnTitles.length != dataClasses.length) {
				throw new IllegalArgumentException ("Column title list length (" + columnTitles.length + ") does not match the data classes list length (" + dataClasses.length + ")");
			}
			if (dataClasses.length == 0) {
				throw new IllegalArgumentException ("Empty class list defined for table row model");
			}

			this.dataClasses = new Class[dataClasses.length];
			this.columnTitles = new String[columnTitles.length];

			for (int i = 0; i < dataClasses.length; i++) {
				Objects.requireNonNull (dataClasses[i], "dataClasses[" + i + "]");
				this.dataClasses[i] = dataClasses[i];

				this.columnTitles[i] = columnTitles[i] != null ? columnTitles[i] : "";
			}
		}

	}

}
