/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.dummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DummyModule extends FacesModule {

	List<FacesPanel> panels = Collections.synchronizedList (new ArrayList<> (2));
	DummyPanel dummyPanel = new DummyPanel ();
	Preferences modulePrefs;

	public DummyModule (Preferences userModulePrefs, Preferences systemModulePrefs) throws FacesException {
		super (userModulePrefs, systemModulePrefs);

		panels.add (dummyPanel);
		panels.add (new DummyDock (dummyPanel));
		this.modulePrefs = userModulePrefs;
	}

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
		dummyPanel.panelLabel.setText (modulePrefs.get ("LABEL_TEXT", "Hallo world"));
	}

	@Override
	public void finish () throws FacesException {
		// Here must be the finalization, but none needed for this dummy module
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return new ArrayList<> (0);
	}

}
