/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializer;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class JsonArrayDataElement extends DataElement<JsonArray> {

	public JsonArrayDataElement (String elementTitle, String elementId, JsonArray defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (
				elementTitle,
				elementId,
				JsonArray.class,
				(defaultValue != null ? defaultValue : Json.createArrayBuilder ().build ()),
				persistenceProvider
		);
	}

	public JsonArrayDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, Json.createArrayBuilder ().build (), persistenceProvider);
	}

	@Override
	public String valueToStoredString (JsonArray val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public JsonArray storedStringToValue (String str) {
		if (str == null || str.equals ("") || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}

		JsonReader jr = Json.createReader (new StringReader (str));
		return jr.readArray ();
	}

	public static class JsonSerializer implements DataElementJsonSerializer<JsonArrayDataElement> {

		@Override
		public JsonObjectBuilder serialize (JsonArrayDataElement dataElement) {
			JsonObjectBuilder job = Json.createObjectBuilder ();

			job.add ("elementTitle", dataElement.elementTitle);
			job.add ("elementId", dataElement.elementId);
			job.add ("defaultValue", dataElement.defaultValue);
			job.add ("currentValue", dataElement.currentValueProperty.get ());

			return job;
		}

		@Override
		public JsonArrayDataElement deserialize (JsonObject json, DataElementPersistenceProvider persistenceProvider) {
			String elementTitle = json.getString ("elementTitle", "");
			String elementId = json.getString ("elementId", "");
			JsonArray defaultValue = json.getJsonArray ("defaultValue");

			JsonArrayDataElement jade = new JsonArrayDataElement (elementTitle, elementId, defaultValue, persistenceProvider);

			JsonArray currentValue = json.getJsonArray ("currentValue");
			jade.getCurrentValueProperty ().set (currentValue);

			return jade;
		}

	}

}
