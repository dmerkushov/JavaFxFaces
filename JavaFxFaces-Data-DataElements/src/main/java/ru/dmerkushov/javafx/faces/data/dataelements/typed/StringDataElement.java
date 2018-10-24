/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.util.Objects;
import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.FacesModules;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class StringDataElement extends DataElement<String> {

	public StringDataElement (String elementTitle, String elementId, String defaultValue) {
		super (elementTitle, elementId, String.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	public StringDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, "");
	}

	ValueProperty currentValueProperty;

	@Override
	public DataElementValueProperty<String> getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (String.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<String> {

		public ValueProperty (Class<String> valueClass) {
			super (valueClass);
		}

		@Override
		public String jsonToValue (JsonObject json) {
			Objects.requireNonNull (json, "json");

			if (!json.containsKey ("value")) {
				return "";
			}

			return json.getString ("value", "");
		}

		@Override
		public JsonObject valueToJson (String value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();
			if (value == null) {
				job.addNull ("value");
			} else {
				job.add ("value", value);
			}
			return job.build ();
		}

		@Override
		public String valueToDisplayedString (String value) {
			return Objects.toString (value, "");
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<StringDataElement, String> {

		public JsonSerializer () {
			super (StringDataElement.class, String.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<StringDataElement> {

		////////////////////////////////////////////////////////////////////////////
		// Displayer is a singleton class
		////////////////////////////////////////////////////////////////////////////
		private static Displayer _instance;

		/**
		 * Get the single instance of Displayer
		 *
		 * @return The same instance of Displayer every time the method is
		 * called
		 */
		public static synchronized Displayer getInstance () {
			if (_instance == null) {
				_instance = new Displayer ();
			}
			return _instance;
		}

		private Displayer () {
		}
		////////////////////////////////////////////////////////////////////////////

		@Override
		public Node getValueEdit (StringDataElement dataElement) {
			TextField tf = new TextField ();
			tf.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getValueProperty ());
			return tf;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	public static void main (String[] args) throws Exception {
		final UUID mainPanelUuid = UUID.fromString ("50bca6cd-cbed-4af9-9d3d-881bafca45d8");

		FacesModules.getInstance ().loadModule (DataElementsModule.class);

		String ir = "hallo";

		final StringDataElement lde = new StringDataElement ("titole", "iid", ir);
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
					System.out.println ("Will be saved as: " + DataElementJsonSerializerRegistry.getInstance ().serialize (lde));
					System.out.println ("Displayed string: " + lde.getCurrentValueDisplayedStringProperty ().get ());
				}
			});
		});

		FacesMain.main (new String[]{"-e", envName});
	}
}
