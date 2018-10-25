/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.persist;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author dmerkushov
 */
public class DataElementPersistenceProviderRegistry {

	////////////////////////////////////////////////////////////////////////////
	// DataElementPersistenceProviderRegistry is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static DataElementPersistenceProviderRegistry _instance;

	/**
	 * Get the single instance of DataElementPersistenceProviderRegistry
	 *
	 * @return The same instance of DataElementPersistenceProviderRegistry every
	 * time the method is called
	 */
	public static synchronized DataElementPersistenceProviderRegistry getInstance () {
		if (_instance == null) {
			_instance = new DataElementPersistenceProviderRegistry ();
		}
		return _instance;
	}

	private DataElementPersistenceProviderRegistry () {
	}
	////////////////////////////////////////////////////////////////////////////

	private Map<String, DataElementPersistenceProvider> providers = new HashMap<> ();

	/**
	 * Register a persistence provider for a data element
	 *
	 * @param elementId
	 * @param provider
	 */
	public void registerPersistenceProvider (String elementId, DataElementPersistenceProvider provider) {
		Objects.requireNonNull (elementId, "elementId");
		Objects.requireNonNull (provider, "provider");

		providers.put (elementId, provider);
	}

	/**
	 * Get the persistence provider for a data element pointed by its elementId
	 *
	 * @param elementId
	 * @param strict throw exception if a persistence provider is not registered
	 * for the given elementId
	 * @return
	 * @throws DataElementPersistenceProviderException when persistence provider
	 * is not registered for the elementId
	 */
	public DataElementPersistenceProvider getPersistenceProvider (String elementId, boolean strict) {
		Objects.requireNonNull (elementId, "elementId");

		if (!providers.containsKey (elementId) && strict) {
			throw new DataElementPersistenceProviderException ("No persistence provider registered for elementId=" + elementId);
		}

		return providers.get (elementId);
	}

	public DataElementPersistenceProvider getPersistenceProvider (String elementId) {
		return getPersistenceProvider (elementId, false);
	}

}
