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
public class LongDataElement extends DataElement<Long> {

	public LongDataElement (String elementTitle, String elementId, Long defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, Long.class, defaultValue, persistenceProvider);
	}

	public LongDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, 0L, persistenceProvider);
	}

	@Override
	public String valueToStoredString (Long val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public Long storedStringToValue (String str) {
		if (str == null || str.equals ("") || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}

		return Long.parseLong (str);
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<LongDataElement, Long> {

		public JsonSerializer () {
			super (LongDataElement.class, Long.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
