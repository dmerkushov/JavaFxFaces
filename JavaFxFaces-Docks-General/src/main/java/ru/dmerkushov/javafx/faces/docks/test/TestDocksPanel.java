/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.test;

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

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class TestDocksPanel extends FacesPanel {

	UUID uuid = null;
	FacesPanelView myView = null;

	@Override
	public UUID getPanelInstanceUuid () {
		if (uuid == null) {
			uuid = UUID.randomUUID ();
		}

		return uuid;
	}

	@Override
	public String getDisplayName () {
		return getPanelInstanceUuid ().toString ();
	}

	@Override
	public String getToolTip () {
		return getPanelInstanceUuid ().toString ();
	}

	@Override
	public FacesPanelView getView () {
		if (myView == null) {
			myView = new TestDocksPanelView (this);
		}

		return myView;
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

	public static final class TestDocksPanelView extends FacesPanelView {

		public TestDocksPanelView (TestDocksPanel panel) {
			super (panel);

			Label label = new Label (panel.getPanelInstanceUuid ().toString ());
			label.maxWidthProperty ().bind (widthProperty ());
			this.getChildren ().add (label);
		}

	}

}
