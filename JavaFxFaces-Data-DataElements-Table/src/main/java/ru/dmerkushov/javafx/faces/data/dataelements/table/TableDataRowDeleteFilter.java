/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.data.dataelements.table;

/**
 *
 * @author dmerkushov
 */
public interface TableDataRowDeleteFilter {

	boolean canDelete (TableDataRow tdr);

	void afterDelete (TableDataRow tdr);

}
