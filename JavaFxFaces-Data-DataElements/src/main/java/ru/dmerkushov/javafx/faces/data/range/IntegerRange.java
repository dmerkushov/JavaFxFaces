/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.range;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class IntegerRange extends Range<Integer> {

	public IntegerRange (Integer first, Integer second, boolean minIsBest) {
		super (first, second, minIsBest);
	}

	@Override
	public String toString () {
		return "IntegerRange {" + first + "," + second + ",minIsBest:" + minIsBest + "}";
	}

}
