/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class NoSavePersistenceProvider implements DataElementPersistenceProvider {

	public NoSavePersistenceProvider () {
	}

	@Override
	public String load (DataElement dataElement) {
		// Insert the default value
		return dataElement.valueToStoredString (dataElement.defaultValue);
	}

	@Override
	public void save (DataElement dataElement) {
		// Do nothing
	}

}
