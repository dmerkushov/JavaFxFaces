/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.table.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.docks.table.TableDock;
import ru.dmerkushov.javafx.faces.docks.test.TestDocksPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class TestTableDockModule extends FacesModule {

	public TestTableDockModule (Preferences modulePrefs) {
		super (modulePrefs);
	}

	TestDocksPanel panel;

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
		panel = new TestDocksPanel ();

		UUID tableDockUuid = UUID.fromString ("e342760c-1208-4962-845a-771d944cf635");

		TableDock dock = new TableDock (tableDockUuid, "My dock", "My dock tooltip", 5, panel);

		panel.getView ().setOnMouseClicked ((e) -> {
			facesLoggerWrapper.info ("Adding a new panel");

			final FacesPanel newPanel = new TestDocksPanel ();

			newPanel.getView ().setOnMouseClicked ((e1) -> {
				dock.removePanel (newPanel);
			});

			dock.addPanel (newPanel);
		});

		FacesPanels.getInstance ().registerPanel (dock);
	}

	@Override
	public Collection<Class<? extends FacesModule>> getDependencies () throws FacesException {
		return new ArrayList<> (0);
	}

	@Override
	public void finish () throws FacesException {
		//Do nothing
	}
}
