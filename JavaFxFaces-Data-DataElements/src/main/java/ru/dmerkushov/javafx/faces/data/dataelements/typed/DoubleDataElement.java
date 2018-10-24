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
public class DoubleDataElement extends DataElement<Double> {

	public DoubleDataElement (String elementTitle, String elementId, Double defaultValue) {
		super (elementTitle, elementId, Double.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	public DoubleDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, 0.0);
	}

	ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<Double> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (Double.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<Double> {

		public ValueProperty (Class<Double> valueClass) {
			super (valueClass);
		}

		@Override
		public Double jsonToValue (JsonObject json) {
			Objects.requireNonNull (json, "json");

			if (!json.containsKey ("value")) {
				return 0.0;
			}

			try {
				return json.getJsonNumber ("value").doubleValue ();
			} catch (ClassCastException ex) {
				return 0.0;
			}
		}

		@Override
		public JsonObject valueToJson (Double value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			job.add ("value", value);
			return job.build ();
		}

		@Override
		public String valueToDisplayedString (Double value) {
			return Objects.toString (value, "0.0");
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DoubleDataElement, Double> {

		public JsonSerializer () {
			super (DoubleDataElement.class, Double.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<DoubleDataElement> {

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
		public Node getValueEdit (DoubleDataElement dataElement) {
			TextField tf = new TextField ();
			tf.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getValueProperty (), new StringConverter<Double> () {
				@Override
				public String toString (Double object) {
					return Objects.toString (object, "0.0");
				}

				@Override
				public Double fromString (String string) {
					if (string == null) {
						return 0.0;
					}
					String formattedString = string.replaceAll ("[^0123456789\\.,\\-]", "").replaceAll (",", ".");
					if (formattedString.length () == 0) {
						return 0.0;
					}
					return Double.parseDouble (formattedString);
				}
			});
			return tf;
		}
	}

}
