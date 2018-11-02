/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
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
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementJsonValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.registry.DataElementRegistry;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DurationDataElement extends DataElement<Duration> {

	public DurationDataElement (String elementTitle, String elementId, Duration defaultValue) {
		super (
				elementTitle,
				elementId,
				Duration.class,
				defaultValue != null ? defaultValue : Duration.ZERO
		);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, new Displayer ());
	}

	public DurationDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, Duration.ZERO);
	}

	private JsonValueProperty currentValueProperty;

	@Override
	public JsonValueProperty jsonValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new JsonValueProperty ();
		}
		return currentValueProperty;
	}

	public static class JsonValueProperty extends DataElementJsonValueProperty<Duration> {

		public JsonValueProperty () {
			super (Duration.class);
			this.valueProperty ().set (Duration.ZERO);

			valueProperty ().addListener (new ChangeListener<Duration> () {
				@Override
				public void changed (ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
					alignPropertyValues ();
				}
			});
		}

		private SimpleLongProperty daysProperty;

		public ReadOnlyLongProperty daysProperty () {
			if (daysProperty == null) {
				daysProperty = new SimpleLongProperty ();
				daysProperty.set (valueProperty ().get ().getSeconds () / 3600 / 24);
			}
			return daysProperty;
		}

		private SimpleIntegerProperty hoursOfDayProperty;

		public ReadOnlyIntegerProperty hoursOfDayProperty () {
			if (hoursOfDayProperty == null) {
				hoursOfDayProperty = new SimpleIntegerProperty ();
				hoursOfDayProperty.set ((int) (valueProperty ().get ().getSeconds () / 3600 % 24));
			}
			return hoursOfDayProperty;
		}

		private SimpleIntegerProperty minutesOfHourProperty;

		public ReadOnlyIntegerProperty minutesOfHourProperty () {
			if (minutesOfHourProperty == null) {
				minutesOfHourProperty = new SimpleIntegerProperty ();
				minutesOfHourProperty.set ((int) (valueProperty ().get ().getSeconds () / 60 % 60));
			}
			return minutesOfHourProperty;
		}

		private SimpleIntegerProperty secondsOfMinuteProperty;

		public ReadOnlyIntegerProperty secondsOfMinuteProperty () {
			if (secondsOfMinuteProperty == null) {
				secondsOfMinuteProperty = new SimpleIntegerProperty ();
				javafx.scene.control.TextInputControl r;
				secondsOfMinuteProperty.set ((int) (valueProperty ().get ().getSeconds () % 60));
			}
			return secondsOfMinuteProperty;
		}

		public void setDays (long days) {
			valueProperty ().set (valueProperty ().get ().minusDays (daysProperty ().get ()).plusDays (days));
		}

		public void setHoursOfDay (int hours) {
			valueProperty ().set (valueProperty ().get ().minusHours (hoursOfDayProperty ().get ()).plusHours (hours));
		}

		public void setMinutesOfHour (int minutes) {
			valueProperty ().set (valueProperty ().get ().minusMinutes (minutesOfHourProperty ().get ()).plusMinutes (minutes));
		}

		public void setSecondsOfMinute (int seconds) {
			valueProperty ().set (valueProperty ().get ().minusSeconds (secondsOfMinuteProperty ().get ()).plusSeconds (seconds));
		}

		private void alignPropertyValues () {
			Duration val = valueProperty ().get ();

			long days = val.getSeconds () / 3600 / 24;
			int hours = (int) (val.getSeconds () / 3600 % 24);
			int minutes = (int) (val.getSeconds () / 60 % 60);
			int seconds = (int) (val.getSeconds () % 60);

			daysProperty ();
			hoursOfDayProperty ();
			minutesOfHourProperty ();
			secondsOfMinuteProperty ();
			daysProperty.set (days);
			hoursOfDayProperty.set (hours);
			minutesOfHourProperty.set (minutes);
			secondsOfMinuteProperty.set (seconds);
		}

		@Override
		public JsonObject valueToJson (Duration value) {
			if (value == null) {
				return Json.createObjectBuilder ().build ();
			}

			int secondsOfMinute = (int) (value.getSeconds () % 60);
			int minutesOfHour = (int) (value.getSeconds () / 60 % 60);
			int hoursOfDay = (int) (value.getSeconds () / 3600 % 24);
			long days = value.getSeconds () / 3600 / 24;

			JsonObjectBuilder job = Json.createObjectBuilder ();

			job.add ("secondsOfMinute", secondsOfMinute);
			job.add ("minutesOfHour", minutesOfHour);
			job.add ("hoursOfDay", hoursOfDay);
			job.add ("days", days);

			return job.build ();
		}

		@Override
		public Duration jsonToValue (JsonObject json) {
			if (json == null) {
				return Duration.ZERO;
			}

			int secondsOfMinute = json.getInt ("secondsOfMinute", 0);
			int minutesOfHour = json.getInt ("minutesOfHour", 0);
			int hoursOfDay = json.getInt ("hoursOfDay", 0);
			long days;
			try {
				days = json.getJsonNumber ("days").longValue ();
			} catch (NullPointerException ex) {
				days = 0L;
			}

			Duration d = Duration.ZERO.plusDays (days).plusHours (hoursOfDay).plusMinutes (minutesOfHour).plusSeconds (secondsOfMinute);
			return d;
		}

		@Override
		public String valueToDisplayedString (Duration value) {
			return value.toString ();
		}

	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DurationDataElement, Duration> {

		public JsonSerializer () {
			super (DurationDataElement.class, Duration.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<DurationDataElement> {

		@Override
		public Node getValueEdit (DurationDataElement dataElement) {
			TextField daysField;
			TextField hoursField;
			TextField minutesField;

			daysField = new TextField ();
			dataElement.jsonValueProperty ().daysProperty ().addListener (new ChangeListener<Number> () {
				@Override
				public void changed (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					daysField.textProperty ().set (Objects.toString (newValue));
				}
			});
			daysField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Long n;
					try {
						n = Long.parseLong (newValue.replaceAll ("[^0-9]", ""));
					} catch (NumberFormatException ex) {
						n = 0L;
					}

					dataElement.jsonValueProperty ().setDays (n);
				}
			});

			hoursField = new TextField ();
			dataElement.jsonValueProperty ().hoursOfDayProperty ().addListener (new ChangeListener<Number> () {
				@Override
				public void changed (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					hoursField.textProperty ().set (Objects.toString (newValue));
				}
			});
			hoursField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Integer n;
					try {
						n = Integer.parseInt (newValue.replaceAll ("[^0-9]", ""));
					} catch (NumberFormatException ex) {
						n = 0;
					}

					dataElement.jsonValueProperty ().setHoursOfDay (n);
				}
			});

			minutesField = new TextField ();
			dataElement.jsonValueProperty ().minutesOfHourProperty ().addListener (new ChangeListener<Number> () {
				@Override
				public void changed (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					minutesField.textProperty ().set (Objects.toString (newValue));
				}
			});
			minutesField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Integer n;
					try {
						n = Integer.parseInt (newValue.replaceAll ("[^0-9]", ""));
					} catch (NumberFormatException ex) {
						n = 0;
					}

					dataElement.jsonValueProperty ().setMinutesOfHour (n);
				}
			});

			HBox hb = new HBox ();

			hb.getChildren ().add (daysField);
			Label fieldLabelDays = new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_DAYS"));
			hb.getChildren ().add (fieldLabelDays);

			hb.getChildren ().add (hoursField);
			Label fieldLabelHours = new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_HOURS"));
			hb.getChildren ().add (fieldLabelHours);

			hb.getChildren ().add (minutesField);
			Label fieldLabelMinutes = new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_MINUTES"));
			hb.getChildren ().add (fieldLabelMinutes);

			daysField.getStyleClass ().add ("input-durationpart");
			daysField.getStyleClass ().add ("input-durationpart-days");
			fieldLabelDays.getStyleClass ().add ("input-durationpart-label");
			fieldLabelDays.getStyleClass ().add ("input-durationpart-days-label");

			hoursField.getStyleClass ().add ("input-durationpart");
			hoursField.getStyleClass ().add ("input-durationpart-hours");
			fieldLabelHours.getStyleClass ().add ("input-durationpart-label");
			fieldLabelHours.getStyleClass ().add ("input-durationpart-hours-label");

			minutesField.getStyleClass ().add ("input-durationpart");
			minutesField.getStyleClass ().add ("input-durationpart-minutes");
			fieldLabelHours.getStyleClass ().add ("input-durationpart-label");
			fieldLabelHours.getStyleClass ().add ("input-durationpart-minutes-label");

			hb.getStyleClass ().add ("input-duration");
			hb.getStyleClass ().add (dataElement.elementId);

			return hb;
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	public static void main (String[] args) throws Exception {
		final UUID mainPanelUuid = UUID.fromString ("50bca6cd-cbed-4af9-9d3d-881bafca45d8");

		final DurationDataElement lde = new DurationDataElement ("titole", "iid");
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
					System.out.println ("Will be saved as: " + lde.jsonValueProperty ().toJsonString ());
					System.out.println ("Displayed string val: " + lde.displayedStringProperty ().get ());
				}
			});
		});

		Thread t = new Thread (new Runnable () {
			@Override
			public void run () {
				DataElement sde = DataElementRegistry.getInstance ().getDataElement ("iid");

				while (true) {
					System.out.println (sde.obtainValue ());
					try {
						Thread.sleep (1000L);
					} catch (InterruptedException ex) {
						Logger.getLogger (StringDataElement.class.getName ()).log (Level.SEVERE, null, ex);
					}
				}
			}
		});
		t.setDaemon (true);
		t.start ();

		FacesMain.main (new String[]{"-e", envName});
	}
}
