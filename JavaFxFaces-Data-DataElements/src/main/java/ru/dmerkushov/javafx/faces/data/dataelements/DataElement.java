/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <T> The type of the data element in the database
 */
public abstract class DataElement<T> {

	/**
	 * The data element's title to be displayed
	 */
	public final String elementTitle;

	/**
	 * The data element's ID for the storing purposes
	 */
	public final String elementId;

	/**
	 * The type of data for the data element
	 */
	public final Class valueType;

	/**
	 * The default value for the data element
	 */
	public final T defaultValue;

	/**
	 * Persistence provider for the data element
	 */
	private DataElementPersistenceProvider persistenceProvider;

	protected Node valueFxNode;
	protected Node nameFxNode;

	protected ObjectProperty<T> currentValueProperty;
	protected ObjectProperty<String> currentValueStoredStringProperty;
	protected ObjectProperty<String> currentValueDisplayedStringProperty;

	/**
	 *
	 * @param elementTitle
	 * @param elementId
	 * @param valueType
	 * @param defaultValue
	 * @param persistenceProvider may be null
	 */
	public DataElement (String elementTitle, String elementId, Class<T> valueType, T defaultValue, DataElementPersistenceProvider persistenceProvider) {
		Objects.requireNonNull (elementTitle, "elementTitle");
		Objects.requireNonNull (elementId, "elementId");
		Objects.requireNonNull (valueType, "valueType");

		this.elementTitle = elementTitle;
		this.elementId = elementId;
		this.valueType = valueType;
		this.defaultValue = defaultValue;
		this.persistenceProvider = persistenceProvider;

		this.currentValueProperty = new SimpleObjectProperty<> (this.defaultValue);

		this.currentValueStoredStringProperty = new SimpleObjectProperty<> ();
		Bindings.bindBidirectional (currentValueStoredStringProperty, currentValueProperty, new StringConverter<T> () {
			@Override
			public String toString (T object) {
				return valueToStoredString (object);
			}

			@Override
			public T fromString (String string) {
				return storedStringToValue (string);
			}
		});

		this.currentValueDisplayedStringProperty = new SimpleObjectProperty<> ();
		Bindings.bindBidirectional (currentValueDisplayedStringProperty, currentValueProperty, new StringConverter<T> () {
			@Override
			public String toString (T object) {
				return valueToDisplayedString (object);
			}

			@Override
			public T fromString (String string) {
				return displayedStringToValue (string);
			}
		});
	}

	/**
	 * Prepare this data element value for saving with the persistence provider.
	 * Must be currentState-independent
	 *
	 * @param val
	 * @return
	 */
	public abstract String valueToStoredString (T val);

	/**
	 * Read this data element's value from the format of the persistence
	 * provider. Must be current
	 *
	 * @param str
	 * @return
	 */
	public abstract T storedStringToValue (String str);

	/**
	 * Prepare this data element value for displaying to the user
	 *
	 * @param val
	 * @return
	 */
	protected String valueToDisplayedString (T val) {
		return valueToStoredString (val);
	}

	/**
	 * Read this data element's value from the user input
	 *
	 * @param str
	 * @return
	 */
	protected T displayedStringToValue (String str) {
		return storedStringToValue (str);
	}

	/**
	 * Get the property that keeps the current value of this data element
	 *
	 * @return
	 */
	public ObjectProperty<T> getCurrentValueProperty () {
		return currentValueProperty;
	}

	/**
	 * Get the property that keeps the current value of this data element in the
	 * form for the persistence provider
	 *
	 * @return
	 */
	public ObjectProperty<String> getCurrentValueStoredStringProperty () {
		return currentValueStoredStringProperty;
	}

	/**
	 * Get the property that keeps the current value of this data element in the
	 * form to be displayed
	 *
	 * @return
	 */
	public ObjectProperty<String> getCurrentValueDisplayedStringProperty () {
		return currentValueDisplayedStringProperty;
	}

	/**
	 * Get the JavaFX node used to read or even edit the data element's value
	 *
	 * @return
	 */
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			TextField textField = new TextField ();
			textField.textProperty ().bindBidirectional (currentValueDisplayedStringProperty);
			valueFxNode = textField;
		}
		return valueFxNode;
	}

	/**
	 * Get the JavaFX node used to show the name of this data element
	 *
	 * @return
	 */
	public Node getNameFxNode () {
		if (nameFxNode == null) {
			Label label = new Label (elementTitle);
			nameFxNode = label;
		}
		return nameFxNode;
	}

	public DataElementPersistenceProvider getPersistenceProvider () {
		return persistenceProvider;
	}

	public void setPersistenceProvider (DataElementPersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
	}
}
