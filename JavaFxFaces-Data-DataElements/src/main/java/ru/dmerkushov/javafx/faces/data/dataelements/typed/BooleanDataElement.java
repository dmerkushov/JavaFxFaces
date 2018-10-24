/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class BooleanDataElement extends DataElement<Boolean> {

	public BooleanDataElement (String elementTitle, String elementId, Boolean defaultValue) {
		super (elementTitle, elementId, Boolean.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	private ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<Boolean> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (Boolean.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<Boolean> {

		public ValueProperty (Class<Boolean> valueClass) {
			super (valueClass);
		}

		@Override
		public Boolean jsonToValue (JsonObject json) {
			return json.getOrDefault ("value", JsonValue.NULL).equals (JsonValue.TRUE);
		}

		@Override
		public JsonObject valueToJson (Boolean value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			job.add ("value", value == null ? JsonValue.FALSE : (value ? JsonValue.TRUE : JsonValue.FALSE));
			return job.build ();
		}

		@Override
		public String valueToDisplayedString (Boolean value) {
			if (value == null) {
				return java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_NULL");
			}

			return value
					? java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_YES")
					: java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_NO");
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<BooleanDataElement, Boolean> {

		public JsonSerializer () {
			super (BooleanDataElement.class, Boolean.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<BooleanDataElement> {

		////////////////////////////////////////////////////////////////////////////
		// BooleanDataElementDisplayer is a singleton class
		////////////////////////////////////////////////////////////////////////////
		private static Displayer _instance;

		/**
		 * Get the single instance of BooleanDataElementDisplayer
		 *
		 * @return The same instance of BooleanDataElementDisplayer every time
		 * the method is called
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

		private Map<BooleanDataElement, Node> titleNodes = new HashMap<> ();
		private Map<BooleanDataElement, Node> valueViewNodes = new HashMap<> ();
		private Map<BooleanDataElement, Node> valueEditNodes = new HashMap<> ();

		@Override
		public Node getValueEdit (BooleanDataElement dataElement) {
			CheckBox checkBox = new CheckBox ();
			checkBox.selectedProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getValueProperty ());
			return checkBox;
		}
	}
}
