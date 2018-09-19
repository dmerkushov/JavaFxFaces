/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.io.StringReader;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializer;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.DateTimeDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.IntegerRangeDataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.typed.StringDataElement;
import ru.dmerkushov.javafx.faces.data.range.IntegerRange;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author dmerkushov
 */
public class TableDataElement extends DataElement<TableData> {

	private Node view = null;
	private TableDataElementView tdeView;
	private TableDataProperty currentValueProperty = null;

	public TableDataElement (String elementTitle, String elementId, TableData defaultData, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, TableData.class, defaultData, persistenceProvider);

		Objects.requireNonNull (defaultData, "defaultData");

		currentValueProperty = new TableDataProperty (defaultData);
	}

	@Override
	public String valueToStoredString (TableData val) {
		DataElementJsonSerializer<TableDataElement> jsonSerializer = DataElementJsonSerializerRegistry.getInstance ().getSerializer (TableDataElement.class);

		return jsonSerializer.serialize (this).build ().toString ();
	}

	@Override
	public TableData storedStringToValue (String str) {
		JsonReader rdr = Json.createReader (new StringReader (str));
		JsonObject json = rdr.readObject ();

		DataElementJsonSerializer<TableDataElement> jsonSerializer = DataElementJsonSerializerRegistry.getInstance ().getSerializer (TableDataElement.class);

		return jsonSerializer.deserialize (json, null).getCurrentValueProperty ().getValue ();
	}

	@Override
	public Node getValueFxNode () {
		if (view == null) {
			TableDataElementView tdev = getTableDataElementView ();
			tdev.prefWidthProperty ().set (900.0);
			tdev.minWidthProperty ().set (300.0);
			tdev.prefHeightProperty ().set (300.0);
			tdev.minHeightProperty ().set (150.0);

			VBox vb = new VBox ();
			vb.getChildren ().add (tdev);

			Button addBtn = new Button (java.util.ResourceBundle.getBundle ("ru/dmerkushov/javafxfaces/data/dataelements/table/Bundle").getString ("BTN_ADDROW_CAPTION"));
			addBtn.setOnAction ((ActionEvent event) -> {

				Callable<TableDataRow> dataRowCreator = getCurrentValueProperty ().getValue ().getDataRowCreatorProperty ().get ();

				if (dataRowCreator == null) {
					return;
				}

				TableDataRow tdr;
				try {
					tdr = dataRowCreator.call ();
				} catch (Exception ex) {
					throw new TableDataElementException ("Exception when calling a data row creator", ex);
				}

				if (tdr == null) {
					return;
				}

				getRows ().add (tdr);
			});

			addBtn.visibleProperty ().bind (getCurrentValueProperty ().getValue ().getDataRowCreatorProperty ().isNotNull ());
			addBtn.getStyleClass ().add ("TableDataElement_addBtn");
			addBtn.getStyleClass ().add ("TableDataElement_" + this.elementId + "_addBtn");

			vb.getChildren ().add (addBtn);
			vb.getStyleClass ().add ("TableDataElement_vb");
			vb.getStyleClass ().add ("TableDataElement_" + this.elementId + "_vb");

			view = vb;
		}

		return view;
	}

	public TableDataElementView getTableDataElementView () {
		if (tdeView == null) {
			tdeView = new TableDataElementView (this);
			tdeView.getStyleClass ().add ("TableDataElement_table");
			tdeView.getStyleClass ().add ("TableDataElement_" + this.elementId + "_table");
		}
		return tdeView;
	}

	@Override
	public DataElementValueProperty<TableData> getCurrentValueProperty () {
		return currentValueProperty;
	}

	private UUID panelInstanceUuid = UUID.randomUUID ();

	@Override
	public UUID getPanelInstanceUuid () {
		return panelInstanceUuid;
	}

	public void setPanelInstanceUuid (UUID panelInstanceUuid) {
		this.panelInstanceUuid = panelInstanceUuid;
	}

	public ObservableList<TableDataRow> getRows () {
		return getCurrentValueProperty ().getValue ().getRows ();
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

		final TableDataElement tde = new TableDataElement (
				"titole",
				"iid",
				new TableData (new TableDataRowPattern (new String[]{"Column 1", "Column 2"}, new Class[]{StringDataElement.class, DataElement.class})),
				null);

		tde.setPanelInstanceUuid (mainPanelUuid);

		TableData tableData = tde.getCurrentValueProperty ().getValue ();

		tableData.getDataRowCreatorProperty ().set (new Callable<TableDataRow> () {
			@Override
			public TableDataRow call () throws Exception {
				StringDataElement newSde1 = new StringDataElement ("title", "id", null);
				newSde1.getCurrentValueProperty ().updateValue ("" + Math.random ());

				StringDataElement newSde2 = new StringDataElement ("title", "id", null);
				newSde2.getCurrentValueProperty ().updateValue ("" + Math.random ());

				return tableData.prepareRow (newSde1, newSde2);
			}
		});

		StringDataElement de11 = new StringDataElement ("title", "id", null);
		de11.getCurrentValueProperty ().setValue ("hallo 11");

		IntegerRangeDataElement de12 = new IntegerRangeDataElement ("title", "id", new IntegerRange (3, 4, true), null);

		StringDataElement de21 = new StringDataElement ("title", "id", null);
		de21.getCurrentValueProperty ().setValue ("hallo 22");

		DataElement de22 = new DateTimeDataElement ("title", "id", null);

		tableData.getRows ().add (tableData.prepareRow (de11, de12));
		tableData.getRows ().add (tableData.prepareRow (de21, de22));

		FacesPanels.getInstance ().registerPanel (tde);

		DataElementRegistry.getInstance ().registerDataElement (tde, null);

		String envName = "tableDataElementTest";

		Preferences systemPrefs = PrefConf.getInstance ().getSystemConfigurationForEnvironment (FacesConfiguration.class, envName);
		systemPrefs.put ("loggingLevel", "ALL");
		systemPrefs.put ("moduleList", TableDataElementModule.class.getCanonicalName ());
		systemPrefs.put ("mainPanelUuid", mainPanelUuid.toString ());

		FacesMain.doBeforePrimaryStageShow (() -> {
			Stage primaryStage = FacesMain.getInstanceWhenCreated ().getPrimaryStage ();

			primaryStage.setMinWidth (1024.0);
			primaryStage.setMinHeight (768.0);

			primaryStage.onHidingProperty ().set (new EventHandler<WindowEvent> () {
				@Override
				public void handle (WindowEvent event) {
					System.out.println (tde.getCurrentValueStoredStringProperty ().getValue ());
				}
			});
		});

		FacesMain.main (new String[]{"-e", envName});
	}

}
