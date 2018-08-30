/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class BooleanDataElement extends DataElement<Boolean> {

	public BooleanDataElement (String elementTitle, String elementId, Boolean defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, Boolean.class, defaultValue, persistenceProvider);
	}

	@Override
	public String valueToStoredString (Boolean val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public Boolean storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("")) {
			return null;
		}

		return Boolean.valueOf (str);
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			CheckBox checkBox = new CheckBox ();
			checkBox.selectedProperty ().bindBidirectional (getCurrentValueProperty ());
			valueFxNode = checkBox;
		}

		return valueFxNode;
	}

	public Node getValueViewFxNode () {
		if (valueViewFxNode == null) {
			Label label = new Label (
					getCurrentValueProperty ().getValue ()
					? java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_YES")
					: java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_NO")
			);
			getCurrentValueProperty ().addListener (new ChangeListener<Boolean> () {
				@Override
				public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					label.textProperty ().setValue (
							newValue
									? java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_YES")
									: java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("BOOLEANDE_NO")
					);
				}
			});
			valueViewFxNode = label;
		}

		return valueViewFxNode;
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<BooleanDataElement, Boolean> {

		public JsonSerializer () {
			super (BooleanDataElement.class, Boolean.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}
}
