/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.time.Duration;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DurationDataElement extends DataElement<Duration> {

	long days;
	long hours;
	long minutes;

	TextField daysField;
	TextField hoursField;
	TextField minutesField;

	public DurationDataElement (String elementTitle, String elementId, Duration defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (
				elementTitle,
				elementId,
				Duration.class,
				defaultValue != null ? defaultValue : Duration.ofMillis (0L),
				persistenceProvider
		);

		minutes = currentValueProperty.get ().toMinutes () % 60;
		hours = currentValueProperty.get ().toHours () % 24;
		days = currentValueProperty.get ().toDays ();

		showNewValues ();

		currentValueProperty.addListener ((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
			long currentMinutes = minutes;
			long currentHours = hours;
			long currentDays = days;

			long newMinutes = newValue.toMinutes () % 60;
			long newHours = newValue.toHours () % 24;
			long newDays = newValue.toDays ();

			if (!(currentMinutes == newMinutes && currentHours == newHours && currentDays == newDays)) {
				minutes = newMinutes;
				hours = newHours;
				days = newDays;
				showNewValues ();
			}
		});
	}

	public DurationDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, Duration.ZERO, persistenceProvider);
	}

	@Override
	public Duration storedStringToValue (String str) {
		if (str == null || str.equals ("null") || str.equals ("")) {
			return defaultValue;
		}

		return Duration.parse (str);
	}

	@Override
	public String valueToStoredString (Duration val) {
		if (val == null) {
			return "null";
		}

		return val.toString ();
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			daysField = new TextField (String.valueOf (minutes));
			daysField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					String realNewVal = (newValue == null ? "0" : newValue.replaceAll ("^[\\D]", ""));
					if (realNewVal.equals ("")) {
						realNewVal = "0";
					}
					currentValueProperty.set (
							currentValueProperty.get ()
									.minus (Duration.ofDays (currentValueProperty.get ().toDays ()))
									.plus (Duration.ofDays (Long.parseLong (realNewVal)))
					);
				}
			});

			hoursField = new TextField (String.valueOf (hours));
			hoursField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					String realNewVal = (newValue == null ? "0" : newValue.replaceAll ("^[\\D]", ""));
					if (realNewVal.equals ("")) {
						realNewVal = "0";
					}
					currentValueProperty.set (
							currentValueProperty.get ()
									.minus (Duration.ofHours (currentValueProperty.get ().toHours () % 24))
									.plus (Duration.ofHours (Long.parseLong (realNewVal)))
					);
				}
			});

			minutesField = new TextField (String.valueOf (days));
			minutesField.textProperty ().addListener (new ChangeListener<String> () {
				@Override
				public void changed (ObservableValue<? extends String> observable, String oldValue, String newValue) {
					String realNewVal = (newValue == null ? "0" : newValue.replaceAll ("^[\\D]", ""));
					if (realNewVal.equals ("")) {
						realNewVal = "0";
					}
					currentValueProperty.set (
							currentValueProperty.get ()
									.minus (Duration.ofMinutes (currentValueProperty.get ().toMinutes () % 60))
									.plus (Duration.ofMinutes (Long.parseLong (realNewVal)))
					);
				}
			});

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

			valueFxNode = hb;
		}

		return valueFxNode;
	}

	private void adjustCurrentValues () {
		minutes = Long.parseLong (minutesField.textProperty ().get ());
		hours = Long.parseLong (hoursField.textProperty ().get ());
		days = Long.parseLong (daysField.textProperty ().get ());

		currentValueProperty.set (Duration.ofDays (days).plus (Duration.ofHours (hours)).plus (Duration.ofMinutes (minutes)));
	}

	private void showNewValues () {
		if (minutesField != null) {
			minutesField.textProperty ().set (String.valueOf (minutes));
		}
		if (hoursField != null) {
			hoursField.textProperty ().set (String.valueOf (hours));
		}
		if (daysField != null) {
			daysField.textProperty ().set (String.valueOf (days));
		}
	}

	public class DurationFieldProperty extends ObjectPropertyBase<Long> {

		Long val = 0L;

		@Override
		public void set (Long newValue) {
			val = newValue;
			adjustCurrentValues ();
		}

		@Override
		public Long get () {
			return val;
		}

		public String getAsString () {
			return String.valueOf (get ());
		}

		public void setAsString (String str) {
			set (Long.parseLong (str));
		}

		@Override
		public Object getBean () {
			return null;
		}

		@Override
		public String getName () {
			return "";
		}

	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DurationDataElement, Duration> {

		public JsonSerializer () {
			super (DurationDataElement.class, Duration.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
