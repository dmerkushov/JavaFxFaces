/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.Arrays;
import java.util.Collection;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerRegistry;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementModule extends FacesModule {

	public TableDataElementModule (Preferences userModulePreferences, Preferences systemModulePreferences) {
		super (userModulePreferences, systemModulePreferences);
	}

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (TableDataElement.class, new TableDataElementJsonSerializer ());
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return Arrays.asList (new Class[]{DataElementsModule.class});
	}

	@Override
	public void finish () throws FacesException {
		// Do nothing
	}

}
