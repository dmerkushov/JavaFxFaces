/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.json;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;

/**
 *
 * @author dmerkushov
 */
public class DataElementSerializerException extends DataElementException {

	public DataElementSerializerException () {
	}

	public DataElementSerializerException (String message) {
		super (message);
	}

	public DataElementSerializerException (String message, Throwable cause) {
		super (message, cause);
	}

	public DataElementSerializerException (Throwable cause) {
		super (cause);
	}

}
