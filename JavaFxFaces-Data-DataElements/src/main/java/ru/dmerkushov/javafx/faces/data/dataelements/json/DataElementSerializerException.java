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
