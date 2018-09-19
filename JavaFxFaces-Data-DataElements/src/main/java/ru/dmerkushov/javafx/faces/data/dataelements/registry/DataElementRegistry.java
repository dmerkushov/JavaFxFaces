/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
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

	private final Map<String, DataElement> dataElements = new LinkedHashMap<> ();
	private final Map<UUID, ArrayList<DataElement>> pageDataElements = new LinkedHashMap<> ();
	private final Map<DataElement, UUID> dataElementPages = new LinkedHashMap<> ();

	public void registerDataElement (DataElement dataElement) {
		registerDataElement (dataElement, null);
	}

	public void registerDataElement (DataElement dataElement, UUID panelUuid) {
		Objects.requireNonNull (dataElement, "dataElement");

		synchronized (dataElements) {
			if (dataElements.containsKey (dataElement.elementId)) {
				throw new IllegalStateException ("A data element with id " + dataElement.elementId + " is already registered");
			}

			DataElementPersistenceProvider persistenceProvider = dataElement.getPersistenceProvider ();
			if (persistenceProvider != null) {
				String persistedStr = persistenceProvider.load (dataElement);
				Object persistedVal = dataElement.storedStringToValue (persistedStr);
				dataElement.getCurrentValueProperty ().updateValue (persistedVal);
			}
			dataElements.put (dataElement.elementId, dataElement);

			dataElementPages.put (dataElement, panelUuid);

			if (panelUuid != null) {
				if (!pageDataElements.containsKey (panelUuid)) {
					pageDataElements.put (panelUuid, new ArrayList<> ());
				}
				pageDataElements.get (panelUuid).add (dataElement);
			}
		}
	}

	public ArrayList<DataElement> getDataElementsForPage (DataElementPagePanel panel) {
		Objects.requireNonNull (panel, "panel");

		return getDataElementsForPage (panel.getPanelInstanceUuid ());
	}

	public ArrayList<DataElement> getDataElementsForPage (UUID panelUuid) {
		Objects.requireNonNull (panelUuid, "panelUuid");

		return pageDataElements.get (panelUuid);
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
