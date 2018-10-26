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
import javax.json.stream.JsonParsingException;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

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

	private final Map<Class<? extends DataElement>, DataElementJsonSerializer> serializersByClass = new HashMap<> ();
	private final Map<String, DataElementJsonSerializer> serializersByElementId = new HashMap<> ();

	public <DE extends DataElement> void registerSerializer (Class<DE> clazz, DataElementJsonSerializer<DE> serializer) {
		Objects.requireNonNull (clazz, "clazz");
		Objects.requireNonNull (serializer, "serializer");

		serializersByClass.put (clazz, serializer);
	}

	public <T extends DataElement> void unregisterDataElementClass (Class<T> clazz) {
		Objects.requireNonNull (clazz, "clazz");

		serializersByClass.remove (clazz);
	}

	public <DE extends DataElement> void registerSerializer (String elementId, DataElementJsonSerializer<DE> serializer) {
		Objects.requireNonNull (elementId, "elementId");
		Objects.requireNonNull (serializer, "serializer");

		serializersByElementId.put (elementId, serializer);
	}

	public <DE extends DataElement> void unregisterSerializer (DataElementJsonSerializer<DE> serializer) {
		Objects.requireNonNull (serializer, "serializer");

		Iterator<Entry<Class<? extends DataElement>, DataElementJsonSerializer>> iter = serializersByClass.entrySet ().iterator ();

		while (iter.hasNext ()) {
			Entry<Class<? extends DataElement>, DataElementJsonSerializer> entry = iter.next ();
			if (entry.getValue ().equals (serializer)) {
				iter.remove ();
				break;
			}
		}
	}

	/**
	 * Get serializer by class
	 *
	 * @param <DE>
	 * @param clazz
	 * @return
	 */
	public <DE extends DataElement> DataElementJsonSerializer<DE> getSerializer (Class<DE> clazz) {
		Objects.requireNonNull (clazz, "clazz");

		Class currentClass = clazz;
		while (serializersByClass.get (currentClass) == null && currentClass != Object.class) {
			currentClass = currentClass.getSuperclass ();
		}

		return serializersByClass.get (currentClass);
	}

	public DataElementJsonSerializer getSerializer (String elementId) {
		Objects.requireNonNull (elementId, "elementId");

		return serializersByElementId.get (elementId);
	}

	public DataElement deserialize (JsonObject dataElementJson) throws ClassNotFoundException {
		Objects.requireNonNull (dataElementJson, "dataElementJson");

		String elementId = dataElementJson.getString ("elementId", "");
		DataElementJsonSerializer serializer = getSerializer (elementId);
		if (serializer != null) {
			return serializer.deserialize (dataElementJson);
		}

		String className = dataElementJson.getString ("dataElementClass", "");
		if (className.equals ("")) {
			throw new DataElementSerializerException ("dataElementClass field in JSON is empty");
		}

		Class clazz = Class.forName (className);

		if (!DataElement.class.isAssignableFrom (clazz)) {
			throw new DataElementSerializerException ("data element class does not extend DataElement: " + clazz.getCanonicalName ());
		}

		serializer = getSerializer (clazz);

		if (serializer == null) {
			throw new DataElementSerializerException ("No DataElement serializer registered for class " + clazz);
		}

		return serializer.deserialize (dataElementJson);
	}

	public DataElement deserialize (String dataElementJsonStr) throws ClassNotFoundException {
		Objects.requireNonNull (dataElementJsonStr, "dataElementJsonStr");

		JsonObject json;
		try {
			JsonReader rdr = Json.createReader (new StringReader (dataElementJsonStr));
			json = rdr.readObject ();
		} catch (JsonParsingException ex) {
			throw new DataElementSerializerException ("Exception when parsing json: " + dataElementJsonStr, ex);
		} catch (RuntimeException ex) {
			throw new DataElementSerializerException (ex);
		}
		return DataElementJsonSerializerRegistry.this.deserialize (json);
	}

	public JsonObject serialize (DataElement dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		DataElementJsonSerializer serializer = getSerializer (dataElement.getClass ());

		if (serializer == null) {
			throw new DataElementSerializerException ("No DataElement serializer registered for data element class " + dataElement.getClass ().getCanonicalName ());
		}

		JsonObjectBuilder job = serializer.serialize (dataElement);

		job.add ("dataElementClass", dataElement.getClass ().getCanonicalName ());
		job.add ("elementId", dataElement.elementId);

		return job.build ();
	}

}
