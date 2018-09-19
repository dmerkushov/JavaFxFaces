package ru.dmerkushov.javafx.faces.data.dataelements.typed;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafx.faces.data.dataelements.json.DataElementJsonSerializerImpl;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.range.IntegerRange;

public class IntegerRangeDataElement extends DataElement<IntegerRange> {

	IntegerProperty minValueProperty;
	IntegerProperty maxValueProperty;
	IntegerProperty worstValueProperty;
	IntegerProperty bestValueProperty;

	public IntegerRangeDataElement (String elementTitle, String elementId, IntegerRange defaultValue, DataElementPersistenceProvider persistenceProvider) {
		super (
				elementTitle,
				elementId,
				IntegerRange.class,
				(defaultValue != null ? defaultValue : new IntegerRange (0, 0, true)),
				persistenceProvider
		);

		minValueProperty = new SimpleIntegerProperty (getCurrentValueProperty ().getValue ().getMin ());
		maxValueProperty = new SimpleIntegerProperty (getCurrentValueProperty ().getValue ().getMax ());
		getCurrentValueProperty ().addListener ((ObservableValue<? extends IntegerRange> observable, IntegerRange oldValue, IntegerRange newValue) -> {
			if (newValue.getMin () != minValueProperty.get ()) {
				minValueProperty.set (newValue.getMin ());
			}
			if (newValue.getMax () != maxValueProperty.get ()) {
				maxValueProperty.set (newValue.getMax ());
			}
		});
		minValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			IntegerRange currentVal = getCurrentValueProperty ().getValue ();
			int currentMax = currentVal.getMax ();
			IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMax, currentVal.minIsBest);
			getCurrentValueProperty ().updateValue (newVal);
		});
		maxValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			IntegerRange currentVal = getCurrentValueProperty ().getValue ();
			int currentMin = currentVal.getMin ();
			IntegerRange newVal = new IntegerRange ((Integer) newValue, currentMin, currentVal.minIsBest);
			getCurrentValueProperty ().updateValue (newVal);
		});

		if (this.defaultValue.minIsBest) {
			bestValueProperty = minValueProperty;
			worstValueProperty = maxValueProperty;
		} else {
			bestValueProperty = maxValueProperty;
			worstValueProperty = minValueProperty;
		}
	}

	@Override
	public String valueToStoredString (IntegerRange val) {
		if (val == null) {
			return "null";
		}

		return val.getMin () + "," + val.getMax () + "," + val.minIsBest;
	}

	@Override
	public IntegerRange storedStringToValue (String str) {
		if (str == null || str.equals ("null")) {
			return defaultValue;
		}

		String[] vals = str.split (",");
		return new IntegerRange (Integer.valueOf (vals[0]), Integer.valueOf (vals[1]), Boolean.valueOf (vals[2]));
	}

	@Override
	protected String valueToDisplayedString (IntegerRange val) {
		if (val == null) {
			return "";
		}

		return val.getMin () + "-" + val.getMax ();
	}

	@Override
	protected IntegerRange displayedStringToValue (String str) {
		String[] vals = str.split ("-");
		return new IntegerRange (Integer.valueOf (vals[0]), Integer.valueOf (vals[1]), this.getCurrentValueProperty ().getValue ().minIsBest);
	}

	@Override
	public Node getValueFxNode () {
		if (valueFxNode == null) {
			TextField minField = new TextField ();
			minValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = getCurrentValueProperty ().getValue ();
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
					minValueProperty.set (Integer.parseInt (realNewVal));
				}
			});
			minField.textProperty ().setValue (getCurrentValueProperty ().getValue ().getMin ().toString ());

			TextField maxField = new TextField ();
			maxValueProperty.addListener ((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
				IntegerRange currentVal = getCurrentValueProperty ().getValue ();
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
					maxValueProperty.set (Integer.parseInt (realNewVal));
				}
			});
			maxField.textProperty ().setValue (getCurrentValueProperty ().getValue ().getMax ().toString ());

			HBox hb = new HBox ();
			hb.getChildren ().add (minField);
			hb.getChildren ().add (new Label ("-"));
			hb.getChildren ().add (maxField);

			hb.getStyleClass ().add ("prop");

			valueFxNode = hb;
		}
		return valueFxNode;
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

	public static class JsonSerializer extends DataElementJsonSerializerImpl<IntegerRangeDataElement, IntegerRange> {

		public JsonSerializer () {
			super (IntegerRangeDataElement.class, IntegerRange.class, new String[]{"elementTitle", "elementId", "defaultValue", "persistenceProvider"});
		}

	}
}
