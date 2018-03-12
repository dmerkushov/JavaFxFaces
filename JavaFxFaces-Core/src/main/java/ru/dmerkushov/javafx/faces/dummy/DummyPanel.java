/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.dummy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.UUID;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanelView;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public final class DummyPanel extends FacesPanel {

	private FacesPanelView myView;

	public Label panelLabel = new Label ("Pane label");

	public DummyPanel () {
		myView = new MyPane (this);

		FacesPanels.getInstance ().registerPanel (this);
	}

	@Override
	public UUID getPanelInstanceUuid () {
		return UUID.fromString ("b8992266-3439-4861-81d4-0a640e99a7ba");
	}

	@Override
	public String getDisplayName () {
		return "Dummy Panel";
	}

	@Override
	public String getToolTip () {
		return "Simple dummy panel";
	}

	@Override
	protected Image prepareIcon (int width, int height) {
		BufferedImage bi = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics ();
		g.setColor (Color.white);
		g.fillRect (0, 0, width, height);
		g.setColor (Color.black);
		g.setFont (Font.getFont (Font.DIALOG));
		g.drawString (getDisplayName (), 10, 10);

		return SwingFXUtils.toFXImage (bi, null);
	}

	@Override
	public FacesPanelView getView () {
		return myView;
	}

	public static final class MyPane extends FacesPanelView {

		public MyPane (DummyPanel panel) {
			super (panel);
			this.getChildren ().add (panel.panelLabel);
			this.minHeightProperty ().setValue (100.0);
			this.minWidthProperty ().setValue (300.0);
		}

	}

}
