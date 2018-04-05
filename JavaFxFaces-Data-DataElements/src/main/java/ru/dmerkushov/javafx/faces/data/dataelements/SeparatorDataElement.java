/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import javafx.scene.Node;
import javafx.scene.control.Separator;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class SeparatorDataElement extends UiDataElement {

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			valueFxNode = new Separator ();

		}
		return valueFxNode;
	}

}
