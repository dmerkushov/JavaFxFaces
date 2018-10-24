/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import javax.json.JsonNumber;

/**
 *
 * @author dmerkushov
 */
public class FacesJsonNumber implements JsonNumber {

	private final BigDecimal val;

	public FacesJsonNumber (BigDecimal val) {
		this.val = val;
	}

	public FacesJsonNumber (BigInteger val) {
		this.val = new BigDecimal (val);
	}

	public FacesJsonNumber (long val) {
		this.val = new BigDecimal (val);
	}

	public FacesJsonNumber (double val) {
		this.val = new BigDecimal (val);
	}

	@Override
	public boolean isIntegral () {
		return (val.scale () <= 0);
	}

	@Override
	public int intValue () {
		return val.intValue ();
	}

	@Override
	public int intValueExact () {
		return val.intValueExact ();
	}

	@Override
	public long longValue () {
		return val.longValue ();
	}

	@Override
	public long longValueExact () {
		return val.longValueExact ();
	}

	@Override
	public BigInteger bigIntegerValue () {
		return val.toBigInteger ();
	}

	@Override
	public BigInteger bigIntegerValueExact () {
		return val.toBigIntegerExact ();
	}

	@Override
	public double doubleValue () {
		return val.doubleValue ();
	}

	@Override
	public BigDecimal bigDecimalValue () {
		return val;
	}

	@Override
	public ValueType getValueType () {
		return ValueType.NUMBER;
	}

	@Override
	public String toString () {
		return val.toString ();
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
		final FacesJsonNumber other = (FacesJsonNumber) obj;
		if (!Objects.equals (this.val, other.val)) {
			return false;
		}
		return true;
	}

}
