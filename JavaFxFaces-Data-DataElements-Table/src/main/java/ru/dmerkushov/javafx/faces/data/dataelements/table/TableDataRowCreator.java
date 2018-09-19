/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

import javax.json.JsonObject;

/**
 *
 * @author dmerkushov
 */
public interface TableDataRowCreator {

	TableDataRow createNewRow (TableData tableData);

	JsonObject toStoredJson ();

}
