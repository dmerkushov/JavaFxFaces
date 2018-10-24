/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.json;

import java.util.Collection;
import java.util.LinkedList;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;

/**
 *
 * @author dmerkushov
 */
public class JsonModule extends ru.dmerkushov.javafx.faces.FacesModule {

	public JsonModule (Preferences userModulePreferences, Preferences systemModulePreferences) {
		super (userModulePreferences, systemModulePreferences);
	}

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return new LinkedList<> ();
	}

	@Override
	public void finish () throws FacesException {
	}

}
