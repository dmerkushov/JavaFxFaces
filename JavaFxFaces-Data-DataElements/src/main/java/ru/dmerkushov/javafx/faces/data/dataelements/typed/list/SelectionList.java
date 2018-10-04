/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

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
public class SelectionList<T extends ListDataElementItem> extends SimpleListProperty<T> {

	private SimpleObjectProperty<T> selectionProperty;

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

		selectionProperty.addListener ((ObservableValue<? extends T> observable1, T oldValue, T newValue) -> {
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

	public SelectionList (T selection) {
		this ();

		selectionProperty.setValue (selection);
	}

	public SimpleObjectProperty<T> getSelectionProperty () {
		return selectionProperty;
	}

	public SimpleIntegerProperty getSelectedIndexProperty () {
		return selectedIndexProperty;
	}

	public void setSelection (T newSelection) {
		System.out.println ("Set selection: " + newSelection);

		getSelectionProperty ().setValue (newSelection);
	}

	public T getSelection () {
		return getSelectionProperty ().getValue ();
	}

	public void setSelectedIndex (int newSelected) {
		getSelectedIndexProperty ().setValue (newSelected);
	}

	public int getSelectedIndex () {
		return getSelectedIndexProperty ().getValue ();
	}

	@Override
	public boolean add (T element) {
		if (element == null) {
			throw new NullPointerException ("element");
		}

		return super.add (element);
	}

	@Override
	public boolean addAll (T... elements) {
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
	public void add (int i, T element) {
		if (element == null) {
			throw new NullPointerException ("element");
		}

		super.add (i, element);
	}

	@Override
	public boolean addAll (int i, Collection<? extends T> elements) {
		if (elements == null) {
			throw new NullPointerException ("elements");
		}
		if (elements.contains (null)) {
			throw new NullPointerException ("elements collection contains null values");
		}

		return super.addAll (i, elements);
	}

	@Override
	public boolean addAll (Collection<? extends T> elements) {
		if (elements == null) {
			throw new NullPointerException ("elements");
		}
		if (elements.contains (null)) {
			throw new NullPointerException ("elements collection contains null values");
		}

		return super.addAll (elements);
	}
}
