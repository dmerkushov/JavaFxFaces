/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class NopPersistenceProvider implements DataElementPersistenceProvider {

	@Override
	public void save (String elementId, JsonObject json) {
		// Do nothing
	}

	@Override
	public JsonObject load (String elementId) {
		JsonObjectBuilder job = Json.createObjectBuilder ();
		return job.build ();
	}

}
