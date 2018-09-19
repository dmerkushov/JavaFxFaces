/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 * @param <DE> the class of the data element
 * @param <DET> the class of the data wrapped by the data element
 */
public class DataElementJsonSerializerImpl<DE extends DataElement<DET>, DET> implements DataElementJsonSerializer<DE> {

	/**
	 * Constructor of the data element class that will be used by this
	 * serializer
	 */
	public final Constructor<DE> dataElementConstructor;

	/**
	 * Parameter names for the constructor of the data element class. May be any
	 * not-null combination of the following strings indicating the order of the
	 * parameters in the constructor of the data element:
	 * <ul>
	 * <li><code>elementTitle</code>
	 * <li><code>elementId</code>
	 * <li><code>valueType</code>
	 * <li><code>defaultValue</code>
	 * <li><code>persistenceProvider</code>
	 * </ul>
	 */
	public final String[] constructorParameterNames;

	/**
	 * Parameter classes for the constructor of the data element class.
	 */
	public final Class[] constructorParameterClasses;

	/**
	 * Data element value's class
	 */
	public final Class<DET> dataElementValueType;

	/**
	 * Mock data element used to produce default values for the created data
	 * elements
	 */
	public final DE mockDataElement;	// Needed to produce default values

	/**
	 *
	 * @param dataElementClass
	 * @param dataElementValueType
	 * @param constructorParameterNames Parameter names for the constructor of
	 * the data element class. May be any not-null combination of the following
	 * strings indicating the order of the parameters in the constructor of the
	 * data element:
	 * <ul>
	 * <li><code>elementTitle</code>
	 * <li><code>elementId</code>
	 * <li><code>valueType</code>
	 * <li><code>defaultValue</code>
	 * <li><code>persistenceProvider</code>
	 * </ul>
	 */
	public DataElementJsonSerializerImpl (Class<DE> dataElementClass, Class<DET> dataElementValueType, String[] constructorParameterNames) {
		Objects.requireNonNull (dataElementClass, "Data element class");
		Objects.requireNonNull (dataElementValueType, "Data element value class");
		Objects.requireNonNull (constructorParameterNames, "constructorParameterNames");

		this.constructorParameterNames = constructorParameterNames;
		this.dataElementValueType = dataElementValueType;

		constructorParameterClasses = new Class[constructorParameterNames.length];

		Object[] mockDataElementConstructorParameters = new Object[constructorParameterNames.length];

		for (int i = 0; i < constructorParameterNames.length; i++) {
			switch (constructorParameterNames[i]) {
				case "elementTitle":
					constructorParameterClasses[i] = String.class;
					mockDataElementConstructorParameters[i] = "";
					break;
				case "elementId":
					constructorParameterClasses[i] = String.class;
					mockDataElementConstructorParameters[i] = "";
					break;
				case "valueType":
					constructorParameterClasses[i] = Class.class;
					mockDataElementConstructorParameters[i] = dataElementValueType;
					break;
				case "defaultValue":
					constructorParameterClasses[i] = dataElementValueType;
					mockDataElementConstructorParameters[i] = null;
					break;
				case "persistenceProvider":
					constructorParameterClasses[i] = DataElementPersistenceProvider.class;

					mockDataElementConstructorParameters[i] = new DataElementPersistenceProvider () {
						@Override
						public String load (DataElement dataElement) {
							return "";
						}

						@Override
						public void save (DataElement dataElement) {
							return;
						}
					};
					break;
				default:
					throw new DataElementSerializerException ("Unknown constructor parameter: " + constructorParameterNames[i]);
			}
		}

		try {
			dataElementConstructor = dataElementClass.getConstructor (constructorParameterClasses);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new DataElementSerializerException (ex);
		}

		try {
			mockDataElement = dataElementConstructor.newInstance (mockDataElementConstructorParameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new DataElementSerializerException (ex);
		}

	}

	@Override
	public final JsonObjectBuilder serialize (DE dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		JsonObjectBuilder job = Json.createObjectBuilder ();
		job.add ("defaultValue", valueToString (dataElement.defaultValue));
		job.add ("value", valueToString (dataElement.getCurrentValueProperty ().getValue ()));
		job.add ("valueType", dataElement.getCurrentValueProperty ().getValue ().getClass ().getCanonicalName ());
		job.add ("elementId", dataElement.elementId);
		job.add ("elementTitle", dataElement.elementTitle);
		return job;
	}

	@Override
	public final DE deserialize (JsonObject json, DataElementPersistenceProvider persistenceProvider) {
		Objects.requireNonNull (json, "json");
//		Objects.requireNonNull (persistenceProvider, "persistenceProvider");

		Class[] constructorParameterClasses = new Class[constructorParameterNames.length];
		Object[] constructorParameters = new Object[constructorParameterNames.length];

		int defValueIndex = -1;

		for (int i = 0; i < constructorParameterNames.length; i++) {
			switch (constructorParameterNames[i]) {
				case "elementTitle":
					constructorParameters[i] = json.getString ("elementTitle", "");
					break;
				case "elementId":
					constructorParameters[i] = json.getString ("elementId", "");
					break;
				case "valueType":
					try {
						constructorParameters[i] = Class.forName (json.getString ("valueType", "java.lang.Object"));
					} catch (ClassNotFoundException ex) {
						throw new DataElementSerializerException (ex);
					}
					constructorParameterClasses[i] = Class.class;
					break;
				case "defaultValue":
					defValueIndex = i;
					break;
				case "persistenceProvider":
					constructorParameters[i] = persistenceProvider;
					break;
				default:
					throw new DataElementSerializerException ("Unknown constructor parameter: " + constructorParameterNames[i]);
			}
		}

		constructorParameters[defValueIndex] = mockDataElement.storedStringToValue (json.getString ("defaultValue"));

		DE de;
		try {
			de = dataElementConstructor.newInstance (constructorParameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new DataElementSerializerException (ex);
		}

		DET value = stringToValue (json.getString ("value"));
		de.getCurrentValueProperty ().updateValue (value);

		return de;
	}

	/**
	 * By default, this method points to the {@link DataElement#valueToStoredString(java.lang.Object)
	 * } method of the {@link #mockDataElement mock data element}, but it may be
	 * overridden for the needs of the serializer. Note that it must be paired
	 * with {@link #stringToValue(java.lang.String) }
	 *
	 * @param val
	 * @return
	 */
	public String valueToString (DET val) {
		return mockDataElement.valueToStoredString (val);
	}

	/**
	 * By default, this method points to the {@link DataElement#storedStringToValue(java.lang.String)
	 * } method of the {@link #mockDataElement mock data element}, but it may be
	 * overridden for the needs of the serializer. Note that it must be paired
	 * with {@link #valueToString(java.lang.String) }
	 *
	 * @param str
	 * @return
	 */
	public DET stringToValue (String str) {
		return mockDataElement.storedStringToValue (str);
	}

}
