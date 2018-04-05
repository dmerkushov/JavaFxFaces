/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <T>
 */
public class EnumDataElement<T> extends DataElement<T> {

	public EnumDataElement (String elementTitle, String elementId, Class<T> elementType, T defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, elementType, defaultValue, persistenceProvider);

		if (!elementType.isEnum ()) {
			throw new IllegalArgumentException ("EnumDataElement can wrap only an enum class. The supplied class is not declared as an enum: " + elementType.getCanonicalName ());
		}
	}

	@Override
	public String valueToStoredString (T val) {
		if (val == null) {
			return "null";
		}

		try {
			Method nameMethod = elementType.getMethod ("name");
			return (String) nameMethod.invoke (val);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | RuntimeException ex) {
			throw new DataElementException (ex);
		}
	}

	@Override
	public T storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("")) {
			return null;
		}

		try {
			return (T) Enum.valueOf (elementType, str);
		} catch (RuntimeException ex) {
			throw new DataElementException (ex);
		}
	}

	public T[] values () {
		try {
			Method valuesMethod = elementType.getMethod ("values");
			return (T[]) valuesMethod.invoke (null);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | RuntimeException ex) {
			throw new DataElementException (ex);
		}
	}

	public String[] stringValues () {
		T[] values = values ();
		String[] stringValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			stringValues[i] = ((Enum) values[i]).name ();
		}
		return stringValues;
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			ComboBox comboBox = new ComboBox<> ();
			comboBox.getItems ().addAll (values ());
			comboBox.valueProperty ().bindBidirectional (currentValueProperty);
			valueFxNode = comboBox;
		}

		return valueFxNode;
	}

}
