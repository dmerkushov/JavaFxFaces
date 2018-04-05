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
public class StringDataElement extends DataElement<String> {

	public StringDataElement (String elementTitle, String elementName, String defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementName, String.class, defaultValue, persistenceProvider);
	}

	public StringDataElement (String elementTitle, String elementName, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementName, "", persistenceProvider);
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

}
