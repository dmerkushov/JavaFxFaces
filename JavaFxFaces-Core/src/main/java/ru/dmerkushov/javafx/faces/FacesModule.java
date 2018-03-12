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

import java.util.Collection;
import java.util.Objects;
import java.util.prefs.Preferences;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public abstract class FacesModule {

	protected Preferences modulePreferences;

	public FacesModule (Preferences modulePreferences) {
		Objects.requireNonNull (modulePreferences, "modulePreferences");

		this.modulePreferences = modulePreferences;
	}

	public abstract void initAfterDependenciesLoaded () throws FacesException;

	public abstract Collection<Class<? extends FacesModule>> getDependencies () throws FacesException;

	public abstract void finish () throws FacesException;

}
