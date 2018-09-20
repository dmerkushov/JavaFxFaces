/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementView extends TableView {

	private TableDataElement tde;
	private Property<TableData> tdp;

	static TableDataElementCell currentlyEditing = null;

	public TableDataElementView (TableDataElement tde) {
		super ();

		Objects.requireNonNull (tde, "tde");

		this.tde = tde;
		this.tdp = tde.getCurrentValueProperty ();

		TableData td = tdp.getValue ();
		TableDataRowPattern rp = td.getRowPattern ();

		TableColumn[] tableColumns = new TableColumn[rp.columnTitles.length];

		for (int i = 0; i < rp.columnTitles.length; i++) {
			tableColumns[i] = new TableColumn (rp.columnTitles[i]);

			final int fin_i = i;

			tableColumns[i].setCellFactory ((TableColumn) -> new TableDataElementCell (fin_i));
			tableColumns[i].setCellValueFactory (new TableDataElementCellValueFactory (fin_i));
		}

		this.setEditable (true);

		this.setItems (tde.getRows ());

		getColumns ().addAll (tableColumns);
	}

	public class TableDataElementCellValueFactory implements Callback<CellDataFeatures<TableDataRow, String>, ObservableValue<String>> {

		int columnIndex;

		public TableDataElementCellValueFactory (int columnIndex) {
			this.columnIndex = columnIndex;
		}

		@Override
		public ObservableValue<String> call (CellDataFeatures<TableDataRow, String> param) {
			DataElement de = param.getValue ().getDataElement (columnIndex);

			SimpleObjectProperty<String> prop = new SimpleObjectProperty<> ();
			Bindings.bindBidirectional (prop, de.getCurrentValueProperty (), new StringConverter () {
				@Override
				public String toString (Object object) {
					return de.valueToStoredString (object);
				}

				@Override
				public Object fromString (String string) {
					return de.storedStringToValue (string);
				}
			}
			);

			return prop;
		}
	}

	public class TableDataElementCell extends TableCell {

		private int _columnIndex;

		public TableDataElementCell (int columnIndex) {
			super ();

			_columnIndex = columnIndex;
			this.editingProperty ();

			setOnMouseClicked (new EventHandler<MouseEvent> () {
				@Override
				public void handle (MouseEvent event) {
					if (currentlyEditing != null && currentlyEditing != TableDataElementCell.this) {
						currentlyEditing.cancelEdit ();
						currentlyEditing.resetGraphic ();
					}

					if (event.getClickCount () >= 2) {
						TableDataElementCell.this.startEdit ();

						currentlyEditing = TableDataElementCell.this;
					}
				}
			});

			this.getStyleClass ().add ("TableDataElement_table_cell");
			this.getStyleClass ().add ("TableDataElement_" + TableDataElementView.this.tde.elementId + "_table_cell");
			this.getStyleClass ().add ("TableDataElement_table_column_" + columnIndex + "_cell");
			this.getStyleClass ().add ("TableDataElement_" + TableDataElementView.this.tde.elementId + "_table_column_" + columnIndex + "_cell");
		}

		@Override
		protected void updateItem (Object item, boolean empty) {
			super.updateItem (item, empty);

			if (!empty && item != null) {
				if (item instanceof String) {
					resetGraphic ();
				} else {
					setText (null);
					setGraphic (null);
				}
			} else {
				setText (null);
				setGraphic (null);
			}
		}

		protected void resetGraphic () {
			TableRow row = this.getTableRow ();
			if (row == null) {
				setText (null);
				setGraphic (null);
				return;
			}

			int rowIndex = row.getIndex ();

			TableDataRow tdr;
			try {
				tdr = tde.getRows ().get (rowIndex);
			} catch (IndexOutOfBoundsException ex) {
				setText (null);
				setGraphic (null);
				return;
			}

			DataElement de = tdr.getDataElement (_columnIndex);

			Node view;

			if (isEditing ()) {
				view = de.getValueFxNode ();
				Platform.runLater (() -> {
					view.requestFocus ();
				});
			} else {
				view = de.getValueViewFxNode ();
			}

			setGraphic (view);
		}

	}

}
