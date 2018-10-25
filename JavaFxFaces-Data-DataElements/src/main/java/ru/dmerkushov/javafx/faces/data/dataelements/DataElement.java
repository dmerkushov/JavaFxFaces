/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayer;
import ru.dmerkushov.javafx.faces.data.dataelements.display.DataElementDisplayerRegistry;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProvider;
import ru.dmerkushov.javafx.faces.data.dataelements.persist.DataElementPersistenceProviderRegistry;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;

/**
 *
 * @author Dmitriy Merkushov (d.merkushov at gmail.com)
 * @param <T> The type of the data element in the database
 */
public abstract class DataElement<T> extends FacesPanel {

	/**
	 * The data element's title to be displayed
	 */
	public final String elementTitle;

	/**
	 * The data element's ID for the storing purposes
	 */
	public final String elementId;

	/**
	 * The type of data for the data element
	 */
	public final Class valueType;

	/**
	 * The default value for the data element
	 */
	private T defaultValue;

	private ObjectProperty<String> currentValueDisplayedStringProperty;

	/**
	 *
	 * @param elementTitle
	 * @param elementId
	 * @param valueType
	 * @param defaultValue
	 * @param persistenceProvider may be null
	 */
	public DataElement (String elementTitle, String elementId, Class<T> valueType, T defaultValue) {
		Objects.requireNonNull (elementTitle, "elementTitle");
		Objects.requireNonNull (elementId, "elementId");
		Objects.requireNonNull (valueType, "valueType");

		this.elementTitle = elementTitle;
		this.elementId = elementId;
		this.valueType = valueType;

		setDefaultValue (defaultValue);
	}

	private void setDefaultValue (T defaultValue) {
		this.defaultValue = defaultValue;

		if (getCurrentValueProperty () == null) {
			return;
		}
		if (getCurrentValueProperty ().getValueProperty () == null) {
			return;
		}

		getCurrentValueProperty ().getValueProperty ().set (defaultValue);
	}

	public T getDefaultValue () {
		return defaultValue;
	}

	/**
	 * Get the property that keeps the current value of this data element
	 *
	 * @return
	 */
	public abstract DataElementValueProperty<T> getCurrentValueProperty ();

	/**
	 * Get the property that keeps the current value of this data element in the
	 * form to be displayed
	 *
	 * @return
	 */
	public ReadOnlyObjectProperty<String> getCurrentValueDisplayedStringProperty () {
		if (currentValueDisplayedStringProperty == null) {
			this.currentValueDisplayedStringProperty = new SimpleObjectProperty<> ();

			currentValueDisplayedStringProperty.bind (Bindings.createStringBinding (new Callable<String> () {
				@Override
				public String call () throws Exception {
					return getCurrentValueProperty ().currentValueToDisplayedString ();
				}
			}, getCurrentValueProperty ()));
		}
		return currentValueDisplayedStringProperty;
	}

	private UUID panelInstanceUuid = UUID.randomUUID ();

	@Override
	public UUID getPanelInstanceUuid () {
		return panelInstanceUuid;
	}

	public void setPanelInstanceUuid (UUID panelInstanceUuid) {
		DataElementDisplayer displayer = DataElementDisplayerRegistry.getInstance ().getDisplayer (this);

		this.panelInstanceUuid = panelInstanceUuid;

		if (displayer != null) {
			DataElementDisplayerRegistry.getInstance ().registerDisplayer (this, displayer);
		}
	}

	@Override
	public String getDisplayName () {
		return elementTitle;
	}

	@Override
	public String getToolTip () {
		return elementTitle;
	}

	public final DataElementPersistenceProvider getPersistenceProvider () {
		return DataElementPersistenceProviderRegistry.getInstance ().getPersistenceProvider (elementId);
	}

	@Override
	protected Image prepareIcon (int width, int height) {
		return null;
	}

	private FacesPanelView fpView;

	@Override
	public FacesPanelView getView () {
		if (fpView == null) {
			fpView = new FacesPanelView (this, DataElementDisplayerRegistry.getInstance ().getDisplayer (this).getValueEdit (this));
		}
		return fpView;
	}
}
