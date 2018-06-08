/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
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

	public static class JsonSerializer extends DataElementJsonSerializerImpl<JsonArrayDataElement, JsonArray> {

		public JsonSerializer () {
			super (JsonArrayDataElement.class, JsonArray.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
