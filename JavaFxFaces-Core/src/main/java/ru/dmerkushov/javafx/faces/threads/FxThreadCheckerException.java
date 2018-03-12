package ru.dmerkushov.javafx.faces.threads;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FxThreadCheckerException extends RuntimeException {

	public FxThreadCheckerException () {
	}

	public FxThreadCheckerException (String message) {
		super (message);
	}

	public FxThreadCheckerException (String message, Throwable cause) {
		super (message, cause);
	}

	public FxThreadCheckerException (Throwable cause) {
		super (cause);
	}

}
