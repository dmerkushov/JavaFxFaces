/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.io.StringReader;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerRegistry;

/**
 *
 * @author dmerkushov
 */
public final class TableData {

	private final RowPattern rp;
	private final ObservableList<TableDataRow> rows = FXCollections.<TableDataRow>observableArrayList ();
	private SimpleObjectProperty<Callable<TableDataRow>> dataRowCreatorProperty = new SimpleObjectProperty<> (null);

	public TableData (RowPattern rowPattern) {
		Objects.requireNonNull (rowPattern, "rowPattern");

		this.rp = rowPattern;
	}

	ObservableList<TableDataRow> getRows () {
		return rows;
	}

	public RowPattern getRowPattern () {
		return rp;
	}

	public TableDataRow newRow (DataElement... dataElements) {
		return new TableDataRow (dataElements);
	}

	public TableDataRow createNewRow () {

		if (dataRowCreatorProperty.get () == null) {
			return null;
		}

		try {
			return dataRowCreatorProperty.get ().call ();
		} catch (Exception ex) {
			throw new TableDataElementException (ex);
		}
	}

	public SimpleObjectProperty<Callable<TableDataRow>> getDataRowCreatorProperty () {
		return dataRowCreatorProperty;
	}

	String toStoredString () {
		JsonObjectBuilder job = toStoredJson ();

		return job.build ().toString ();
	}

	JsonObjectBuilder toStoredJson () {
		JsonObjectBuilder job = Json.createObjectBuilder ();

		JsonObjectBuilder rpJob = Json.createObjectBuilder ();

		JsonArrayBuilder rpCtJab = Json.createArrayBuilder ();
		int rpSize = rp.columnTitles.length;
		for (int i = 0; i < rpSize; i++) {
			rpCtJab.add (rp.columnTitles[i]);
		}
		rpJob.add ("columnTitles", rpCtJab);

		JsonArrayBuilder rpDcJab = Json.createArrayBuilder ();
		for (int i = 0; i < rpSize; i++) {
			rpDcJab.add (rp.dataClasses[i].getCanonicalName ());
		}
		rpJob.add ("dataClasses", rpDcJab);

		job.add ("rowPattern", rpJob);

		ObservableList<TableDataRow> rows = getRows ();
		JsonArrayBuilder rowsJab = Json.createArrayBuilder ();
		for (TableDataRow tdr : rows) {
			JsonArrayBuilder rowJab = Json.createArrayBuilder ();
			for (DataElement de : tdr.dataElements) {
				JsonObject deJob = DataElementJsonSerializerRegistry.getInstance ().serialize (de);
				rowJab.add (deJob);
			}
			rowsJab.add (rowJab);
		}
		job.add ("rows", rowsJab);

		return job;
	}

	static TableData fromStoredString (String str) throws ClassNotFoundException {
		JsonReader jr = Json.createReader (new StringReader (str));
		JsonObject jo = jr.readObject ();

		return fromStoredJson (jo);
	}

	static TableData fromStoredJson (JsonObject json) throws ClassNotFoundException {

		JsonObject rpJo = json.getJsonObject ("rowPattern");
		JsonArray rpCt = rpJo.getJsonArray ("columnTitles");
		JsonArray rpDc = rpJo.getJsonArray ("dataClasses");

		int rpSize = rpCt.size ();
		String[] columnTitles = new String[rpSize];
		for (int i = 0; i < rpSize; i++) {
			columnTitles[i] = rpCt.getString (i, "");
		}

		Class<DataElement>[] dataClasses = new Class[rpSize];
		for (int i = 0; i < rpSize; i++) {
			dataClasses[i] = (Class<DataElement>) Class.forName (rpDc.getString (i));
		}

		RowPattern rp = new RowPattern (columnTitles, dataClasses);

		TableData td = new TableData (rp);

		JsonArray rowsJa = json.getJsonArray ("rows");
		for (int i = 0; i < rowsJa.size (); i++) {
			JsonArray rowJa = rowsJa.getJsonArray (i);
			DataElement[] des = new DataElement[rowJa.size ()];
			for (int j = 0; j < rowJa.size (); j++) {
				des[j] = DataElementJsonSerializerRegistry.getInstance ().deserialize (rowJa.getJsonObject (j), null);
			}
			TableDataRow tdr = td.newRow (des);
			td.getRows ().add (tdr);
		}

		return td;
	}

	////////////////////////////////////////////////////////////////////////////
	public final class TableDataRow {

		DataElement[] dataElements;

		private TableDataRow (DataElement... dataElements) {
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

//		private ChangeListener cl = new ChangeListener () {
//			@Override
//			public void changed (ObservableValue observable, Object oldValue, Object newValue) {
//				getRows ().valueChanged ();
//			}
//		};
//
//		void addListeners () {
//			for (DataElement dataElement : dataElements) {
//				dataElement.getCurrentValueProperty ().addListener (cl);
//			}
//		}
//
//		void removeListeners () {
//			for (DataElement dataElement : dataElements) {
//				dataElement.getCurrentValueProperty ().removeListener (cl);
//			}
//		}
	}

	////////////////////////////////////////////////////////////////////////////
	public static final class RowPattern {

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
