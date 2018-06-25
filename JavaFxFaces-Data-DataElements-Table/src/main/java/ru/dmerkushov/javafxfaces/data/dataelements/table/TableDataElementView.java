/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafxfaces.data.dataelements.table;

import java.util.Objects;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

/**
 *
 * @author dmerkushov
 */
public class TableDataElementView extends TableView {

	private TableDataElement tde;
	private Property<TableData> tdp;

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

			final int ifin = i;

			tableColumns[i].setCellFactory (new Callback<TableColumn, TableCell> () {
				@Override
				public TableCell call (TableColumn column) {
					return new TableDataElementCell (ifin);
				}
			});
		}

		getColumns ().addAll (tableColumns);
	}

	public class TableDataElementCell extends TableCell {

		int rowIndex;
		int columnIndex;

		public TableDataElementCell (int columnIndex) {
			this.columnIndex = columnIndex;

			TableRow row = this.getTableRow ();
			rowIndex = row.getIndex ();

			TableData td = tdp.getValue ();
			TableData.TableDataRow tdr = td.getRow (rowIndex);

			DataElement de = tdr.dataElements[columnIndex];
//			Node viewCell = de.getValueViewFxNode ();
			Node editCell = de.getValueFxNode ();

			setGraphic (editCell);
		}

		@Override
		protected void updateItem (Object item, boolean empty) {
			super.updateItem (item, empty);

			if () {

			}
		}

	}

}
