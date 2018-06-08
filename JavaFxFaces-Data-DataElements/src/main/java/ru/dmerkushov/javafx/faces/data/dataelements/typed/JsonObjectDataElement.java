/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class JsonObjectDataElement extends DataElement<JsonObject> {

	public JsonObjectDataElement (String elementTitle, String elementId, JsonObject defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (
				elementTitle,
				elementId,
				JsonObject.class,
				(defaultValue != null ? defaultValue : Json.createObjectBuilder ().build ()),
				persistenceProvider
		);
	}

	public JsonObjectDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, Json.createObjectBuilder ().build (), persistenceProvider);
	}

	@Override
	public String valueToStoredString (JsonObject val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public JsonObject storedStringToValue (String str) {
		if (str == null || str.equals ("") || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}

		JsonReader jr = Json.createReader (new StringReader (str));
		return jr.readObject ();
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<JsonObjectDataElement, JsonObject> {

		public JsonSerializer () {
			super (JsonObjectDataElement.class, JsonObject.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
