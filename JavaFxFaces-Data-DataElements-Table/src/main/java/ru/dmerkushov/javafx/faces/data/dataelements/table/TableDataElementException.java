/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementException extends DataElementException {

	public TableDataElementException () {
	}

	public TableDataElementException (String message) {
		super (message);
	}

	public TableDataElementException (String message, Throwable cause) {
		super (message, cause);
	}

	public TableDataElementException (Throwable cause) {
		super (cause);
	}

}
