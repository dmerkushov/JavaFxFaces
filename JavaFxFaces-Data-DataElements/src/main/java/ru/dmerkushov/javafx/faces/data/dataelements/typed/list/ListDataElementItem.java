/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

import java.util.Objects;

/**
 *
 * @author dmerkushov
 * @param <T> the contained data type
 */
public abstract class ListDataElementItem<T> {

	private T contained;

	/**
	 * Constructs a list item from a string representation of the contained
	 * object.
	 *
	 * A constructor with a single {@link String} parameter must be present in
	 * every ancestor of this class, because it is used in
	 * {@link ListDataElement}
	 *
	 * @param containedStr must not be null
	 */
	public ListDataElementItem (String containedStr) {
		setContainedAsStoredString (containedStr);
	}

	/**
	 * Get the contained object
	 *
	 * @return
	 */
	public final T getContained () {
		return contained;
	}

	/**
	 * Get the contained object in the form of its string representation
	 *
	 * @return
	 */
	public final String getContainedAsStoredString () {
		return containedToStoredString (contained);
	}

	private void setContainedAsStoredString (String containedStr) {
		Objects.requireNonNull (containedStr, "containedStr");

		this.contained = containedFromStoredString (containedStr);
	}

	/**
	 * Create a value of the contained type from its string representation
	 *
	 * @param str
	 * @return
	 */
	protected abstract T containedFromStoredString (String str);

	/**
	 * Create a string representation of a value of the contained type
	 *
	 * @param contained
	 * @return
	 */
	protected abstract String containedToStoredString (T contained);

	/**
	 * Use the contained value's <b>toString()</b>, so it is used in the list
	 *
	 * @return
	 * @see Object#toString()
	 * @see Objects#toString(java.lang.Object)
	 */
	@Override
	public String toString () {
		return Objects.toString (contained);
	}

	@Override
	public final int hashCode () {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode (this.contained);
		return hash;
	}

	@Override
	public final boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass () != obj.getClass ()) {
			return false;
		}
		final ListDataElementItem<?> other = (ListDataElementItem<?>) obj;
		return Objects.equals (this.contained, other.contained);
	}

}
