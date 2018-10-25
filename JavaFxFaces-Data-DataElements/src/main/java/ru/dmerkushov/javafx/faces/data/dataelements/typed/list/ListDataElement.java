/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed.list;

import java.util.Objects;
import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.JsonObject;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
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

	/**
	 * Create a ListDataElement instance
	 *
	 * @param elementTitle
	 * @param elementId
	 * @param defaultValue
	 * @param dropDownList is drop-down list?
	 */
	public ListDataElement (String elementTitle, String elementId, SelectionList<LI> defaultValue, boolean dropDownList) {
		super (
				elementTitle,
				elementId,
				SelectionList.class,
				defaultValue
		);

		this.dropDownList = dropDownList;

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, new Displayer ());

		getCurrentValueProperty ().getValueProperty ().getValue ().addListener (new ListChangeListener () {
			@Override
			public void onChanged (ListChangeListener.Change c) {
				getCurrentValueProperty ().addAll (getCurrentValueProperty ().valueToJson (getCurrentValueProperty ().getValueProperty ().getValue ()));
			}
		});
		getCurrentValueProperty ().getValueProperty ().getValue ().getSelectionProperty ().addListener (new ChangeListener () {
			@Override
			public void changed (ObservableValue observable, Object oldValue, Object newValue) {
				getCurrentValueProperty ().addAll (getCurrentValueProperty ().valueToJson (getCurrentValueProperty ().getValueProperty ().getValue ()));
			}
		});
	}

	/**
	 * Create a ListDataElement instance, with drop-down list mode on
	 *
	 * @param elementTitle
	 * @param elementId
	 * @param defaultValue
	 */
	public ListDataElement (String elementTitle, String elementId, SelectionList<LI> defaultValue) {
		this (
				elementTitle,
				elementId,
				defaultValue,
				true
		);
	}

	private ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<SelectionList> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty ();
		}
		return currentValueProperty;
	}

	public class ValueProperty extends DataElementValueProperty<SelectionList> {

		public ValueProperty () {
			super (valueType);
		}

		@Override
		public JsonObject valueToJson (SelectionList value) {
			return SelectionList.toJson (value);
		}

		@Override
		public SelectionList jsonToValue (JsonObject json) {
			return SelectionList.fromJson (json);
		}

		@Override
		public String valueToDisplayedString (SelectionList value) {
			return Objects.toString (value.getSelection ());
		}
	}

	public class Displayer implements DataElementDisplayer<ListDataElement<LI>> {

		@Override
		public Node getValueEdit (ListDataElement<LI> dataElement) {
			if (dropDownList) {
				return getValueEditDropdownList ();
			} else {
				return getValueEditListView ();
			}
		}

		private Node getValueEditDropdownList () {
			ComboBox<LI> comboBox = new ComboBox<> ();
			comboBox.setItems (getCurrentValueProperty ().getValueProperty ().get ());
			comboBox.getSelectionModel ().select ((LI) getCurrentValueProperty ().getValueProperty ().get ().getSelection ());
			comboBox.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener () {
				@Override
				public void changed (ObservableValue observable, Object oldValue, Object newValue) {
					if (oldValue == newValue) {
						return;
					}
					getCurrentValueProperty ().getValueProperty ().get ().setSelection (comboBox.getSelectionModel ().getSelectedItem ());
				}
			});
			getCurrentValueProperty ().getValueProperty ().get ().getSelectionProperty ().addListener (new ChangeListener () {
				@Override
				public void changed (ObservableValue observable, Object oldValue, Object newValue) {
					if (oldValue == newValue) {
						return;
					}
					comboBox.getSelectionModel ().select ((LI) newValue);
				}
			});
			return comboBox;
		}

		private Node getValueEditListView () {
			ListView<LI> listView = new ListView<> ();
			listView.setItems (getCurrentValueProperty ().getValueProperty ().get ());
			listView.getSelectionModel ().select ((LI) getCurrentValueProperty ().getValueProperty ().get ().getSelection ());
			listView.getSelectionModel ().selectedItemProperty ().addListener (new ChangeListener () {
				@Override
				public void changed (ObservableValue observable, Object oldValue, Object newValue) {
					if (oldValue == newValue) {
						return;
					}
					getCurrentValueProperty ().getValueProperty ().get ().setSelection (listView.getSelectionModel ().getSelectedItem ());
				}
			});
			getCurrentValueProperty ().getValueProperty ().get ().getSelectionProperty ().addListener (new ChangeListener () {
				@Override
				public void changed (ObservableValue observable, Object oldValue, Object newValue) {
					if (oldValue == newValue) {
						return;
					}
					listView.getSelectionModel ().select ((LI) newValue);
				}
			});
			return listView;
		}

		@Override
		public Node getValueView (ListDataElement<LI> dataElement) {
			throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public Node getTitle (ListDataElement<LI> dataElement) {
			throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}

	private UUID panelInstanceUuid = UUID.randomUUID ();

	@Override
	public UUID getPanelInstanceUuid () {
		return panelInstanceUuid;
	}

	public void setPanelInstanceUuid (UUID panelInstanceUuid) {
		Displayer displayer = (Displayer) DataElementDisplayerRegistry.getInstance ().getDisplayer (this);

		this.panelInstanceUuid = panelInstanceUuid;

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, displayer);
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

		final ListDataElement<StringListDataElementItem> lde = new ListDataElement<> ("titole", "iid", sl, true);
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
					System.out.println ("Will be saved as: " + lde.getCurrentValueProperty ().toJsonString ());
				}
			});
		});

		FacesMain.main (new String[]{"-e", envName});
	}

}
