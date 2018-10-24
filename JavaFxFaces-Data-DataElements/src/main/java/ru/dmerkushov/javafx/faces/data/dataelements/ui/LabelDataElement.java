/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.ui;

import java.util.Objects;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class LabelDataElement extends UiDataElement<String> {

	public LabelDataElement (String displayName, Property<String> labelTextProperty) {
		super (displayName, String.class);

		Objects.requireNonNull (labelTextProperty, "labelTextProperty");

		getCurrentValueProperty ().getValueProperty ().bind (labelTextProperty);
	}

	public LabelDataElement (String displayName, String labelText) {
		this (displayName, new SimpleStringProperty (labelText));
	}

	private ValueProperty currentValueProperty;

	public ValueProperty getCurrentValueProperty () {
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<String> {

		public ValueProperty () {
			super (String.class);	// Since String is final
		}

		@Override
		public JsonObject valueToJson (String value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			job.add ("value", value);
			return job.build ();
		}

		@Override
		public String jsonToValue (JsonObject json) {
			return json.getString ("value", null);
		}

		@Override
		public String valueToDisplayedString (String value) {
			return Objects.toString (value);
		}

	}

}
