/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.json;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 * @param <T>
 */
public interface DataElementJsonSerializer<T extends DataElement> {

	public JsonObjectBuilder serialize (T dataElement);

	public T deserialize (JsonObject json, DataElementPersistenceProvider persistenceProvider);

}
