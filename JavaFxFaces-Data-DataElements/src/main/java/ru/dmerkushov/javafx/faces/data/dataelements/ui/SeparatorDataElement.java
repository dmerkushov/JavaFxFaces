/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.ui;

import javafx.scene.Node;
import javafx.scene.control.Separator;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class SeparatorDataElement extends UiDataElement<Object> {

	public SeparatorDataElement (String displayName) {
		super (displayName, Object.class);
	}

	public static class Displayer implements DataElementDisplayer<SeparatorDataElement> {

		@Override
		public Node getValueEdit (SeparatorDataElement dataElement) {
			return new Separator ();
		}

		@Override
		public Node getValueView (SeparatorDataElement dataElement) {
			return new Separator ();
		}

	}

}
