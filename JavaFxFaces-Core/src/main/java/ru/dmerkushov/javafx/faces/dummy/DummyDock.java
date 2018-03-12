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
package ru.dmerkushov.javafx.faces.dummy;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.dmerkushov.javafx.faces.FacesDock;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class DummyDock extends FacesDock {

	UUID uuid;
	FacesPanel panel;
	FacesPanelView myView;
	ImageView icon;
	Pane panelPane;

	public DummyDock (FacesPanel panel) {
		this.panel = panel;
		FacesPanels.getInstance ().registerPanel (this);

		myView = new FacesPanelView (this);

		icon = new ImageView (panel.getIcon (48, 48));
		panelPane = panel.getView ();

		panelPane.layoutXProperty ().bind (icon.fitWidthProperty ().add (50.0));

		uuid = UUID.fromString ("8e2bf236-81d5-4f6e-813d-6118b5467bbf");

		myView.getChildren ().addAll (icon, panelPane);

		FacesPanels.getInstance ().registerPanel (this);
	}

	@Override
	public String getDisplayName () {
		return "Dummy Dock: " + panel.getDisplayName ();
	}

	@Override
	public String getToolTip () {
		return panel.getToolTip ();
	}

	@Override
	protected Image prepareIcon (int width, int height) {
		return panel.getIcon (width, height);
	}

	@Override
	public FacesPanelView getView () {
		return myView;
	}

	@Override
	public UUID getPanelInstanceUuid () {
		return uuid;
	}

	@Override
	public List<FacesPanel> getPanels () {
		return Arrays.asList (panel);
	}

	@Override
	public void addPanel (int panelIndex, FacesPanel panel) {
		//TODO Implement generated addPanel() in DummyDock
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public void removePanel (int panelIndex) {
		//TODO Implement generated removePanel() in DummyDock
		throw new UnsupportedOperationException ("Not supported yet.");
	}

	@Override
	public boolean containsPanel (FacesPanel panel) {
		return this.panel == panel;
	}

}
