/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.serializers;

import javax.json.JsonObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementUniversalSerializer;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.LongDataElement;

/**
 *
 * @author dmerkushov
 */
public class LongDataElementJsonSerializerTest {

	@BeforeClass
	public static void beforeClass () {
		DataElementUniversalSerializer.getInstance ().registerSerializer (LongDataElement.class, new LongDataElement.JsonSerializer ());
	}

	@Test
	public void test () {
		String title = "Title";
		String id = "id";
		Class elementType = LongDataElement.class;
		Class valueType = Long.class;
		Long defaultValue = 1L;
		Long currentValue = 2L;

		DataElementPersistenceProvider pp = new DataElementPersistenceProvider () {
			@Override
			public String load (DataElement dataElement) {
				return "1";
			}

			@Override
			public void save (DataElement dataElement) {
			}
		};

		LongDataElement sde = new LongDataElement (title, id, defaultValue, pp);
		sde.getCurrentValueProperty ().set (currentValue);

		System.out.println ("Original - element type: " + sde.getClass ().getCanonicalName ());
		System.out.println ("Original - title: " + sde.elementTitle);
		System.out.println ("Original - id: " + sde.elementId);
		System.out.println ("Original - value type: " + sde.valueType.getCanonicalName ());
		System.out.println ("Original - default value: " + sde.defaultValue);
		System.out.println ("Original - current value: " + sde.getCurrentValueProperty ().get ());

		JsonObject json = DataElementUniversalSerializer.getInstance ().serialize (sde);

		System.out.println ("JSON: " + json.toString ());

		DataElement dde;
		try {
			dde = DataElementUniversalSerializer.getInstance ().deserialize (json, pp);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace (System.err);
			return;
		}

		Assert.assertEquals (elementType, dde.getClass ());
		Assert.assertEquals (title, dde.elementTitle);
		Assert.assertEquals (id, dde.elementId);
		Assert.assertEquals (valueType, dde.valueType);
		Assert.assertEquals (defaultValue, dde.defaultValue);
		Assert.assertEquals (currentValue, dde.getCurrentValueProperty ().get ());
		Assert.assertEquals (pp, dde.getPersistenceProvider ());

		System.out.println ("Deserialized - element type: " + dde.getClass ().getCanonicalName ());
		System.out.println ("Deserialized - title: " + dde.elementTitle);
		System.out.println ("Deserialized - id: " + dde.elementId);
		System.out.println ("Deserialized - value type: " + dde.valueType.getCanonicalName ());
		System.out.println ("Deserialized - default value: " + dde.defaultValue);
		System.out.println ("Deserialized - current value: " + dde.getCurrentValueProperty ().get ());
	}

}
