/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <LI> list item type
 */
public class ListDataElement<LI extends ListDataElementItem> extends DataElement<SelectionList> {

	private boolean dropDownList;

	public ListDataElement (String elementTitle, String elementId, SelectionList<LI> defaultValue, DataElementPersistenceProvider persistenceProvider, boolean dropDownList) {
		super (
				elementTitle,
				elementId,
				SelectionList.class,
				defaultValue,
				persistenceProvider
		);

		this.dropDownList = dropDownList;
	}

	public ListDataElement (String elementTitle, String elementId, SelectionList<LI> defaultValue, DataElementPersistenceProvider persistenceProvider) {
		this (
				elementTitle,
				elementId,
				defaultValue,
				persistenceProvider,
				true
		);
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			if (dropDownList) {
				ComboBox<LI> comboBox = new ComboBox<> ();
				comboBox.setItems (getCurrentValueProperty ().getValue ());
				comboBox.getSelectionModel ().select ((LI) getCurrentValueProperty ().getValue ().getSelection ());
				comboBox.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener () {
					@Override
					public void changed (ObservableValue observable, Object oldValue, Object newValue) {
						if (oldValue == newValue) {
							return;
						}
						getCurrentValueProperty ().getValue ().setSelection (comboBox.getSelectionModel ().getSelectedItem ());
					}
				});
				getCurrentValueProperty ().getValue ().getSelectionProperty ().addListener (new ChangeListener () {
					@Override
					public void changed (ObservableValue observable, Object oldValue, Object newValue) {
						if (oldValue == newValue) {
							return;
						}
						comboBox.getSelectionModel ().select ((LI) newValue);
					}
				});
				valueFxNode = comboBox;
			} else {
				ListView<LI> listView = new ListView<> ();
				listView.setItems (getCurrentValueProperty ().getValue ());
				listView.getSelectionModel ().select ((LI) getCurrentValueProperty ().getValue ().getSelection ());
				listView.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener () {
					@Override
					public void changed (ObservableValue observable, Object oldValue, Object newValue) {
						if (oldValue == newValue) {
							return;
						}
						getCurrentValueProperty ().getValue ().setSelection (listView.getSelectionModel ().getSelectedItem ());
					}
				});
				getCurrentValueProperty ().getValue ().getSelectionProperty ().addListener (new ChangeListener () {
					@Override
					public void changed (ObservableValue observable, Object oldValue, Object newValue) {
						if (oldValue == newValue) {
							return;
						}
						listView.getSelectionModel ().select ((LI) newValue);
					}
				});
				valueFxNode = listView;
			}
		}

		return valueFxNode;
	}

	@Override
	public String valueToStoredString (SelectionList val) {
		JsonObjectBuilder job = Json.createObjectBuilder ();
		job.add ("selected", val.getSelection ().toString ());
		job.add ("itemClass", val.getSelection ().getClass ().getCanonicalName ());

		JsonArrayBuilder jab = Json.createArrayBuilder ();
		for (Object item : val) {
			jab.add (item.toString ());
		}

		job.add ("items", jab);

		return job.build ().toString ();
	}

	@Override
	public SelectionList storedStringToValue (String str) {
		JsonReader rdr = Json.createReader (new StringReader (str));
		JsonObject json = rdr.readObject ();
		SelectionList<LI> sl = new SelectionList<> ();

		try {
			String className = json.getString ("itemClass", "java.lang.String");
			Class clazz = Class.forName (className);
			Constructor constr = clazz.getConstructor (String.class);

			JsonArray itemsArr = json.getJsonArray ("items");
			for (int i = 0; i < itemsArr.size (); i++) {
				String itemStr = itemsArr.getString (i);
				LI itemLi = (LI) constr.newInstance (itemStr);
				sl.add (itemLi);
			}

			String selectedValueStr = json.getString ("selected");
			LI selectedValueLi = (LI) constr.newInstance (selectedValueStr);
			sl.setSelection (selectedValueLi);
		} catch (ClassNotFoundException
				| NoSuchMethodException
				| SecurityException
				| InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException ex) {
			throw new ListDataElementException (ex);
		}

		return sl;
	}

	private UUID panelInstanceUuid = UUID.randomUUID ();

	@Override
	public UUID getPanelInstanceUuid () {
		return panelInstanceUuid;
	}

	public void setPanelInstanceUuid (UUID panelInstanceUuid) {
		this.panelInstanceUuid = panelInstanceUuid;
	}

	public boolean isDropDownList () {
		return dropDownList;
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	public static void main (String[] args) throws Exception {
		final UUID mainPanelUuid = UUID.fromString ("50bca6cd-cbed-4af9-9d3d-881bafca45d8");

		SelectionList<StringListDataElementItem> sl = new SelectionList<StringListDataElementItem> () {
			{
				add (new StringListDataElementItem ("illa"));
				add (new StringListDataElementItem ("ulla"));

				setSelectedIndex (0);
			}
		};

		final ListDataElement<StringListDataElementItem> lde = new ListDataElement<> ("titole", "iid", sl, new DataElementPersistenceProvider () {
			@Override
			public String load (DataElement dataElement) {
				return "{\"selected\":\"olla\",\"itemClass\":\"ru.dmerkushov.javafx.faces.data.dataelements.typed.list.StringListDataElementItem\",\"items\":[\"0\",\"alla\",\"ella\",\"olla\"]}";
			}

			@Override
			public void save (DataElement dataElement) {
			}
		}, false);
		lde.setPanelInstanceUuid (mainPanelUuid);

		FacesPanels.getInstance ().registerPanel (lde);

		DataElementRegistry.getInstance ().registerDataElement (lde, null);

		String envName = "listDataElementTest";

		Preferences systemPrefs = PrefConf.getInstance ().getSystemConfigurationForEnvironment (FacesConfiguration.class, envName);
		systemPrefs.put ("loggingLevel", "ALL");
		systemPrefs.put ("moduleList", DataElementsModule.class.getCanonicalName ());
		systemPrefs.put ("mainPanelUuid", mainPanelUuid.toString ());

		FacesMain.doBeforePrimaryStageShow (() -> {
			Stage primaryStage = FacesMain.getInstanceWhenCreated ().getPrimaryStage ();

			primaryStage.setMinWidth (1024.0);
			primaryStage.setMinHeight (768.0);

			primaryStage.onHidingProperty ().set (new EventHandler<WindowEvent> () {
				@Override
				public void handle (WindowEvent event) {
					System.out.println (lde.valueToStoredString (lde.getCurrentValueProperty ().getValue ()));
				}
			});
		});

		FacesMain.main (new String[]{"-e", envName});
	}

}
