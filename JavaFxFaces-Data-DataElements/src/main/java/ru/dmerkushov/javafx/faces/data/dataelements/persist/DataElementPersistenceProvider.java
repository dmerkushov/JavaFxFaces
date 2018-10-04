/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <T> Data element type to which this persistence provider is
 * specialized
 */
public interface DataElementPersistenceProvider<T extends DataElement> {

	public String load (T dataElement);

	public void save (T dataElement);

}
