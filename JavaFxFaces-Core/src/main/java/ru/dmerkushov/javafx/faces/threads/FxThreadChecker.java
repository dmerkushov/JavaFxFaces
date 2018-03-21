package ru.dmerkushov.javafx.faces.threads;

import javafx.application.Platform;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FxThreadChecker {

	/**
	 * Check that we are on the JavaFX Application thread.
	 *
	 * @return true if are on the app thread, or the app thread is not set by
	 * {@link setAppThread()}
	 */
	public static boolean isOnAppThread () {
		return Platform.isFxApplicationThread ();
	}

	/**
	 * Check that we are on the JavaFX Application thread, and throw a
	 * non-checked {@link FxThreadCheckerException} if not. Can check only if
	 * the JavaFX application thread is set previously by {@link setAppThread()}
	 */
	public static void checkOnAppThread () {
		if (!isOnAppThread ()) {
			throw new FxThreadCheckerException ("Not on JavaFX Application thread: " + Thread.currentThread ().toString ());
		}
	}

}
