/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.range;

import java.util.Objects;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 * @param <T>
 */
public class Range<T extends Number> {

	public final T first;
	public final T second;
	public final boolean minIsBest;

	public Range (T first, T second, boolean minIsBest) {
		Objects.requireNonNull (first, "first");
		Objects.requireNonNull (second, "second");

		this.first = first;
		this.second = second;
		this.minIsBest = minIsBest;
	}

	public T getMin () {
		double f = first.doubleValue ();
		double s = second.doubleValue ();

		return (f <= s ? first : second);
	}

	public T getMax () {
		double f = first.doubleValue ();
		double s = second.doubleValue ();

		return (f <= s ? second : first);
	}

	public T getBest () {
		return (minIsBest ? getMin () : getMax ());
	}

	public T getWorst () {
		return (minIsBest ? getMax () : getMin ());
	}

	public boolean inRange (T val) {
		double v = val.doubleValue ();
		return (v >= getMin ().doubleValue () && v <= getMax ().doubleValue ());
	}

}
