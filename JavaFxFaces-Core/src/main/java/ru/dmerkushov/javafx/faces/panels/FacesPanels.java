/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.panels;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public final class FacesPanels {

	////////////////////////////////////////////////////////////////////////////
	// FacesPanels is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static FacesPanels _instance;

	/**
	 * Get the single instance of FacesPanels
	 *
	 * @return The same instance of FacesPanels every time the method is called
	 */
	public static synchronized FacesPanels getInstance () {
		if (_instance == null) {
			_instance = new FacesPanels ();
		}
		return _instance;
	}

	private FacesPanels () {
	}
	////////////////////////////////////////////////////////////////////////////

	private Map<UUID, FacesPanel> panels = new LinkedHashMap<> ();
	private final Object lock = new Object ();

	public void registerPanel (FacesPanel panel) {
		Objects.requireNonNull (panel, "panel");

		synchronized (lock) {
			panels.put (panel.getPanelInstanceUuid (), panel);
		}
	}

	public void unregisterPanel (FacesPanel panel) {
		Objects.requireNonNull (panel, "panel");

		synchronized (lock) {
			panels.remove (panel.getPanelInstanceUuid ());
		}
	}

	public void unregisterPanel (UUID panelInstanceUuid) {
		Objects.requireNonNull (panelInstanceUuid, "panelInstanceUuid");

		synchronized (lock) {
			panels.remove (panelInstanceUuid);
		}
	}

	public FacesPanel getPanel (UUID panelInstanceUuid) {
		Objects.requireNonNull (panelInstanceUuid, "panelInstanceUuid");

		synchronized (lock) {
			return panels.get (panelInstanceUuid);
		}
	}

}
