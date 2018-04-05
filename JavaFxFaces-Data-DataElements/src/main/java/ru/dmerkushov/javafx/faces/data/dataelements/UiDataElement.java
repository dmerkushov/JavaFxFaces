/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.util.UUID;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public abstract class UiDataElement extends DataElement<Object> {

	public UiDataElement () {
		super ("", UUID.randomUUID ().toString (), Object.class, "", null);
	}

	@Override
	public String valueToStoredString (Object val) {
		return "";
	}

	@Override
	public Object storedStringToValue (String str) {
		return "";
	}

}
