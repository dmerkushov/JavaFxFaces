/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author dmerkushov
 * @param <T>
 */
public class DataElementValueProperty<T> extends SimpleObjectProperty<T> {

	public DataElementValueProperty () {
	}

	public DataElementValueProperty (T initialValue) {
		super (initialValue);
	}

	public DataElementValueProperty (Object bean, String name) {
		super (bean, name);
	}

	public DataElementValueProperty (Object bean, String name, T initialValue) {
		super (bean, name, initialValue);
	}

	public void updateValue (T newVal) {
		super.setValue (newVal);
	}

	/**
	 * @deprecated Must use {@link #updateValue()} for DataElementValueProperty
	 * @param newVal
	 */
	@Override
	public final void setValue (T newVal) {
		super.setValue (newVal);
	}

	/**
	 * @deprecated Must use {@link #updateValue()} for DataElementValueProperty
	 * @param newVal
	 */
	@Override
	public final void set (T newVal) {
		super.set (newVal);
	}

}
