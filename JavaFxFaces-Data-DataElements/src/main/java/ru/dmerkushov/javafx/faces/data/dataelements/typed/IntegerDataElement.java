/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class IntegerDataElement extends DataElement<Integer> {

	public IntegerDataElement (String elementTitle, String elementName, Integer defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementName, Integer.class, defaultValue, persistenceProvider);
	}

	public IntegerDataElement (String elementTitle, String elementName, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementName, 0, persistenceProvider);
	}

	@Override
	public String valueToStoredString (Integer val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public Integer storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}
		if (str.equals ("")) {
			return 0;
		}

		return Integer.parseInt (str);
	}

}
