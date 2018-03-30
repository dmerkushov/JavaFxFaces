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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import ru.dmerkushov.javafx.faces.dummy.DummyModule;
import ru.dmerkushov.prefconf.PrefConf;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesConfiguration {

	private static Preferences userPrefs = Preferences.userNodeForPackage (FacesConfiguration.class);
	private static Preferences systemPrefs = Preferences.systemNodeForPackage (FacesConfiguration.class);

	static void configure () throws FacesException {
		userPrefs = PrefConf.getInstance ().getUserConfigurationForEnvironment (FacesConfiguration.class.getPackage (), CommandLine.getInstance ().configEnvName);
		systemPrefs = PrefConf.getInstance ().getSystemConfigurationForEnvironment (FacesConfiguration.class.getPackage (), CommandLine.getInstance ().configEnvName);
	}

	private static UUID mainPanelUuid;

	public static synchronized UUID getMainPanelUuid () {
		if (mainPanelUuid == null) {
			mainPanelUuid = UUID.fromString (FacesConfiguration.systemPrefs.get ("mainPanelUuid", "8e2bf236-81d5-4f6e-813d-6118b5467bbf"));
		}
		return mainPanelUuid;
	}

	private static List<String> moduleClassList;

	public static synchronized List<String> getModuleClassList () {
		if (moduleClassList == null) {
			String moduleListStr = FacesConfiguration.systemPrefs.get ("moduleList", DummyModule.class.getCanonicalName ());
			moduleClassList = Arrays.asList (moduleListStr.split (" "));
		}
		return moduleClassList;
	}

	public static synchronized Preferences getUserPrefsForModule (Class<? extends FacesModule> moduleClass) {
		return userPrefs.node ("modules/" + moduleClass.getCanonicalName ().replaceAll ("\\.", "/"));
	}

	public static synchronized Preferences getSystemPrefsForModule (Class<? extends FacesModule> moduleClass) {
		return systemPrefs.node ("modules/" + moduleClass.getCanonicalName ().replaceAll ("\\.", "/"));
	}

	private static Level loggingLevel;

	public static synchronized Level getLoggingLevel () {
		if (loggingLevel == null) {
			try {
				loggingLevel = Level.parse (FacesConfiguration.systemPrefs.get ("loggingLevel", "FINE"));
			} catch (IllegalArgumentException | NullPointerException ex) {
				loggingLevel = Level.FINE;
			}
		}
		return loggingLevel;
	}

}
