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
package ru.dmerkushov.javafx.faces;

import ru.dmerkushov.javafx.faces.panels.FacesPanels;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public abstract class FacesDock extends FacesPanel {

	public abstract void addPanel (int panelIndex, FacesPanel panel);

	public void addPanel (FacesPanel panel) {
		addPanel (getPanelsCount (), panel);
	}

	public abstract void removePanel (int panelIndex);

	public void removePanel (FacesPanel panel) {
		List<FacesPanel> panels = getPanels ();
		int panelIndex = 0;
		for (FacesPanel currPanel : panels) {
			if (panel == currPanel) {
				break;
			}
			panelIndex++;
		}
		removePanel (panelIndex);
	}

	public abstract boolean containsPanel (FacesPanel panel);

	public final boolean containsPanel (UUID panelUuid) {
		Objects.requireNonNull (panelUuid, "panelUuid");

		FacesPanel panel = FacesPanels.getInstance ().getPanel (panelUuid);
		return containsPanel (panel);
	}

	public abstract List<FacesPanel> getPanels ();

	public int getPanelsCount () {
		return getPanels ().size ();
	}

}
