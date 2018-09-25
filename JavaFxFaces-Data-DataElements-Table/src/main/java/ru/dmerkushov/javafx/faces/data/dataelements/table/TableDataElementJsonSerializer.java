/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializer;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementSerializerException;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementJsonSerializer implements DataElementJsonSerializer<TableDataElement> {

	@Override
	public JsonObjectBuilder serialize (TableDataElement tde) {

		JsonObjectBuilder job = Json.createObjectBuilder ();

		TableData td = tde.getCurrentValueProperty ().getValue ();
		JsonObjectBuilder tdJob = td.toStoredJson ();

		job.add ("tableData", tdJob);

		JsonObjectBuilder viewJob = Json.createObjectBuilder ();
		TableDataElementView tdev = tde.getTableDataElementView ();
		ObservableList<TableColumn> tdevColumns = tdev.getColumns ();
		JsonArrayBuilder columnWidthJab = Json.createArrayBuilder ();
		for (TableColumn column : tdevColumns) {
			columnWidthJab.add (column.getWidth ());
		}
		viewJob.add ("columnWidth", columnWidthJab);

		job.add ("tableView", viewJob);

		job.add ("elementTitle", tde.elementTitle);
		job.add ("elementId", tde.elementId);

		return job;
	}

	@Override
	public TableDataElement deserialize (JsonObject json, DataElementPersistenceProvider persistenceProvider) {
		JsonObject tableDataJson = json.getJsonObject ("tableData");
		TableData td;
		try {
			td = TableData.fromStoredJson (tableDataJson);
		} catch (ClassNotFoundException ex) {
			throw new DataElementSerializerException (ex);
		}

		String elementTitle = json.getString ("elementTitle", "");
		String elementId = json.getString ("elementId", "");

		TableDataElement tde = new TableDataElement (elementTitle, elementId, td, persistenceProvider);

		JsonObject tableViewJson = json.getJsonObject ("tableView");
		JsonArray columnWidth = tableViewJson.getJsonArray ("columnWidth");
		TableDataElementView tdev = tde.getTableDataElementView ();
		ObservableList<TableColumn> tdevColumns = tdev.getColumns ();
		for (int i = 0; i < columnWidth.size (); i++) {
			double w = columnWidth.getJsonNumber (i).doubleValue ();
			tdevColumns.get (i).prefWidthProperty ().set (w);
		}

		return tde;
	}

}
