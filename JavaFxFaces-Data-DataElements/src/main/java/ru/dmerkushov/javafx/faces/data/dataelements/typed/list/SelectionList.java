/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

import java.util.Arrays;
import java.util.Collection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

/**
 *
 * @author dmerkushov
 */
public class SelectionList<LDEI extends ListDataElementItem> extends SimpleListProperty<LDEI> {

	private SimpleObjectProperty<LDEI> selectionProperty;

	private SimpleIntegerProperty selectedIndexProperty;

	public SelectionList () {
		super (FXCollections.observableArrayList ());

		selectedIndexProperty = new SimpleIntegerProperty (0);
		selectionProperty = new SimpleObjectProperty<> ();

		selectedIndexProperty.addListener (new ChangeListener<Number> () {
			@Override
			public void changed (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue == null) {
					throw new NullPointerException ("newValue for selectedIndexProperty");
				}
				if (oldValue.equals (newValue)) {
					return;
				}
				if (newValue == null) {
					return;
				}
				if (SelectionList.this.size () <= newValue.intValue ()) {
					selectedIndexProperty.setValue (oldValue);
				}
				selectionProperty.setValue (SelectionList.this.get (newValue.intValue ()));
			}
		});

		selectionProperty.addListener ((ObservableValue<? extends LDEI> observable, LDEI oldValue, LDEI newValue) -> {
			if (newValue == null) {
				throw new NullPointerException ("newValue for selectionProperty");
			}
			if (oldValue != null && oldValue.equals (newValue)) {
				return;
			}
			if (!SelectionList.this.contains (newValue)) {
				SelectionList.this.add (newValue);
			}
			selectedIndexProperty.setValue (SelectionList.this.indexOf (newValue));
		});
	}

	public SelectionList (LDEI selection) {
		this ();

		selectionProperty.setValue (selection);
	}

	public SelectionList (int selectedIndex, Iterable<LDEI> items) {
		this ();

		items.forEach ((LDEI item) -> {
			this.add (item);
		});
		this.setSelectedIndex (selectedIndex);
	}

	public SelectionList (Iterable<LDEI> items) {
		this (0, items);
	}

	public SelectionList (int selectedIndex, LDEI... items) {
		this (selectedIndex, Arrays.asList (items));
	}

	public SelectionList (LDEI... items) {
		this (0, items);
	}

	public SimpleObjectProperty<LDEI> getSelectionProperty () {
		return selectionProperty;
	}

	public SimpleIntegerProperty getSelectedIndexProperty () {
		return selectedIndexProperty;
	}

	public void setSelection (LDEI newSelection) {
		System.out.println ("Set selection: " + newSelection);

		getSelectionProperty ().set (newSelection);
	}

	public LDEI getSelection () {
		return getSelectionProperty ().get ();
	}

	public void setSelectedIndex (int newSelected) {
		getSelectedIndexProperty ().set (newSelected);
	}

	public int getSelectedIndex () {
		return getSelectedIndexProperty ().get ();
	}

	@Override
	public boolean add (LDEI element) {
		if (element == null) {
			throw new NullPointerException ("element");
		}

		return super.add (element);
	}

	@Override
	public boolean addAll (LDEI... elements) {
		if (elements == null) {
			throw new NullPointerException ("elements");
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				throw new NullPointerException ("elements array contains null value at index " + i);
			}
		}

		return super.addAll (elements);
	}

	@Override
	public void add (int i, LDEI element) {
		if (element == null) {
			throw new NullPointerException ("element");
		}

		super.add (i, element);
	}

	@Override
	public boolean addAll (int i, Collection<? extends LDEI> elements) {
		if (elements == null) {
			throw new NullPointerException ("elements");
		}
		if (elements.contains (null)) {
			throw new NullPointerException ("elements collection contains null values");
		}

		return super.addAll (i, elements);
	}

	@Override
	public boolean addAll (Collection<? extends LDEI> elements) {
		if (elements == null) {
			throw new NullPointerException ("elements");
		}
		if (elements.contains (null)) {
			throw new NullPointerException ("elements collection contains null values");
		}

		return super.addAll (elements);
	}
}
