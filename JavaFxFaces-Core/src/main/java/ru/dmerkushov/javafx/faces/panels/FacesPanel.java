/*
 * Copyright 2017 Dmitriy Merkushov <d.merkushov@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.dmerkushov.javafx.faces.panels;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public abstract class FacesPanel {

	private final Map<Dimension, Image> icons = new HashMap<> ();
	private final Object iconsLock = new Object ();

	public abstract UUID getPanelInstanceUuid ();

	public abstract String getDisplayName ();

	public abstract String getToolTip ();

	public final Image getIcon (int width, int height) {
		return getIcon (new Dimension (width, height));
	}

	public final Image getIcon (Dimension d) {
		Image resultIcon = null;
		synchronized (iconsLock) {
			if (!icons.containsKey (d)) {
				Image appIcon = prepareIcon (d.width, d.height);

				icons.put (d, appIcon);
			}
			resultIcon = icons.get (d);
		}

		return resultIcon;
	}

	/**
	 * @deprecated No need in this method
	 * @param srcIcon
	 * @param bgColor
	 * @return
	 */
	public static final Image getIconOnBackgroundColor (Image srcIcon, Color bgColor) {
		int width = (int) srcIcon.getWidth ();
		int height = (int) srcIcon.getHeight ();
		PixelReader srcIconR = srcIcon.getPixelReader ();

		WritableImage icon = new WritableImage (width, height);
		PixelWriter iconW = icon.getPixelWriter ();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color pixel;
				try {
					pixel = srcIconR.getColor (x, y);
				} catch (IndexOutOfBoundsException ex) {
					pixel = bgColor;
				}
				if (pixel.getOpacity () < 0.1) {
					iconW.setColor (x, y, bgColor);
				} else {
					iconW.setColor (x, y, pixel);
				}
			}
		}

		return icon;
	}

	protected abstract Image prepareIcon (int width, int height);

	public abstract FacesPanelView getView ();

	@Override
	public final boolean equals (Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof FacesPanel)) {
			return false;
		}
		return ((FacesPanel) o).getPanelInstanceUuid ().equals (this.getPanelInstanceUuid ());
	}

	@Override
	public final int hashCode () {
		return getPanelInstanceUuid ().hashCode ();
	}

	@Override
	public String toString () {
		return getPanelInstanceUuid ().toString ();
	}

}
