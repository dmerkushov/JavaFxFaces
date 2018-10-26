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
public class IntegerDataElement extends DataElement<Integer> {

	public IntegerDataElement (String elementTitle, String elementId, Integer defaultValue) {
		super (elementTitle, elementId, Integer.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	public IntegerDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, 0);
	}

	ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<Integer> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (Integer.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<Integer> {

		public ValueProperty (Class<Integer> valueClass) {
			super (valueClass);
		}

		@Override
		public Integer jsonToValue (JsonObject json) {
			if (json == null) {
				return 0;
			}

			return json.getInt ("value", 0);
		}

		@Override
		public JsonObject valueToJson (Integer value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			if (value == null) {
				job.addNull ("value");
			} else {
				job.add ("value", value);
			}
			return job.build ();
		}

		@Override
		public String valueToDisplayedString (Integer value) {
			return Objects.toString (value, "0");
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<IntegerDataElement, Integer> {

		public JsonSerializer () {
			super (IntegerDataElement.class, Integer.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<IntegerDataElement> {

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
		public Node getValueEdit (IntegerDataElement dataElement) {
			TextField tf = new TextField ();
			tf.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getValueProperty (), new StringConverter<Integer> () {
				@Override
				public String toString (Integer object) {
					return Objects.toString (object, "0");
				}

				@Override
				public Integer fromString (String string) {
					if (string == null) {
						return 0;
					}
					String formattedString = string.replaceAll ("[^0123456789\\-]", "");
					if (formattedString.length () == 0) {
						return 0;
					}
					return Integer.parseInt (formattedString);
				}
			});
			return tf;
		}
	}
}
