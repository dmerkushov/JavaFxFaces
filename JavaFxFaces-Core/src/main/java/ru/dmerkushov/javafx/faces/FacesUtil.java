/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces;

import java.util.Objects;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.layout.Region;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesUtil {

	/**
	 * Bind the size of the given region to the given width and height
	 * observable values. This will bind all the 3 size properties: minimum
	 * size, maximum size and preferred size
	 *
	 * @param region must not be null
	 * @param width may be null; if null, the region's width will not be bound
	 * @param height may be null; if null, the region's height will not be bound
	 *
	 * @throws NullPointerException if the region is null
	 */
	public static void bindWidthHeight (Region region, ObservableDoubleValue width, ObservableDoubleValue height) {
		Objects.requireNonNull (region, "region");

		if (width != null) {
			region.minWidthProperty ().bind (width);
			region.maxWidthProperty ().bind (width);
			region.prefWidthProperty ().bind (width);
		}

		if (height != null) {
			region.minHeightProperty ().bind (height);
			region.maxHeightProperty ().bind (height);
			region.prefHeightProperty ().bind (height);
		}
	}

}
