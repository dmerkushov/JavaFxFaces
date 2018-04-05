/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.util.Collection;
import java.util.HashSet;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DataElementsModule extends FacesModule {

	public DataElementsModule (Preferences userModulePreferences, Preferences systemModulePreferences) {
		super (userModulePreferences, systemModulePreferences);
	}

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return new HashSet<> ();
	}

	@Override
	public void finish () throws FacesException {
		DataElementRegistry.getInstance ().save ();
	}

}
