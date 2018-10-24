/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.time.Duration;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;

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
	}

	public DurationDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, Duration.ZERO);
	}

	private ValueProperty currentValueProperty;

	@Override
	public ValueProperty getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (Duration.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<Duration> {

		public ValueProperty (Class<Duration> valueClass) {
			super (valueClass);
			this.getValueProperty ().set (Duration.ZERO);
		}

		private LongProperty daysProperty;

		public LongProperty getDaysProperty () {
			if (daysProperty == null) {
				daysProperty = new SimpleLongProperty ();
				daysProperty.set (getValueProperty ().get ().getSeconds () / 3600 / 24);
				getValueProperty ().addListener ((ObservableValue<? extends Duration> observable1, Duration oldValue, Duration newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					daysProperty.set (newValue.getSeconds () / 3600 / 24);
				});
				daysProperty.addListener ((ObservableValue<? extends Number> io, Number oldValue, Number newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Duration current = getValueProperty ().get ();

					long currentDays = current.getSeconds () / 3600 / 24;
					if (Objects.equals (currentDays, newValue)) {
						return;
					}

					Duration newD = current.minusDays (currentDays).plusDays (newValue.longValue ());

					getValueProperty ().set (newD);
				});
			}
			return daysProperty;
		}

		private IntegerProperty hoursOfDayProperty;

		public IntegerProperty getHoursOfDayProperty () {
			if (hoursOfDayProperty == null) {
				hoursOfDayProperty = new SimpleIntegerProperty ();
				hoursOfDayProperty.set ((int) (getValueProperty ().get ().getSeconds () / 3600 % 24));
				getValueProperty ().addListener ((ObservableValue<? extends Duration> observable1, Duration oldValue, Duration newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					hoursOfDayProperty.set ((int) (getValueProperty ().get ().getSeconds () / 3600 % 24));
				});
				hoursOfDayProperty.addListener ((ObservableValue<? extends Number> io, Number oldValue, Number newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Duration current = getValueProperty ().get ();

					int currentHours = (int) (current.getSeconds () / 3600 % 24);
					if (Objects.equals (currentHours, newValue)) {
						return;
					}

					Duration newD = current.minusHours (currentHours).plusHours (newValue.longValue ());

					getValueProperty ().set (newD);
				});
			}
			return hoursOfDayProperty;
		}

		private IntegerProperty minutesOfHourProperty;

		public IntegerProperty getMinutesOfHourProperty () {
			if (minutesOfHourProperty == null) {
				minutesOfHourProperty = new SimpleIntegerProperty ();
				minutesOfHourProperty.set ((int) (getValueProperty ().get ().getSeconds () / 60 % 60));
				getValueProperty ().addListener ((ObservableValue<? extends Duration> observable1, Duration oldValue, Duration newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					minutesOfHourProperty.set ((int) (getValueProperty ().get ().getSeconds () / 60 % 60));
				});
				minutesOfHourProperty.addListener ((ObservableValue<? extends Number> io, Number oldValue, Number newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Duration current = getValueProperty ().get ();

					int currentMinutes = (int) (current.getSeconds () / 60 % 60);
					if (Objects.equals (currentMinutes, newValue)) {
						return;
					}

					Duration newD = current.minusMinutes (currentMinutes).plusMinutes (newValue.longValue ());

					getValueProperty ().set (newD);
				});
			}
			return minutesOfHourProperty;
		}

		private IntegerProperty secondsOfMinuteProperty;

		public IntegerProperty getSecondsOfMinuteProperty () {
			if (secondsOfMinuteProperty == null) {
				secondsOfMinuteProperty = new SimpleIntegerProperty ();
				secondsOfMinuteProperty.set ((int) (getValueProperty ().get ().getSeconds () % 60));
				getValueProperty ().addListener ((ObservableValue<? extends Duration> observable1, Duration oldValue, Duration newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}
					secondsOfMinuteProperty.set ((int) (getValueProperty ().get ().getSeconds () % 60));
				});
				secondsOfMinuteProperty.addListener ((ObservableValue<? extends Number> io, Number oldValue, Number newValue) -> {
					if (Objects.equals (oldValue, newValue)) {
						return;
					}

					Duration current = getValueProperty ().get ();

					int currentSeconds = (int) (current.getSeconds () % 60);
					if (Objects.equals (currentSeconds, newValue)) {
						return;
					}

					Duration newD = current.minusSeconds (currentSeconds).plusSeconds (newValue.longValue ());

					getValueProperty ().set (newD);
				});
			}
			return secondsOfMinuteProperty;
		}

		@Override
		public JsonObject valueToJson (Duration value) {
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
			int secondsOfMinute = json.getInt ("secondsOfMinute");
			int minutesOfHour = json.getInt ("minutesOfHour");
			int hoursOfDay = json.getInt ("hoursOfDay");
			long days = json.getJsonNumber ("days").longValue ();

			Duration d = Duration.ZERO.plusDays (days).plusHours (hoursOfDay).plusMinutes (minutesOfHour).plusSeconds (secondsOfMinute);
			return d;
		}

		@Override
		public String valueToDisplayedString (Duration value) {
			throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
			daysField.textProperty ().bindBidirectional ((Property<Number>) dataElement.getCurrentValueProperty ().getDaysProperty (), new NumberStringConverter ());

			hoursField = new TextField ();
			hoursField.textProperty ().bindBidirectional ((Property<Number>) dataElement.getCurrentValueProperty ().getHoursOfDayProperty (), new NumberStringConverter ());

			minutesField = new TextField ();
			hoursField.textProperty ().bindBidirectional ((Property<Number>) dataElement.getCurrentValueProperty ().getMinutesOfHourProperty (), new NumberStringConverter ());

			HBox hb = new HBox ();

			hb.getChildren ().add (daysField);
			hb.getChildren ().add (new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_DAYS")));

			hb.getChildren ().add (hoursField);
			hb.getChildren ().add (new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_HOURS")));

			hb.getChildren ().add (minutesField);
			hb.getChildren ().add (new Label (java.util.ResourceBundle.getBundle ("ru.dmerkushov.javafx.faces.data.dataelements.Bundle").getString ("DURATIONDE_FIELDLABEL_MINUTES")));

			daysField.getStyleClass ().add ("input-durationpart");
			hoursField.getStyleClass ().add ("input-durationpart");
			minutesField.getStyleClass ().add ("input-durationpart");
			hb.getStyleClass ().add ("input-duration");

			return hb;
		}
	}
}
