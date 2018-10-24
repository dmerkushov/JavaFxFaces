/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.util.Arrays;
import java.util.Collection;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.BooleanDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.DateTimeDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.DoubleDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.DurationDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.IntegerDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.IntegerRangeDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.LongDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.StringDataElement;
import ru.dmerkushov.javafx.faces.data.json.JsonModule;

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
		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (BooleanDataElement.class, new BooleanDataElement.JsonSerializer ());

		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (DoubleDataElement.class, new DoubleDataElement.JsonSerializer ());
		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (IntegerDataElement.class, new IntegerDataElement.JsonSerializer ());
		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (LongDataElement.class, new LongDataElement.JsonSerializer ());

		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (StringDataElement.class, new StringDataElement.JsonSerializer ());

		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (DateTimeDataElement.class, new DateTimeDataElement.JsonSerializer ());
		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (DurationDataElement.class, new DurationDataElement.JsonSerializer ());

		DataElementJsonSerializerRegistry.getInstance ().registerSerializer (IntegerRangeDataElement.class, new IntegerRangeDataElement.JsonSerializer ());

		//JSON serializers for EnumDataElement and ListDataElement won't be registered here, since the final class of the data elements is not known at this phase.
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return Arrays.asList (JsonModule.class);
	}

	@Override
	public void finish () throws FacesException {
	}

}
