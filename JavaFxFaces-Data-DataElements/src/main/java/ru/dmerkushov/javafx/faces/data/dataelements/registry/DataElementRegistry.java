/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.panel.DataElementPagePanel;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DataElementRegistry {

	////////////////////////////////////////////////////////////////////////////
	// DataElementRegistry is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static DataElementRegistry _instance;

	/**
	 * Get the single instance of DataElementRegistry
	 *
	 * @return The same instance of DataElementRegistry every time the method is
	 * called
	 */
	public static synchronized DataElementRegistry getInstance () {
		if (_instance == null) {
			_instance = new DataElementRegistry ();
		}
		return _instance;
	}

	private DataElementRegistry () {
	}
	////////////////////////////////////////////////////////////////////////////

	private final Map<String, DataElement> dataElements = new HashMap<> ();
	private final Map<UUID, List<DataElement>> pageDataElements = new HashMap<> ();
	private final Map<DataElement, List<UUID>> dataElementPages = new HashMap<> ();

	public void registerDataElement (DataElement dataElement, UUID... panelUuids) {
		Objects.requireNonNull (dataElement, "dataElement");

		synchronized (dataElements) {
			if (dataElements.containsKey (dataElement.elementId)) {
				throw new IllegalStateException ("A data element with id " + dataElement.elementId + " is already registered");
			}

			DataElementPersistenceProvider persistenceProvider = dataElement.getPersistenceProvider ();
			if (persistenceProvider != null) {
				dataElement.getCurrentValueProperty ().set (dataElement.storedStringToValue (persistenceProvider.load (dataElement)));
			}
			dataElements.put (dataElement.elementId, dataElement);

			if (panelUuids == null) {
				dataElementPages.put (dataElement, new ArrayList<> (0));
			} else {
				dataElementPages.put (dataElement, Arrays.asList (panelUuids));
				for (UUID panel : panelUuids) {
					if (!pageDataElements.containsKey (panel)) {
						pageDataElements.put (panel, new ArrayList<> ());
					}
					pageDataElements.get (panel).add (dataElement);
				}
			}
		}
	}

	public List<DataElement> getDataElementsForPage (DataElementPagePanel panel) {
		return pageDataElements.get (panel.getPanelInstanceUuid ());
	}

	public DataElement getDataElement (String elementId) {
		Objects.requireNonNull (elementId, "elementId");

		DataElement dataElement;
		synchronized (dataElements) {
			dataElement = dataElements.get (elementId);
		}

		return dataElement;
	}

	public void save () {
		Collection<DataElement> dataElementColl;
		synchronized (dataElements) {
			dataElementColl = dataElements.values ();
		}
		dataElementColl.stream ().filter ((de) -> {
			return de.getPersistenceProvider () != null;
		}).forEach ((de) -> {
			de.getPersistenceProvider ().save (de);
		});
	}

}
