/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DataElementPersistenceProviderException extends DataElementException {

	public DataElementPersistenceProviderException () {
	}

	public DataElementPersistenceProviderException (String message) {
		super (message);
	}

	public DataElementPersistenceProviderException (String message, Throwable cause) {
		super (message, cause);
	}

	public DataElementPersistenceProviderException (Throwable cause) {
		super (cause);
	}

}
