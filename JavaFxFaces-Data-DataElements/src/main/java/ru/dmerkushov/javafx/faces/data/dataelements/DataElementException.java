/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DataElementException extends RuntimeException {

	public DataElementException () {
	}

	public DataElementException (String message) {
		super (message);
	}

	public DataElementException (String message, Throwable cause) {
		super (message, cause);
	}

	public DataElementException (Throwable cause) {
		super (cause);
	}

}
