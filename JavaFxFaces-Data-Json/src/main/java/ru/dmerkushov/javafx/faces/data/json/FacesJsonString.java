/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.json;

import java.util.Objects;
import javax.json.JsonString;

/**
 *
 * @author dmerkushov
 */
public class FacesJsonString implements JsonString {

	private final String val;

	public FacesJsonString (String val) {
		this.val = val;
	}

	@Override
	public String getString () {
		return val;
	}

	@Override
	public CharSequence getChars () {
		return val;
	}

	@Override
	public ValueType getValueType () {
		return ValueType.STRING;
	}

	@Override
	public int hashCode () {
		return Objects.hashCode (this.val);
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass () != obj.getClass ()) {
			return false;
		}
		final FacesJsonString other = (FacesJsonString) obj;
		if (!Objects.equals (this.val, other.val)) {
			return false;
		}
		return true;
	}

}
