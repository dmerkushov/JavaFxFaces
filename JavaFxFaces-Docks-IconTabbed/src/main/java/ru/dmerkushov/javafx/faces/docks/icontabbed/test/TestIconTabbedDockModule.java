/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces.docks.icontabbed.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.FacesException;
import ru.dmerkushov.javafx.faces.FacesModule;
import ru.dmerkushov.javafx.faces.docks.icontabbed.IconTabbedDock;
import ru.dmerkushov.javafx.faces.docks.test.TestDocksPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class TestIconTabbedDockModule extends FacesModule {

	public TestIconTabbedDockModule (Preferences modulePrefs) {
		super (modulePrefs);
	}

	TestDocksPanel panel1;
	TestDocksPanel panel2;
	TestDocksPanel panel3;

	@Override
	public void initAfterDependenciesLoaded () throws FacesException {
		panel1 = new TestDocksPanel ();
		panel2 = new TestDocksPanel ();
		panel3 = new TestDocksPanel ();

		UUID tabbedDockUuid = UUID.fromString ("aadd8246-0a76-485c-a02a-b57101d1ded1");

		IconTabbedDock dock = new IconTabbedDock (tabbedDockUuid, "My dock", "My dock tooltip", 200, 50, panel1, panel2);

		panel1.getView ().setOnMouseClicked ((e) -> {
			if (!dock.containsPanel (panel3)) {
				dock.addPanel (2, panel3);
			}
		});
		panel2.getView ().setOnMouseClicked ((e) -> {
			if (dock.containsPanel (panel3)) {
				dock.removePanel (panel3);
			}
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
