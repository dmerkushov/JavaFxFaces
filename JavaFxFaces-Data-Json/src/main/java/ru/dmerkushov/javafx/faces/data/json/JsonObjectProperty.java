/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.json;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

/**
 *
 * @author dmerkushov
 */
public class JsonObjectProperty extends SimpleMapProperty<String, JsonValue> implements JsonObjectBuilder, JsonObject {

	private ObservableSet<String> addOrder = FXCollections.observableSet (new LinkedHashSet<> ());

	public JsonObjectProperty () {
	}

	public JsonObjectProperty (Object bean, String name) {
		super (bean, name);
	}

	public JsonObjectProperty (Object bean, String name, ObservableMap<String, JsonValue> initialValue) {
		super (bean, name, initialValue);
	}

	public JsonObjectProperty (ObservableMap<String, JsonValue> initialValue) {
		super (initialValue);
	}

	public JsonObjectProperty (String jsonStr) {
		addAll (jsonStr);
	}

	public JsonObjectProperty (InputStream is) {
		addAll (is);
	}

	public JsonObjectProperty (Reader rdr) {
		addAll (rdr);
	}

	public JsonObjectProperty (Map<String, JsonValue> map) {
		addAll (map);
	}

	public final void addAll (Map<String, JsonValue> map) {
		if (map == null) {
			return;
		}

		map.entrySet ().forEach ((Entry<String, JsonValue> entry) -> {
			add (entry.getKey (), entry.getValue ());
		});
	}

	public final void addAll (String jsonStr) {
		if (jsonStr == null) {
			return;
		}

		JsonReader jrdr = Json.createReader (new StringReader (jsonStr));
		JsonObject json = jrdr.readObject ();
		this.addAll (json);
	}

	public final void addAll (InputStream is) {
		if (is == null) {
			return;
		}

		JsonReader jrdr = Json.createReader (is);
		JsonObject json = jrdr.readObject ();
		addAll (json);
	}

	public final void addAll (Reader rdr) {
		if (rdr == null) {
			return;
		}

		JsonReader jrdr = Json.createReader (rdr);
		JsonObject json = jrdr.readObject ();
		addAll (json);
	}

	@Override
	public final JsonObjectBuilder add (String name, JsonValue value) {
		this.put (name, value);

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, String value) {
		this.put (name, new FacesJsonString (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, BigInteger value) {
		this.put (name, new FacesJsonNumber (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, BigDecimal value) {
		this.put (name, new FacesJsonNumber (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, int value) {
		this.put (name, new FacesJsonNumber (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, long value) {
		this.put (name, new FacesJsonNumber (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, double value) {
		this.put (name, new FacesJsonNumber (value));

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, boolean value) {
		this.put (name, value ? JsonValue.TRUE : JsonValue.FALSE);

		return this;
	}

	@Override
	public final JsonObjectBuilder addNull (String name) {
		this.put (name, JsonValue.NULL);

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, JsonObjectBuilder builder) {
		this.put (name, builder.build ());

		return this;
	}

	@Override
	public final JsonObjectBuilder add (String name, JsonArrayBuilder builder) {
		this.put (name, builder.build ());

		return this;
	}

	@Override
	public void putAll (Map<? extends String, ? extends JsonValue> elements) {
		super.putAll (elements);

		getAddOrder ().addAll (elements.keySet ());
	}

	@Override
	public JsonValue put (String key, JsonValue value) {
		JsonValue result = super.put (key, value);

		getAddOrder ().add (key);

		return result;
	}

	@Override
	public JsonValue remove (Object key) {
		JsonValue result = super.remove (key);

		getAddOrder ().remove (key);

		return result;
	}

	@Override
	public boolean remove (Object key, Object value) {
		boolean result = super.remove (key, value);

		if (result) {
			getAddOrder ().remove (key);
		}

		return result;
	}

	/**
	 * Returns the JSON object associated with this object builder. The
	 * iteration order for the {@code JsonObject} is based on the order in which
	 * name/value pairs are added to the object using this builder.
	 *
	 * @return an immutable copy of this object
	 */
	@Override
	public final JsonObject build () {
		JsonObjectBuilder job = Json.createObjectBuilder ();

		getAddOrder ().forEach ((String key) -> {
			if (containsKey (key)) {
				job.add (key, get (key));
			}
		});

		return job.build ();
	}

	@Override
	public final JsonArray getJsonArray (String name) {
		return (JsonArray) get (name);
	}

	@Override
	public final JsonObject getJsonObject (String name) {
		return (JsonObject) get (name);
	}

	@Override
	public final JsonNumber getJsonNumber (String name) {
		return (JsonNumber) get (name);
	}

	@Override
	public final JsonString getJsonString (String name) {
		return (JsonString) get (name);
	}

	@Override
	public final String getString (String name) {
		return ((JsonString) get (name)).getString ();
	}

	@Override
	public final String getString (String name, String defaultValue) {
		try {
			return getString (name);
		} catch (NullPointerException | ClassCastException ex) {
			return defaultValue;
		}
	}

	@Override
	public final int getInt (String name) {
		return ((JsonNumber) get (name)).intValue ();
	}

	@Override
	public final int getInt (String name, int defaultValue) {
		try {
			return getInt (name);
		} catch (NullPointerException | ClassCastException ex) {
			return defaultValue;
		}
	}

	@Override
	public final boolean getBoolean (String name) {
		if (!this.containsKey (name)) {
			throw new NullPointerException ("No key " + name + " in the map");
		}
		JsonValue val = get (name);
		if (val != JsonValue.TRUE && val != JsonValue.FALSE) {
			throw new ClassCastException ("Key " + name + " does not contain a boolean value");
		}
		return (val == JsonValue.TRUE);
	}

	@Override
	public final boolean getBoolean (String name, boolean defaultValue) {
		if (!this.containsKey (name)) {
			return defaultValue;
		}
		JsonValue val = get (name);
		if (val != JsonValue.TRUE && val != JsonValue.FALSE) {
			return defaultValue;
		}
		return (val == JsonValue.TRUE);
	}

	@Override
	public final boolean isNull (String name) {
		return (get (name) == JsonValue.NULL);
	}

	@Override
	public final ValueType getValueType () {
		return ValueType.OBJECT;
	}

	public final ObservableSet<String> getAddOrder () {
		return addOrder;
	}

	@Override
	public String toString () {
		return toJsonString ();
	}

	public final String toJsonString () {
		StringWriter sw = new StringWriter ();
		JsonWriter jw = Json.createWriter (sw);
		jw.write (this);

		return sw.toString ();
	}

}
