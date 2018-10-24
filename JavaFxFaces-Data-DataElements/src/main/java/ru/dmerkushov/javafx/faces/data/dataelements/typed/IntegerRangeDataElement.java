package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.util.UUID;
import java.util.prefs.Preferences;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.FacesMain;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.data.range.IntegerRange;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.prefconf.PrefConf;

public class IntegerRangeDataElement extends DataElement<IntegerRange> {

	public IntegerRangeDataElement (String elementTitle, String elementId, IntegerRange defaultValue) {
		super (
				elementTitle,
				elementId,
				IntegerRange.class,
				(defaultValue != null ? defaultValue : new IntegerRange (0, 0, true))
		);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, new Displayer ());
	}

	private ValueProperty currentValueProperty;

	@Override
	public ValueProperty getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty ();
		}
		return currentValueProperty;
	}

	public class ValueProperty extends DataElementValueProperty<IntegerRange> {

		IntegerProperty minValueProperty;
		IntegerProperty maxValueProperty;
		IntegerProperty worstValueProperty;
		IntegerProperty bestValueProperty;

		public ValueProperty () {
			super (IntegerRange.class);

			getValueProperty ().set (new IntegerRange (0, 0, true));

			minValueProperty = new SimpleIntegerProperty (getValueProperty ().get ().getMin ());
			maxValueProperty = new SimpleIntegerProperty (getValueProperty ().get ().getMax ());
			getValueProperty ().addListener ((ObservableValue<? extends IntegerRange> observable, IntegerRange oldValue, IntegerRange newValue) -> {
				if (newValue.getMin () != minValueProperty.get ()) {
					minValueProperty.set (newValue.getMin ());
				}
				if (newValue.getMax () != maxValueProperty.get ()) {
					maxValueProperty.set (newValue.getMax ());
				}
			});
			minValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = getValueProperty ().get ();
				int currentMax = currentVal.getMax ();
				IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMax, currentVal.minIsBest);
				getValueProperty ().set (newVal);
			});
			maxValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = getValueProperty ().get ();
				int currentMin = currentVal.getMin ();
				IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMin, currentVal.minIsBest);
				getValueProperty ().set (newVal);
			});

			if (getDefaultValue ().minIsBest) {
				bestValueProperty = minValueProperty;
				worstValueProperty = maxValueProperty;
			} else {
				bestValueProperty = maxValueProperty;
				worstValueProperty = minValueProperty;
			}
		}

		public IntegerProperty getMinValueProperty () {
			return minValueProperty;
		}

		public IntegerProperty getMaxValueProperty () {
			return maxValueProperty;
		}

		public IntegerProperty getWorstValueProperty () {
			return worstValueProperty;
		}

		public IntegerProperty getBestValueProperty () {
			return bestValueProperty;
		}

		@Override
		public JsonObject valueToJson (IntegerRange value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();

			job.add ("first", value.first);
			job.add ("second", value.second);
			job.add ("minIsBest", value.minIsBest);

			return job.build ();
		}

		@Override
		public IntegerRange jsonToValue (JsonObject json) {
			int first = json.getInt ("first");
			int second = json.getInt ("second");
			boolean minIsBest = json.getBoolean ("minIsBest");

			return new IntegerRange (first, second, minIsBest);
		}

		@Override
		public String valueToDisplayedString (IntegerRange value) {
			StringBuilder sb = new StringBuilder ();
			if (value.minIsBest) {
				sb.append ("*");
			}
			sb.append (value.getMin ()).append (" - ").append (value.getMax ());
			if (!value.minIsBest) {
				sb.append ("*");
			}
			return sb.toString ();
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<IntegerRangeDataElement, IntegerRange> {

		public JsonSerializer () {
			super (IntegerRangeDataElement.class, IntegerRange.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<IntegerRangeDataElement> {

		@Override
		public Node getValueEdit (IntegerRangeDataElement dataElement) {
			TextField minField = new TextField ();
			dataElement.getCurrentValueProperty ().getMinValueProperty ().addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = dataElement.getCurrentValueProperty ().getValueProperty ().get ();
				int currentMax = currentVal.getMax ();
				IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMax, currentVal.minIsBest);
				minField.textProperty ().set (newVal.getMin ().toString ());
			});
			minField.textProperty ().addListener ((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				String realNewVal = (newValue == null ? "0" : newValue.replaceAll ("[\\D]+", ""));
				if (realNewVal.equals ("")) {
					realNewVal = "0";
				}
				if (!realNewVal.equals (newValue)) {
					minField.textProperty ().set (realNewVal);
				}
				if (!realNewVal.equals (oldValue)) {
					dataElement.getCurrentValueProperty ().getMinValueProperty ().set (Integer.parseInt (realNewVal));
				}
			});
			minField.textProperty ().setValue (dataElement.getCurrentValueProperty ().getValueProperty ().get ().getMin ().toString ());

			TextField maxField = new TextField ();
			dataElement.getCurrentValueProperty ().getMaxValueProperty ().addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = dataElement.getCurrentValueProperty ().getValueProperty ().get ();
				int currentMin = currentVal.getMin ();
				IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMin, currentVal.minIsBest);
				maxField.textProperty ().set (newVal.getMax ().toString ());
			});
			maxField.textProperty ().addListener ((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
				String realNewVal = (newValue == null ? "0" : newValue.replaceAll ("[\\D]+", ""));
				if (realNewVal.equals ("")) {
					realNewVal = "0";
				}
				if (!realNewVal.equals (newValue)) {
					maxField.textProperty ().set (realNewVal);
				}
				if (!realNewVal.equals (oldValue)) {
					dataElement.getCurrentValueProperty ().getMaxValueProperty ().set (Integer.parseInt (realNewVal));
				}
			});
			maxField.textProperty ().setValue (dataElement.getCurrentValueProperty ().getValueProperty ().get ().getMax ().toString ());

			HBox hb = new HBox ();
			hb.getChildren ().add (minField);
			hb.getChildren ().add (new Label ("-"));
			hb.getChildren ().add (maxField);

			hb.getStyleClass ().add ("prop");

			return hb;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	public static void main (String[] args) throws Exception {
		final UUID mainPanelUuid = UUID.fromString ("50bca6cd-cbed-4af9-9d3d-881bafca45d8");

		IntegerRange ir = new IntegerRange (0, 1, true);

		final IntegerRangeDataElement lde = new IntegerRangeDataElement ("titole", "iid", ir);
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
					System.out.println ("Displayed string val: " + lde.getCurrentValueDisplayedStringProperty ().get ());
				}
			});
		});

		FacesMain.main (new String[]{"-e", envName});
	}
}
