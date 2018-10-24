/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.ui;

import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public abstract class UiDataElement<T> extends DataElement<T> {

	public UiDataElement (String displayName, Class<T> clazz) {
		super (displayName, UUID.randomUUID ().toString (), clazz, null);
	}

	private DataElementValueProperty<T> cvp;

	@Override
	public DataElementValueProperty<T> getCurrentValueProperty () {
		if (cvp == null) {
			cvp = new DataElementValueProperty<T> (valueType) {
				@Override
				public JsonObject valueToJson (T value) {
					return Json.createObjectBuilder ().build ();
				}

				@Override
				public T jsonToValue (JsonObject json) {
					return null;
				}

				@Override
				public String valueToDisplayedString (Object value) {
					return "";
				}
			};
		}
		return cvp;
	}

}
