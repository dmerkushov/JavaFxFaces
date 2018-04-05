/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DataElementSet {

	private final LinkedHashSet<DataElement> dataElements = new LinkedHashSet<> ();

	public void add (DataElement dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		synchronized (dataElements) {
			dataElements.add (dataElement);
		}
	}

	public void addAll (Collection<? extends DataElement> dataElements) {
		Objects.requireNonNull (dataElements, "dataElements");

		synchronized (dataElements) {
			this.dataElements.addAll (dataElements);
		}
	}

	public void remove (DataElement dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		synchronized (dataElements) {
			dataElements.remove (dataElement);
		}
	}

	public ArrayList<DataElement> getDataElements () {
		synchronized (dataElements) {
			return new ArrayList<> (dataElements);
		}
	}

	public boolean contains (DataElement dataElement) {
		Objects.requireNonNull (dataElement, "dataElement");

		synchronized (dataElements) {
			return dataElements.contains (dataElement);
		}
	}
}
