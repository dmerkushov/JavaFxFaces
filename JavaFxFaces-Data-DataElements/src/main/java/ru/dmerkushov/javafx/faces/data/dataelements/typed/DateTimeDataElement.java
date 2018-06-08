/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementException;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DateTimeDataElement extends DataElement<LocalDateTime> {

	public static final String FIELD_DELIMITER = "_";

	public final ObjectProperty<LocalDate> currentValueLocalDateProperty = new ObjectPropertyBase<LocalDate> () {
		@Override
		public LocalDate get () {
			return currentValueProperty.get ().toLocalDate ();
		}

		@Override
		public void set (LocalDate v) {
			int year = v.get (ChronoField.YEAR);
			int month = v.get (ChronoField.MONTH_OF_YEAR);
			int dayOfMonth = v.get (ChronoField.DAY_OF_MONTH);

			LocalDateTime curr = currentValueProperty.getValue ();
			int hour = curr.get (ChronoField.HOUR_OF_DAY);
			int minute = curr.get (ChronoField.MINUTE_OF_HOUR);
			int second = curr.get (ChronoField.SECOND_OF_MINUTE);
			int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

			currentValueProperty.set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
		}

		@Override
		public Object getBean () {
			return null;
		}

		@Override
		public String getName () {
			return "";
		}
	};

	public final ObjectProperty<Integer> currentValueHourProperty = new ObjectPropertyBase<Integer> () {
		@Override
		public Integer get () {
			return currentValueProperty.get ().getHour ();
		}

		@Override
		public void set (Integer v) {
			LocalDateTime curr = currentValueProperty.getValue ();
			int year = curr.get (ChronoField.YEAR);
			int month = curr.get (ChronoField.MONTH_OF_YEAR);
			int dayOfMonth = curr.get (ChronoField.DAY_OF_MONTH);

			int hour = v;

			int minute = curr.get (ChronoField.MINUTE_OF_HOUR);
			int second = curr.get (ChronoField.SECOND_OF_MINUTE);
			int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

			currentValueProperty.set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
		}

		@Override
		public Object getBean () {
			return null;
		}

		@Override
		public String getName () {
			return "";
		}
	};

	public final ObjectProperty<Integer> currentValueMinuteProperty = new ObjectPropertyBase<Integer> () {
		@Override
		public Integer get () {
			return currentValueProperty.get ().getMinute ();
		}

		@Override
		public void set (Integer v) {
			LocalDateTime curr = currentValueProperty.getValue ();
			int year = curr.get (ChronoField.YEAR);
			int month = curr.get (ChronoField.MONTH_OF_YEAR);
			int dayOfMonth = curr.get (ChronoField.DAY_OF_MONTH);
			int hour = curr.get (ChronoField.HOUR_OF_DAY);

			int minute = v;

			int second = curr.get (ChronoField.SECOND_OF_MINUTE);
			int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

			currentValueProperty.set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
		}

		@Override
		public Object getBean () {
			return null;
		}

		@Override
		public String getName () {
			return "";
		}
	};

	public DateTimeDataElement (String elementTitle, String elementId, LocalDateTime defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (elementTitle, elementId, LocalDateTime.class, defaultValue, persistenceProvider);
	}

	public DateTimeDataElement (String elementTitle, String elementId, DataElementPersistenceProvider persistenceProvider) {
		this (elementTitle, elementId, LocalDateTime.now (), persistenceProvider);
	}

	@Override
	public String valueToStoredString (LocalDateTime val) {
		if (val == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder ();
		sb.append (val.get (ChronoField.YEAR)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.MONTH_OF_YEAR)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.DAY_OF_MONTH)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.HOUR_OF_DAY)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.MINUTE_OF_HOUR)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.SECOND_OF_MINUTE)).append (FIELD_DELIMITER);
		sb.append (val.get (ChronoField.NANO_OF_SECOND));

		return sb.toString ();
	}

	@Override
	public LocalDateTime storedStringToValue (String str) {
		if (str == null || str.equals ("") || str.equals ("null") || str.equals ("NULL")) {
			return null;
		}

		String[] fields = str.split (FIELD_DELIMITER);

		if (fields.length != 7) {
			throw new DataElementException ("Incorrect LocalDateTime format (must be 7 parts as split by \"" + FIELD_DELIMITER + "\"): " + str);
		}

		int year = Integer.parseInt (fields[0]);
		int month = Integer.parseInt (fields[1]);
		int dayOfMonth = Integer.parseInt (fields[2]);
		int hour = Integer.parseInt (fields[3]);
		int minute = Integer.parseInt (fields[4]);
		int second = Integer.parseInt (fields[5]);
		int nanoOfSecond = Integer.parseInt (fields[6]);

		return LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			TextField hourField = new TextField ();
			hourField.textProperty ().bindBidirectional (currentValueHourProperty, new StringConverter<Integer> () {
				@Override
				public String toString (Integer object) {
					return String.valueOf (object);
				}

				@Override
				public Integer fromString (String string) {
					return Integer.parseInt (string);
				}
			});
			hourField.setPrefWidth (40.0);

			TextField minuteField = new TextField ();
			minuteField.textProperty ().bindBidirectional (currentValueMinuteProperty, new StringConverter<Integer> () {
				@Override
				public String toString (Integer object) {
					return String.valueOf (object);
				}

				@Override
				public Integer fromString (String string) {
					return Integer.parseInt (string);
				}
			});
			minuteField.setPrefWidth (40.0);

			HBox hb = new HBox ();
			hb.getChildren ().add (hourField);
			hb.getChildren ().add (new Label (":"));
			hb.getChildren ().add (minuteField);

			DatePicker datePicker = new DatePicker ();
			datePicker.setConverter (new StringConverter<LocalDate> () {

				String pattern = "dd.MM.yyyy";
				DateTimeFormatter f = DateTimeFormatter.ofPattern (pattern);

				@Override
				public String toString (LocalDate date) {
					if (date == null) {
						return "";
					}
					return f.format (date);
				}

				@Override
				public LocalDate fromString (String string) {
					if (string == null || string.isEmpty ()) {
						return null;
					}
					return LocalDate.parse (string, f);
				}
			});
			datePicker.valueProperty ().bindBidirectional (currentValueLocalDateProperty);

			VBox vb = new VBox ();
			vb.getChildren ().add (hb);
			vb.getChildren ().add (datePicker);

			valueFxNode = vb;
		}

		return valueFxNode;
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DateTimeDataElement, LocalDateTime> {

		public JsonSerializer () {
			super (DateTimeDataElement.class, LocalDateTime.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}

}
