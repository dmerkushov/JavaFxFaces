/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class NopPersistenceProvider<T extends Object, DEVP extends DataElementValueProperty<T>> implements DataElementPersistenceProvider<T, DEVP> {

	public NopPersistenceProvider () {
	}

	@Override
	public void load (String elementId, DEVP valueProperty) {
		// Do nothing
	}

	@Override
	public void save (String elementId, DEVP valueProperty) {
		// Do nothing
	}

}
