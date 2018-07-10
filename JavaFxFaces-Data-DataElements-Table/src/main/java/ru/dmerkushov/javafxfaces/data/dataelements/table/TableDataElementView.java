/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.Objects;
import javafx.beans.property.Property;
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
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;
import ru.dmerkushov.javafxfaces.data.dataelements.table.TableData.TableDataRow;

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
		TableData.RowPattern rp = td.getRowPattern ();

		TableColumn[] tableColumns = new TableColumn[rp.columnTitles.length];

		for (int i = 0; i < rp.columnTitles.length; i++) {
			tableColumns[i] = new TableColumn (rp.columnTitles[i]);

			final int fin_i = i;

			tableColumns[i].setCellFactory ((TableColumn) -> new TableDataElementCell (fin_i));
			tableColumns[i].setCellValueFactory (new TableDataElementCellValueFactory (fin_i));
		}

		this.setEditable (true);

		this.setItems (tdp.getValue ().getRows ());

		getColumns ().addAll (tableColumns);
	}

	public class TableDataElementCellValueFactory implements Callback<CellDataFeatures<TableData.TableDataRow, String>, ObservableValue<String>> {

		int columnIndex;

		public TableDataElementCellValueFactory (int columnIndex) {
			this.columnIndex = columnIndex;
		}

		@Override
		public ObservableValue<String> call (CellDataFeatures<TableDataRow, String> param) {
			return param.getValue ().dataElements[columnIndex].getCurrentValueStoredStringProperty ();
		}
	}

	public class TableDataElementCell extends TableCell {

		int columnIndex;

		public TableDataElementCell (int columnIndex) {
			super ();

			this.columnIndex = columnIndex;
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

			TableData td = tdp.getValue ();
			TableData.TableDataRow tdr;
			try {
				tdr = td.getRows ().get (rowIndex);
			} catch (IndexOutOfBoundsException ex) {
				setText (null);
				setGraphic (null);
				return;
			}

			DataElement de = tdr.dataElements[columnIndex];

			Node view = isEditing () ? de.getValueFxNode () : de.getValueViewFxNode ();

			setGraphic (view);
		}

	}

}
