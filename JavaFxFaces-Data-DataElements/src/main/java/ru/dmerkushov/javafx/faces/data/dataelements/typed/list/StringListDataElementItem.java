/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

/**
 *
 * @author dmerkushov
 */
public class StringListDataElementItem extends ListDataElementItem<String> {

	public StringListDataElementItem (Object o) {
		super (o.toString ());
	}

	public StringListDataElementItem (String keptStr) {
		super (keptStr);
	}

	@Override
	public String containedFromString (String str) {
		return str;
	}

	@Override
	public String containedToString (String kept) {
		return kept;
	}

}
