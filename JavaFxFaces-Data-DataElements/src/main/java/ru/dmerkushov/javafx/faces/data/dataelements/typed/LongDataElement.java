/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class LongDataElement extends DataElement<Long> {

	public LongDataElement (String elementTitle, String elementId, Long defaultValue) {
		super (elementTitle, elementId, Long.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	public LongDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, 0L);
	}

	ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<Long> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (Long.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<Long> {

		public ValueProperty (Class<Long> valueClass) {
			super (valueClass);
		}

		@Override
		public Long jsonToValue (JsonObject json) {
			Objects.requireNonNull (json, "json");

			if (!json.containsKey ("value")) {
				return 0L;
			}

			try {
				return json.getJsonNumber ("value").longValue ();
			} catch (ClassCastException ex) {
				return 0L;
			}
		}

		@Override
		public JsonObject valueToJson (Long value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			job.add ("value", value);
			return job.build ();
		}

		@Override
		public String valueToDisplayedString (Long value) {
			return Objects.toString (value, "0");
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<LongDataElement, Long> {

		public JsonSerializer () {
			super (LongDataElement.class, Long.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<LongDataElement> {

		////////////////////////////////////////////////////////////////////////////
		// Displayer is a singleton class
		////////////////////////////////////////////////////////////////////////////
		private static Displayer _instance;

		/**
		 * Get the single instance of Displayer
		 *
		 * @return The same instance of Displayer every time the method is
		 * called
		 */
		public static synchronized Displayer getInstance () {
			if (_instance == null) {
				_instance = new Displayer ();
			}
			return _instance;
		}

		private Displayer () {
		}
		////////////////////////////////////////////////////////////////////////////

		@Override
		public Node getValueEdit (LongDataElement dataElement) {
			TextField tf = new TextField ();
			tf.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getValueProperty (), new StringConverter<Long> () {
				@Override
				public String toString (Long object) {
					return Objects.toString (object, "0");
				}

				@Override
				public Long fromString (String string) {
					if (string == null) {
						return 0L;
					}
					String formattedString = string.replaceAll ("[^0123456789\\-]", "");
					if (formattedString.length () == 0) {
						return 0L;
					}
					return Long.parseLong (formattedString);
				}
			});
			return tf;
		}
	}
}
