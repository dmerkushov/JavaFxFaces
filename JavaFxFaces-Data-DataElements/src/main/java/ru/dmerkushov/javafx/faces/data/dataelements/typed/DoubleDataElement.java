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
public class DoubleDataElement extends DataElement<Double> {

	public DoubleDataElement (String elementTitle, String elementId, Double defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, Double.class, defaultValue, persistenceProvider);
	}

	public DoubleDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, 0.0, persistenceProvider);
	}

	@Override
	public String valueToStoredString (Double val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public Double storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}
		if (str.equals ("")) {
			return 0.0;
		}

		return Double.parseDouble (str);
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DoubleDataElement, Double> {

		public JsonSerializer () {
			super (DoubleDataElement.class, Double.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
