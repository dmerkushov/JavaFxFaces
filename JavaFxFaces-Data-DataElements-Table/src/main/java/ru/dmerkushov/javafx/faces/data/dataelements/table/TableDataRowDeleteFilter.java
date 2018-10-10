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

	default boolean canDelete (TableDataRow tdr) {
		// Do nothing by default
		return true;
	}

	default void afterDelete (TableDataRow tdr) {
		// Do nothing by default
	}

}
