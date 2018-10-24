/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javax.json.JsonObject;
import javax.json.JsonValue;
import ru.dmerkushov.javafx.faces.data.json.JsonObjectProperty;

/**
 *
 * @author dmerkushov
 * @param <T>
 */
public abstract class DataElementValueProperty<T> extends JsonObjectProperty {

	private SimpleObjectProperty<T> valueProperty;

	private Class<T> valueClass;

	public DataElementValueProperty (Class<T> valueClass) {
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, T value) {
		getValueProperty ().setValue (value);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, Object bean, String name) {
		super (bean, name);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, Object bean, String name, ObservableMap<String, JsonValue> initialValue) {
		super (bean, name, initialValue);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, ObservableMap<String, JsonValue> initialValue) {
		super (initialValue);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, String jsonStr) {
		super (jsonStr);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, InputStream is) {
		super (is);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, Reader rdr) {
		super (rdr);
		setValueClass (valueClass);
	}

	public DataElementValueProperty (Class<T> valueClass, Map<String, JsonValue> map) {
		super (map);
		setValueClass (valueClass);
	}

	private void setValueClass (Class<T> valueClass) {
		Objects.requireNonNull (valueClass, "valueClass");

		this.valueClass = valueClass;
		this.add ("valueClass", valueClass.getCanonicalName ());
	}

	public Class<T> getValueClass () {
		return valueClass;
	}

	public final SimpleObjectProperty<T> getValueProperty () {
		if (valueProperty == null) {
			valueProperty = new SimpleObjectProperty<> ();

			valueProperty.addListener (new ChangeListener<T> () {
				@Override
				public void changed (ObservableValue<? extends T> observable, T oldValue, T newValue) {
					if (newValue != null && newValue.equals (oldValue)) {
						return;
					}
					DataElementValueProperty.this.putAll (valueToJson (newValue));
				}
			});
			this.addListener (new ChangeListener () {
				@Override
				public void changed (ObservableValue observable, Object oldValue, Object newValue) {
					if (newValue != null && newValue.equals (oldValue)) {
						return;
					}
					valueProperty.set (jsonToValue (DataElementValueProperty.this));
				}
			});
		}
		return valueProperty;
	}

	public abstract JsonObject valueToJson (T value);

	public abstract T jsonToValue (JsonObject json);

	public abstract String valueToDisplayedString (T value);

	public final String currentValueToDisplayedString () {
		return valueToDisplayedString (getValueProperty ().get ());
	}

	public final JsonObject currentValueToJson () {
		return valueToJson (getValueProperty ().get ());
	}

	public void updateValue (T newValue) {
		getValueProperty ().set (newValue);
	}

	public T acquireValue () {
		return getValueProperty ().get ();
	}

}
