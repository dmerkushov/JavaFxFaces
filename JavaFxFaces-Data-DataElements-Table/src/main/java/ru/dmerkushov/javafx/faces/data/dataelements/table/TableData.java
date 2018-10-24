/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.io.StringReader;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

	private final TableDataRowPattern rp;
	private final ObservableList<TableDataRow> rows = FXCollections.<TableDataRow>observableArrayList ();
	private final SimpleObjectProperty<Callable<TableDataRow>> dataRowCreatorProperty = new SimpleObjectProperty<> (null);
	private final SimpleBooleanProperty rowsDeletableProperty = new SimpleBooleanProperty (false);
	private final SimpleObjectProperty<TableDataRowDeleteFilter> deleteFilterProperty = new SimpleObjectProperty<> (null);

	public TableData (TableDataRowPattern rowPattern) {
		Objects.requireNonNull (rowPattern, "rowPattern");

		this.rp = rowPattern;
	}

	public ObservableList<TableDataRow> getRows () {
		return rows;
	}

	public TableDataRowPattern getRowPattern () {
		return rp;
	}

	public TableDataRow prepareRow (DataElement... dataElements) {
		return new TableDataRow (this, dataElements);
	}

	public SimpleObjectProperty<Callable<TableDataRow>> getDataRowCreatorProperty () {
		return dataRowCreatorProperty;
	}

	public BooleanProperty getRowsDeletableProperty () {
		return rowsDeletableProperty;
	}

	public SimpleObjectProperty<TableDataRowDeleteFilter> getDeleteFilterProperty () {
		return deleteFilterProperty;
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

			int dataElementCount = tdr.getDataElementCount ();
			for (int i = 0; i < dataElementCount; i++) {
				DataElement de = tdr.getDataElement (i);
				JsonObject deJob = DataElementJsonSerializerRegistry.getInstance ().serialize (de);
				rowJab.add (deJob);
			}

			rowsJab.add (rowJab);
		}
		job.add ("rows", rowsJab);

		job.add ("rowsDeletable", getRowsDeletableProperty ().getValue ());

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

		TableDataRowPattern rp = new TableDataRowPattern (columnTitles, dataClasses);

		TableData td = new TableData (rp);

		JsonArray rowsJa = json.getJsonArray ("rows");
		for (int i = 0; i < rowsJa.size (); i++) {
			JsonArray rowJa = rowsJa.getJsonArray (i);
			DataElement[] des = new DataElement[rowJa.size ()];
			for (int j = 0; j < rowJa.size (); j++) {
				des[j] = DataElementJsonSerializerRegistry.getInstance ().deserialize (rowJa.getJsonObject (j), null);
			}
			TableDataRow tdr = td.prepareRow (des);
			td.getRows ().add (tdr);
		}

		boolean rowsDeletable = json.getBoolean ("rowsDeletable", false);
		td.getRowsDeletableProperty ().setValue (rowsDeletable);

		return td;
	}
}
