/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.list.ListDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.list.ListDataElementItem;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.list.SelectionList;

/**
 * A list of enum constants.
 *
 * The enum type must not be an inner class
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <LI>
 */
public class EnumDataElement<LI extends ListDataElementItem> extends ListDataElement<LI> {

	public EnumDataElement (String elementTitle, String elementId, Enum defaultValue, boolean dropDownList) {
		super (
				elementTitle,
				elementId,
				createSelectionListFromEnum (defaultValue),
				dropDownList
		);
	}

	private static SelectionList createSelectionListFromEnum (Enum defaultValue) {
		SelectionList sl = new SelectionList ();

		try {
			Class<Enum> enumClass = (Class<Enum>) defaultValue.getClass ();

			Method namesMethod = enumClass.getMethod ("values");

			Object[] names = (Object[]) namesMethod.invoke (null);

			for (Object value : names) {
				EnumListDataElementItem eldei = new EnumListDataElementItem ((Enum) value);
				sl.add (eldei);
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | RuntimeException ex) {
			throw new DataElementException (ex);
		}

		return sl;
	}

	public static class EnumListDataElementItem extends ListDataElementItem<Enum> {

		public EnumListDataElementItem (Enum contained) {
			super (containedToStoredStringStatic (contained));
		}

		@Override
		protected Enum containedFromStoredString (String str) {
			try {
				JsonReader rdr = Json.createReader (new StringReader (str));
				JsonObject json = rdr.readObject ();
				Class containedClass = Class.forName (json.getString ("containedClass"));
				Method byName = containedClass.getMethod ("valueOf", String.class);

				String containedValueStr = json.getString ("containedValue");

				return (Enum) byName.invoke (null, containedValueStr);
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | RuntimeException ex) {
				throw new DataElementException (ex);
			}
		}

		@Override
		protected String containedToStoredString (Enum contained) {
			return containedToStoredStringStatic (contained);
		}

		private static String containedToStoredStringStatic (Enum contained) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			job.add ("containedClass", contained.getClass ().getCanonicalName ());
			job.add ("containedValue", contained.name ());

			return job.build ().toString ();
		}
	}
}
