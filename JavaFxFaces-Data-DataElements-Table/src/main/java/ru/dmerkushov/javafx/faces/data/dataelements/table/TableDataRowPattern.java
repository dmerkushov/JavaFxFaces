package ru.dmerkushov.javafx.faces.data.dataelements.table;

import java.util.Arrays;
import java.util.Objects;
import ru.dmerkushov.javafx.faces.data.dataelements.DataElement;

public final class TableDataRowPattern {

	public final Class<DataElement>[] dataClasses;
	public final String[] columnTitles;

	public TableDataRowPattern (String[] columnTitles, Class<DataElement>[] dataClasses) {
		Objects.requireNonNull (columnTitles, "columnTitles");
		Objects.requireNonNull (dataClasses, "dataClasses");
		if (columnTitles.length != dataClasses.length) {
			throw new IllegalArgumentException ("Column title list length (" + columnTitles.length + ") does not match the data classes list length (" + dataClasses.length + ")");
		}
		if (dataClasses.length == 0) {
			throw new IllegalArgumentException ("Empty class list defined for table row model");
		}
		this.dataClasses = new Class[dataClasses.length];
		this.columnTitles = new String[columnTitles.length];
		for (int i = 0; i < dataClasses.length; i++) {
			Objects.requireNonNull (dataClasses[i], "dataClasses[" + i + "]");
			this.dataClasses[i] = dataClasses[i];
			this.columnTitles[i] = columnTitles[i] != null ? columnTitles[i] : "";
		}
	}

	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder ();

		sb.append (getClass ().getSimpleName ());
		sb.append (" {columnTitles: [");
		for (int i = 0; i < columnTitles.length; i++) {
			sb.append (columnTitles[i]);
			if (i < columnTitles.length - 1) {
				sb.append (", ");
			}
		}
		sb.append ("], dataClasses: [");
		for (int i = 0; i < dataClasses.length; i++) {
			sb.append (dataClasses[i]);
			if (i < dataClasses.length - 1) {
				sb.append (", ");
			}
		}
		sb.append ("]}");

		return sb.toString ();
	}

	@Override
	public int hashCode () {
		int hash = 5;
		hash = 89 * hash + Arrays.deepHashCode (this.dataClasses);
		hash = 89 * hash + Arrays.deepHashCode (this.columnTitles);
		return hash;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass () != obj.getClass ()) {
			return false;
		}
		final TableDataRowPattern other = (TableDataRowPattern) obj;
		if (!Arrays.deepEquals (this.dataClasses, other.dataClasses)) {
			return false;
		}
		if (!Arrays.deepEquals (this.columnTitles, other.columnTitles)) {
			return false;
		}
		return true;
	}

}
