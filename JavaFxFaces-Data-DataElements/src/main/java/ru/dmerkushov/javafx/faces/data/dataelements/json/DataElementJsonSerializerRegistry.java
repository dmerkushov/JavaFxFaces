/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.json;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 */
public class DataElementJsonSerializerRegistry {

	////////////////////////////////////////////////////////////////////////////
	// DataElementProducer is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static DataElementJsonSerializerRegistry _instance;

	/**
	 * Get the single instance of DataElementProducer
	 *
	 * @return The same instance of DataElementProducer every time the method is
	 * called
	 */
	public static synchronized DataElementJsonSerializerRegistry getInstance () {
		if (_instance == null) {
			_instance = new DataElementJsonSerializerRegistry ();
		}
		return _instance;
	}

	private DataElementJsonSerializerRegistry () {
	}
	////////////////////////////////////////////////////////////////////////////

	private final Map<Class<? extends DataElement>, DataElementJsonSerializer> serializers = new HashMap<> ();

	public <T extends DataElement> void registerSerializer (Class<T> clazz, DataElementJsonSerializer<T> serializer) {
		Objects.requireNonNull (clazz, "clazz");
		Objects.requireNonNull (serializer, "serializer");

		serializers.put (clazz, serializer);
	}

	public <T extends DataElement> void unregisterDataElementClass (Class<T> clazz) {
		Objects.requireNonNull (clazz, "clazz");

		serializers.remove (clazz);
	}

	public <T extends DataElement> void unregisterSerializer (DataElementJsonSerializer<T> serializer) {
		Objects.requireNonNull (serializer, "serializer");

		Iterator<Entry<Class<? extends DataElement>, DataElementJsonSerializer>> iter = serializers.entrySet ().iterator ();

		while (iter.hasNext ()) {
			Entry<Class<? extends DataElement>, DataElementJsonSerializer> entry = iter.next ();
			if (entry.getValue ().equals (serializer)) {
				iter.remove ();
				break;
			}
		}
	}

	public <T extends DataElement> DataElementJsonSerializer<T> getSerializer (Class<T> clazz) {
		Objects.requireNonNull (clazz, "clazz");

		return serializers.get (clazz);
	}

	public DataElement deserialize (JsonObject dataElementJson, DataElementPersistenceProvider persistenceProvider) throws ClassNotFoundException {
		Objects.requireNonNull (dataElementJson, "dataElementJson");
		Objects.requireNonNull (persistenceProvider, "persistenceProvider");

		String className = dataElementJson.getString ("class", "");
		if (className.equals ("")) {
			throw new DataElementSerializerException ("class field in JSON is empty");
		}

		Class clazz = Class.forName (className);

		if (!DataElement.class.isAssignableFrom (clazz)) {
			throw new DataElementSerializerException ("data element class does not extend DataElement: " + clazz.getCanonicalName ());
		}

		DataElementJsonSerializer serializer = getSerializer (clazz);

		if (serializer == null) {
			throw new DataElementSerializerException ("No DataElement serializer registered for class " + clazz);
		}

		return serializer.deserialize (dataElementJson, persistenceProvider);
	}

	public DataElement deserialize (String dataElementJsonStr, DataElementPersistenceProvider persistenceProvider) throws ClassNotFoundException {
		Objects.requireNonNull (dataElementJsonStr, "dataElementJsonStr");
		Objects.requireNonNull (persistenceProvider, "persistenceProvider");

		JsonObject json;
		try {
			JsonReader rdr = Json.createReader (new StringReader (dataElementJsonStr));
			json = rdr.readObject ();
		} catch (RuntimeException ex) {
			throw new DataElementSerializerException (ex);
		}
		return DataElementJsonSerializerRegistry.this.deserialize (json, persistenceProvider);
	}

	public JsonObject serialize (DataElement dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		DataElementJsonSerializer serializer = getSerializer (dataElement.getClass ());

		if (serializer == null) {
			throw new DataElementSerializerException ("No DataElement serializer registered for class " + dataElement.getClass ());
		}

		JsonObjectBuilder job = serializer.serialize (dataElement);

		job.add ("class", dataElement.getClass ().getCanonicalName ());

		return job.build ();
	}

}