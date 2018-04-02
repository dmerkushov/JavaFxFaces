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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;
import ru.dmerkushov.javafx.faces.panels.FacesPanel;
import ru.dmerkushov.javafx.faces.panels.FacesPanels;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesMain extends Application {

	public static void main (String[] args) throws Exception {
		CommandLine.getInstance ().parseCommandLine (args);

		FacesConfiguration.configure ();
		FacesLogging.configure ();

		facesLoggerWrapper.info ("Configuration environment: " + CommandLine.getInstance ().configEnvName);

		Application.launch ();
	}

	private static FacesMain instance = null;

	private static void setInstance (FacesMain instance) {
		FacesMain.instance = instance;
	}

	public static FacesMain getInstanceWhenCreated () {
		return instance;
	}

	private FacesPanel mainPanel;
	private Stage primaryStage;
	private Scene mainScene;

	@Override
	public void start (Stage primaryStage) throws Exception {
		facesLoggerWrapper.entering ();

		setInstance (this);

		FacesModules.getInstance ().loadConfiguredModules ();

		mainPanel = FacesPanels.getInstance ().getPanel (FacesConfiguration.getMainPanelUuid ());
		if (mainPanel == null) {
			throw new FacesException ("Could not find the main panel: a panel with UUID " + FacesConfiguration.getMainPanelUuid ());
		}

		this.primaryStage = primaryStage;

		this.mainScene = new Scene (mainPanel.getView ());
		primaryStage.setScene (mainScene);
		primaryStage.setTitle (mainPanel.getDisplayName ());

		FacesUtil.bindWidthHeight (mainPanel.getView (), mainScene.widthProperty (), mainScene.heightProperty ());

		primaryStage.show ();

		facesLoggerWrapper.exiting ();
	}

	@Override
	public void stop () throws Exception {
		facesLoggerWrapper.entering ();

		FacesModules.getInstance ().finishModules ();

		facesLoggerWrapper.exiting ();
		super.stop ();
	}

	public FacesPanel getMainPanel () {
		return mainPanel;
	}

	public Stage getPrimaryStage () {
		return primaryStage;
	}

	public Scene getMainScene () {
		return mainScene;
	}

}
