/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.panels;

import java.util.Objects;
import java.util.UUID;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesPanelView extends Pane {

	FacesPanel panel;

	public FacesPanelView (FacesPanel panel) {
		Objects.requireNonNull (panel, "panel");
		this.panel = panel;
	}

	public FacesPanelView (FacesPanel panel, Node... children) {
		super (children);

		Objects.requireNonNull (panel, "panel");
		this.panel = panel;
	}

	public final FacesPanel getPanel () {
		return panel;
	}

	public final UUID getPanelUuid () {
		return panel.getPanelInstanceUuid ();
	}

}
