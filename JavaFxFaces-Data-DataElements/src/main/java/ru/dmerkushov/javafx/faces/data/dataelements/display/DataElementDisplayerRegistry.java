/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.display;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author dmerkushov
 */
public final class DataElementDisplayerRegistry {

	////////////////////////////////////////////////////////////////////////////
	// DataElementDisplayerRegistry is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static DataElementDisplayerRegistry _instance;

	/**
	 * Get the single instance of DataElementDisplayerRegistry
	 *
	 * @return The same instance of DataElementDisplayerRegistry every time the
	 * method is called
	 */
	public static synchronized DataElementDisplayerRegistry getInstance () {
		if (_instance == null) {
			_instance = new DataElementDisplayerRegistry ();
		}
		return _instance;
	}

	private DataElementDisplayerRegistry () {
		initialize ();
	}
	////////////////////////////////////////////////////////////////////////////

	private Map<Class<DataElement>, DataElementDisplayer> classesToDisplayers;

	private Map<DataElement, DataElementDisplayer> instancesToDisplayers;

	private void initialize () {
		classesToDisplayers = new HashMap<> ();
		instancesToDisplayers = new HashMap<> ();
	}

	public DataElementDisplayer getDisplayer (DataElement de) {
		Objects.requireNonNull (de, "de");

		if (!instancesToDisplayers.containsKey (de)) {
			Class currClass = de.getClass ();
			DataElementDisplayer displayer = null;
			while (!classesToDisplayers.containsKey (currClass) && DataElement.class.isAssignableFrom (currClass)) {
				currClass = currClass.getSuperclass ();
			}

			if (classesToDisplayers.containsKey (currClass)) {
				displayer = classesToDisplayers.get (currClass);
			} else {
				displayer = new DataElementDisplayer () {
				};
			}

			instancesToDisplayers.put (de, displayer);
		}
		return instancesToDisplayers.get (de);
	}

	public void registerDisplayer (DataElement de, DataElementDisplayer ded) {
		Objects.requireNonNull (de, "de");
		Objects.requireNonNull (ded, "ded");

		instancesToDisplayers.put (de, ded);
	}

	public void registerDisplayer (Class<DataElement> dec, DataElementDisplayer ded) {
		Objects.requireNonNull (dec, "dec");
		Objects.requireNonNull (ded, "ded");

		classesToDisplayers.put (dec, ded);
	}
}
