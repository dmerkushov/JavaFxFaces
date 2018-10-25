/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import javax.json.JsonObject;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <DE> Data element type to which this persistence provider is
 * specialized
 */
public interface DataElementPersistenceProvider {

	/**
	 * Save the JSON representation of a data element. May throw a
	 * {@link DataElementPersistenceProviderException}
	 *
	 * @param elementId
	 * @param json
	 */
	public void save (String elementId, JsonObject json);

	/**
	 * Load a JSON representation of a data element. May throw a
	 * {@link DataElementPersistenceProviderException}. May not return null
	 *
	 * @param elementId
	 * @return
	 */
	public JsonObject load (String elementId);

}
