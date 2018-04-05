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
public class LongDataElement extends DataElement<Long> {

	public LongDataElement (String elementTitle, String elementName, Long defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementName, Long.class, defaultValue, persistenceProvider);
	}

	public LongDataElement (String elementTitle, String elementName, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementName, 0L, persistenceProvider);
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

}
