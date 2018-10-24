/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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
import javafx.util.converter.LocalDateTimeStringConverter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import ru.dmerkushov.javafx.faces.FacesConfiguration;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementValueProperty;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElementsModule;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 */
public class DateTimeDataElement extends DataElement<LocalDateTime> {

	public DateTimeDataElement (String elementTitle, String elementId, LocalDateTime defaultValue) {
		super (elementTitle, elementId, LocalDateTime.class, defaultValue);

		DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, Displayer.getInstance ());
	}

	public DateTimeDataElement (String elementTitle, String elementId) {
		this (elementTitle, elementId, LocalDateTime.now ());
	}

	ValueProperty currentValueProperty;

	@Override
	public ValueProperty getCurrentValueProperty () {
		if (currentValueProperty == null) {
			currentValueProperty = new ValueProperty (LocalDateTime.class);
		}
		return currentValueProperty;
	}

	public static class ValueProperty extends DataElementValueProperty<LocalDateTime> {

		LocalDateTimeStringConverter stringConverter;

		public ValueProperty (Class<LocalDateTime> valueClass) {
			super (valueClass);

			String dateTimePattern = FacesConfiguration.getUserPrefsForModule (DataElementsModule.class).get ("DATE_STRING_FORMAT", "yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern (dateTimePattern);
			stringConverter = new LocalDateTimeStringConverter (formatter, formatter);
		}

		@Override
		public LocalDateTime jsonToValue (JsonObject json) {

			if (json.containsKey ("value") && json.get ("value").equals (JsonValue.NULL)) {
				return null;
			}

			int year = json.getInt ("year", 2000);
			Month month = Month.valueOf (json.getString ("month", Month.JANUARY.name ()));
			int dayOfMonth = json.getInt ("dayOfMonth", 1);
			int hourOfDay = json.getInt ("hourOfDay", 0);
			int minuteOfHour = json.getInt ("minuteOfHour", 0);
			int secondOfMinute = json.getInt ("secondOfMinute", 0);
			int nanoOfSecond = json.getInt ("nanoOfSecond", 0);

			return LocalDateTime.of (year, month, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, nanoOfSecond);
		}

		@Override
		public JsonObject valueToJson (LocalDateTime value) {
			JsonObjectBuilder job = Json.createObjectBuilder ();

			if (value == null) {
				job.addNull ("value");
			} else {
				job.add ("year", value.getYear ());
				job.add ("month", value.getMonth ().name ());
				job.add ("dayOfMonth", value.getDayOfMonth ());
				job.add ("hourOfDay", value.getHour ());
				job.add ("minuteOfHour", value.getMinute ());
				job.add ("secondOfMinute", value.getSecond ());
				job.add ("nanoOfSecond", value.getNano ());
			}

			return job.build ();
		}

		@Override
		public String valueToDisplayedString (LocalDateTime value) {
			return stringConverter.toString (value);
		}

		private ObjectProperty<LocalDate> localDateProperty;

		public ObjectProperty<LocalDate> getLocalDateProperty () {
			if (localDateProperty == null) {
				localDateProperty = new ObjectPropertyBase<LocalDate> () {
					@Override
					public LocalDate get () {
						return ValueProperty.this.getValueProperty ().get ().toLocalDate ();
					}

					@Override
					public void set (LocalDate v) {
						int year = v.get (ChronoField.YEAR);
						int month = v.get (ChronoField.MONTH_OF_YEAR);
						int dayOfMonth = v.get (ChronoField.DAY_OF_MONTH);

						LocalDateTime curr = ValueProperty.this.getValueProperty ().get ();
						int hour = curr.get (ChronoField.HOUR_OF_DAY);
						int minute = curr.get (ChronoField.MINUTE_OF_HOUR);
						int second = curr.get (ChronoField.SECOND_OF_MINUTE);
						int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

						ValueProperty.this.getValueProperty ().set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
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
			}

			return localDateProperty;
		}

		private ObjectProperty<Integer> hourProperty;

		public ObjectProperty<Integer> getHourProperty () {
			if (hourProperty == null) {
				hourProperty = new ObjectPropertyBase<Integer> () {
					@Override
					public Integer get () {
						return ValueProperty.this.getValueProperty ().get ().getHour ();
					}

					@Override
					public void set (Integer v) {
						LocalDateTime curr = ValueProperty.this.getValueProperty ().get ();
						int year = curr.get (ChronoField.YEAR);
						int month = curr.get (ChronoField.MONTH_OF_YEAR);
						int dayOfMonth = curr.get (ChronoField.DAY_OF_MONTH);

						int hour = v;

						int minute = curr.get (ChronoField.MINUTE_OF_HOUR);
						int second = curr.get (ChronoField.SECOND_OF_MINUTE);
						int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

						ValueProperty.this.getValueProperty ().set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
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
			}

			return hourProperty;
		}

		private ObjectProperty<Integer> minuteProperty;

		public ObjectProperty<Integer> getMinuteProperty () {
			if (minuteProperty == null) {
				minuteProperty = new ObjectPropertyBase<Integer> () {
					@Override
					public Integer get () {
						return ValueProperty.this.getValueProperty ().get ().getMinute ();
					}

					@Override
					public void set (Integer v) {
						LocalDateTime curr = ValueProperty.this.getValueProperty ().get ();
						int year = curr.get (ChronoField.YEAR);
						int month = curr.get (ChronoField.MONTH_OF_YEAR);
						int dayOfMonth = curr.get (ChronoField.DAY_OF_MONTH);
						int hour = curr.get (ChronoField.HOUR_OF_DAY);

						int minute = v;

						int second = curr.get (ChronoField.SECOND_OF_MINUTE);
						int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

						ValueProperty.this.getValueProperty ().set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
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
			}

			return minuteProperty;
		}

		private ObjectProperty<Integer> secondProperty;

		public ObjectProperty<Integer> getSecondProperty () {
			if (secondProperty == null) {
				secondProperty = new ObjectPropertyBase<Integer> () {
					@Override
					public Integer get () {
						return ValueProperty.this.getValueProperty ().get ().getSecond ();
					}

					@Override
					public void set (Integer v) {
						LocalDateTime curr = ValueProperty.this.getValueProperty ().get ();
						int year = curr.get (ChronoField.YEAR);
						int month = curr.get (ChronoField.MONTH_OF_YEAR);
						int dayOfMonth = curr.get (ChronoField.DAY_OF_MONTH);
						int hour = curr.get (ChronoField.HOUR_OF_DAY);
						int minute = curr.get (ChronoField.MINUTE_OF_HOUR);

						int second = v;

						int nanoOfSecond = curr.get (ChronoField.NANO_OF_SECOND);

						ValueProperty.this.getValueProperty ().set (LocalDateTime.of (year, month, dayOfMonth, hour, minute, second, nanoOfSecond));
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
			}

			return secondProperty;
		}
	}

	public static class JsonSerializer extends DataElementJsonSerializerImpl<DateTimeDataElement, LocalDateTime> {

		public JsonSerializer () {
			super (DateTimeDataElement.class, LocalDateTime.class, new String[]{"elementTitle", "elementId", "defaultValue"});
		}
	}

	public static class Displayer implements DataElementDisplayer<DateTimeDataElement> {

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
		public Node getValueEdit (DateTimeDataElement dataElement) {
			TextField hourField = new TextField ();
			hourField.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getHourProperty (), new StringConverter<Integer> () {
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
			minuteField.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getMinuteProperty (), new StringConverter<Integer> () {
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

			TextField secondField = new TextField ();
			secondField.textProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getSecondProperty (), new StringConverter<Integer> () {
				@Override
				public String toString (Integer object) {
					return String.valueOf (object);
				}

				@Override
				public Integer fromString (String string) {
					return Integer.parseInt (string);
				}
			});
			secondField.setPrefWidth (40.0);

			HBox hb = new HBox ();
			hb.getChildren ().add (hourField);
			hb.getChildren ().add (new Label (":"));
			hb.getChildren ().add (minuteField);
			hb.getChildren ().add (new Label (":"));
			hb.getChildren ().add (secondField);

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
			datePicker.valueProperty ().bindBidirectional (dataElement.getCurrentValueProperty ().getLocalDateProperty ());

			VBox vb = new VBox ();
			vb.getChildren ().add (hb);
			vb.getChildren ().add (datePicker);

			return vb;
		}
	}
}
