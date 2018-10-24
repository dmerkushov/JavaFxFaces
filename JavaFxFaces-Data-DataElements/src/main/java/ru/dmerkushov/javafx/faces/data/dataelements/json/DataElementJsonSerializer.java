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
