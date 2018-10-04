/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;

/**
 *
 * @author dmerkushov
 */
public class ListDataElementException extends DataElementException {

	public ListDataElementException () {
	}

	public ListDataElementException (String message) {
		super (message);
	}

	public ListDataElementException (String message, Throwable cause) {
		super (message, cause);
	}

	public ListDataElementException (Throwable cause) {
		super (cause);
	}

}
