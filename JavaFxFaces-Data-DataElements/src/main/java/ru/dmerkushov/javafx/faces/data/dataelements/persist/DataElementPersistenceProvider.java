/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <DE> Data element type to which this persistence provider is
 * specialized
 */
public interface DataElementPersistenceProvider<T extends Object, DEVP extends DataElementValueProperty<T>> {

	public void load (String elementId, DEVP valueProperty);

	public void save (String elementId, DEVP valueProperty);

}
