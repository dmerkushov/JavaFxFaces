/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class JsonArrayDataElement extends DataElement<JsonArray> {

	public JsonArrayDataElement (String elementTitle, String elementName, JsonArray defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementName, JsonArray.class, defaultValue, persistenceProvider);
	}

	public JsonArrayDataElement (String elementTitle, String elementName, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementName, Json.createArrayBuilder ().build (), persistenceProvider);
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

}
