/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.Objects;
import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.beans.property.Property;
import javafx.scene.Node;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.IntegerRangeDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.StringDataElement;
import ru.dmerkushov.javafx.faces.data.range.IntegerRange;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.javafxfaces.data.dataelements.table.TableData.RowPattern;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author dmerkushov
 */
public class TableDataElement extends DataElement<TableData> {

	private TableDataElementView view = null;
	private TableData data;

	public TableDataElement (String elementTitle, String elementId, TableData defaultData, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, TableData.class, defaultData, persistenceProvider);

		Objects.requireNonNull (defaultData, "defaultData");

		this.data = defaultData;
	}

	@Override
	public String valueToStoredString (TableData val) {
		return val.toStoredString ();
	}

	@Override
	public TableData storedStringToValue (String str) {
		try {
			return TableData.fromStoredString (str);
		} catch (Exception ex) {
			throw new RuntimeException (ex);
		}
	}

	@Override
	public Node getValueFxNode () {
		if (view == null) {
			view = new TableDataElementView (this);
		}

		return view;
	}

	@Override
	public Property<TableData> getCurrentValueProperty () {
		return data.getTableDataProperty ();
	}

	/**
	 * main() is for testing purposes
	 *
	 * @deprecated
	 * @param args
	 * @throws Exception
	 */
	public static void main (String[] args) throws Exception {

		final UUID mainPanelUuid = UUID.fromString ("c4399529-a2a7-4b8a-bb99-c0b47171fb3c");

		TableDataElement tde = new TableDataElement (
				"titole",
				"iid",
				new TableData (new RowPattern (new String[]{"Column 1", "Column 2"}, new Class[]{StringDataElement.class, IntegerRangeDataElement.class})),
				null) {
			@Override
			public UUID getPanelInstanceUuid () {
				return mainPanelUuid;
			}
		};

		TableData tableData = tde.getCurrentValueProperty ().getValue ();

		StringDataElement de11 = new StringDataElement ("title", "id", null);
		de11.getCurrentValueProperty ().setValue ("hallo 11");

		IntegerRangeDataElement de12 = new IntegerRangeDataElement ("title", "id", new IntegerRange (3, 4, true), null);

		StringDataElement de21 = new StringDataElement ("title", "id", null);
		de21.getCurrentValueProperty ().setValue ("hallo 22");

		IntegerRangeDataElement de22 = new IntegerRangeDataElement ("title", "id", new IntegerRange (5, 6, true), null);

		tableData.getRows ().add (tableData.newRow (de11, de12));
		tableData.getRows ().add (tableData.newRow (de21, de22));

		FacesPanels.getInstance ().registerPanel (tde);

		DataElementRegistry.getInstance ().registerDataElement (tde, null);

		String envName = "tableDataElementTest";

		Preferences systemPrefs = PrefConf.getInstance ().getSystemConfigurationForEnvironment (FacesConfiguration.class, envName);
		systemPrefs.put ("loggingLevel", "ALL");
		systemPrefs.put ("moduleList", TableDataElementModule.class.getCanonicalName ());
		systemPrefs.put ("mainPanelUuid", mainPanelUuid.toString ());

		FacesMain.main (new String[]{"-e", envName});
	}

}
