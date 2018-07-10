/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.UUID;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

/**
 *
 * @author dmerkushov
 */
public class FacesPanelImpl extends FacesPanel {

	private FacesPanelView view;

	private UUID uuid = UUID.fromString ("b8992266-3439-4861-81d4-0a640e99a7ba");

	private boolean viewRequested = false;

	public FacesPanelImpl (FacesPanelView view, UUID uuid) {

		if (view != null) {
			this.view = new MyPane (this);
		}

		if (uuid != null) {
			this.uuid = uuid;
		}

		FacesPanels.getInstance ().registerPanel (this);
	}

	@Override
	public UUID getPanelInstanceUuid () {
		return uuid;
	}

	@Override
	public String getDisplayName () {
		return "Simple Panel " + getPanelInstanceUuid ();
	}

	@Override
	public String getToolTip () {
		return "Simple FxFaces panel";
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
		viewRequested = true;

		return view;
	}

	public boolean isViewRequested () {
		return viewRequested;
	}

	public void setView (FacesPanelView view) {
		Objects.requireNonNull (view, "view");

		if (viewRequested) {
			throw new IllegalStateException ("The view has been requested earlier, so cannot set it now");
		}

		this.view = view;
	}

	public static final class MyPane extends FacesPanelView {

		private MyPane (FacesPanelImpl panel) {
			super (panel);
			this.getChildren ().add (new Label ("A label"));
			this.minHeightProperty ().setValue (100.0);
			this.minWidthProperty ().setValue (300.0);
		}

	}

}
