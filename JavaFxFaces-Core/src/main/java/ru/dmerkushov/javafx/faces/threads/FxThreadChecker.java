package ru.dmerkushov.javafx.faces.threads;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FxThreadChecker {

	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock (true);
	private static int fxAppThreadHash;
	private static long fxAppThreadId;
	private static String fxAppThreadName;
	private static boolean fxAppThreadSet = false;

	public static void setAppThread () {
		lock.writeLock ().lock ();
		try {
			if (!fxAppThreadSet) {
				fxAppThreadHash = Thread.currentThread ().hashCode ();
				fxAppThreadId = Thread.currentThread ().getId ();
				fxAppThreadName = Thread.currentThread ().getName ();
				fxAppThreadSet = true;
			}
		} finally {
			lock.writeLock ().unlock ();
		}
	}

	/**
	 * Check that we are on the JavaFX Application thread.
	 *
	 * @return true if are on the app thread, or the app thread is not set by
	 * {@link setAppThread()}
	 */
	public static boolean isOnAppThread () {
		boolean onAppThread = true;
		lock.readLock ().lock ();
		try {
			if (fxAppThreadSet) {
				if (fxAppThreadHash != Thread.currentThread ().hashCode ()) {
					onAppThread = false;
				}
				if (onAppThread && fxAppThreadId != Thread.currentThread ().getId ()) {
					onAppThread = false;
				}
				if (onAppThread && !fxAppThreadName.equals (Thread.currentThread ().getName ())) {
					onAppThread = false;
				}
			}
		} finally {
			lock.readLock ().unlock ();
		}
		return onAppThread;
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
