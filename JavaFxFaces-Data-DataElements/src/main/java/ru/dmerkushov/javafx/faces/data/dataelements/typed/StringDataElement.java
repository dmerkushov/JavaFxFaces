/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class StringDataElement extends DataElement<String> {

	public StringDataElement (String elementTitle, String elementId, String defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, String.class, defaultValue, persistenceProvider);
	}

	public StringDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, "", persistenceProvider);
	}

	@Override
	public String valueToStoredString (String val) {
		if (val == null) {
			return "null";
		}

		return val;
	}

	@Override
	public String storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}

		return str;
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<StringDataElement, String> {

		public JsonSerializer () {
			super (StringDataElement.class, String.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
