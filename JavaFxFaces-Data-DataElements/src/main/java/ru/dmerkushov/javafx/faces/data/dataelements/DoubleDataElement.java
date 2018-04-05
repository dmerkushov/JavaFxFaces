/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DoubleDataElement extends DataElement<Double> {

	public DoubleDataElement (String elementTitle, String elementName, Double defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementName, Double.class, defaultValue, persistenceProvider);
	}

	public DoubleDataElement (String elementTitle, String elementName, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementName, 0.0, persistenceProvider);
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

}
