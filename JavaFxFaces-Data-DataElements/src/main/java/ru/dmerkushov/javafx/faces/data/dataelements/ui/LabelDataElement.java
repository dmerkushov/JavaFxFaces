/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.ui;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class LabelDataElement extends UiDataElement {

	private final StringProperty labelTextProperty;

	public LabelDataElement (String displayName, StringProperty labelTextProperty) {
		super (displayName);

		Objects.requireNonNull (labelTextProperty, "labelTextProperty");

		this.labelTextProperty = labelTextProperty;
	}

	public LabelDataElement (String displayName, String labelText) {
		this (displayName, new SimpleStringProperty (labelText));
	}

	public StringProperty getLabelTextProperty () {
		return labelTextProperty;
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			Label l = new Label ();
			l.textProperty ().bind (getLabelTextProperty ());
			l.getStyleClass ().add ("LabelDataElement");

			valueFxNode = l;
		}
		return valueFxNode;
	}

	@Override
	public Node getValueViewFxNode () {
		return getValueFxNode ();
	}

}
