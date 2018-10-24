/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.display;

import javafx.scene.Node;
import javafx.scene.control.Label;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author dmerkushov
 * @param <DE> Data element type
 */
public interface DataElementDisplayer<DE extends DataElement> {

	default Node getValueEdit (DE dataElement) {
		return getValueView (dataElement);
	}

	default Node getValueView (DE dataElement) {
		Label label = new Label ();
		label.textProperty ().bind (dataElement.getCurrentValueDisplayedStringProperty ());

		return label;
	}

	default Node getTitle (DE dataElement) {
		return new Label (dataElement.getDisplayName ());
	}

}
