/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializer;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementJsonSerializer implements DataElementJsonSerializer<TableDataElement> {

	@Override
	public JsonObjectBuilder serialize (TableDataElement dataElement) {
		//TODO Implement serialize()
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public TableDataElement deserialize (JsonObject json, DataElementPersistenceProvider persistenceProvider) {
		//TODO Implement deserialize()
		throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
