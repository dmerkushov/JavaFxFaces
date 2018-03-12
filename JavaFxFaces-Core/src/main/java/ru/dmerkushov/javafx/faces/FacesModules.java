/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.prefs.Preferences;
import static ru.dmerkushov.javafx.faces.FacesLogging.facesLoggerWrapper;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesModules {

	////////////////////////////////////////////////////////////////////////////
	// FacesModules is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static FacesModules _instance;

	/**
	 * Get the single instance of FacesModules
	 *
	 * @return The same instance of FacesModules every time the method is called
	 */
	public static synchronized FacesModules getInstance () {
		if (_instance == null) {
			_instance = new FacesModules ();
		}
		return _instance;
	}

	private FacesModules () {
	}
	////////////////////////////////////////////////////////////////////////////

	private final LinkedHashSet<Class<? extends FacesModule>> loadingModulesClasses = new LinkedHashSet<> ();
	private final LinkedHashMap<Class<? extends FacesModule>, FacesModule> loadedModules = new LinkedHashMap<> ();
	private final Object lock = new Object ();

	public void loadConfiguredModules () throws FacesException {
		facesLoggerWrapper.entering ();

		synchronized (lock) {
			for (String moduleClassName : FacesConfiguration.getModuleClassList ()) {
				loadModule (moduleClassName);
			}
		}

		facesLoggerWrapper.exiting ();
	}

	public FacesModule loadModule (String moduleClassName) throws FacesException {
		Objects.requireNonNull (moduleClassName, "moduleClassName");

		Class moduleClassGeneral;
		try {
			moduleClassGeneral = Class.forName (moduleClassName);
		} catch (ClassNotFoundException ex) {
			throw new FacesException ("Could not find module class " + moduleClassName + ", mentioned in config env " + CommandLine.getInstance ().configEnvName, ex);
		}

		if (!FacesModule.class.isAssignableFrom (moduleClassGeneral)) {
			throw new FacesException ("Module class " + moduleClassGeneral.getCanonicalName () + " is not an extension of FacesModule");
		}

		Class<FacesModule> moduleClass = (Class<FacesModule>) moduleClassGeneral;

		return loadModule (moduleClass);
	}

	public FacesModule loadModule (Class<? extends FacesModule> moduleClass) throws FacesException {
		Objects.requireNonNull (moduleClass, "moduleClass");
		facesLoggerWrapper.fine ("Loading module " + moduleClass.getCanonicalName ());

		FacesModule module;
		synchronized (lock) {
			if (loadedModules.containsKey (moduleClass)) {
				facesLoggerWrapper.fine ("Module " + moduleClass.getCanonicalName () + " already loaded");
				return loadedModules.get (moduleClass);
			}

			if (moduleClass.isInterface ()) {
				throw new FacesException ("The module class " + moduleClass.getCanonicalName () + " is an interface");
			}

			if (Modifier.isAbstract (moduleClass.getModifiers ())) {
				throw new FacesException ("The module class " + moduleClass.getCanonicalName () + " is abstract");
			}

			if (loadingModulesClasses.contains (moduleClass)) {	// The current loading classes list contains the current class, so a cycle dependency is found
				StringBuilder sb = new StringBuilder ();
				sb.append ("Cycle module dependency found for module class " + moduleClass.getCanonicalName () + ":\n");
				Iterator<Class<? extends FacesModule>> i = loadingModulesClasses.iterator ();
				Class<? extends FacesModule> prev = i.next ();
				while (i.hasNext ()) {
					Class<? extends FacesModule> curr = i.next ();
					sb.append ("\t" + prev.getCanonicalName () + " depends on " + curr.getCanonicalName () + "\n");
					prev = curr;
				}
				sb.append ("\t" + prev.getCanonicalName () + " depends on " + moduleClass.getCanonicalName ());
				throw new FacesException (sb.toString ());
			}
			loadingModulesClasses.add (moduleClass);

			Constructor<? extends FacesModule> moduleClassConstructor;
			try {
				moduleClassConstructor = moduleClass.getConstructor (Preferences.class);
			} catch (NoSuchMethodException ex) {
				throw new FacesException ("No public constructor with a single Preferences parameter found for module class " + moduleClass.getCanonicalName (), ex);
			} catch (SecurityException ex) {
				throw new FacesException (ex);
			}

			try {
				module = moduleClassConstructor.newInstance (FacesConfiguration.getPrefsForModule (moduleClass));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				throw new FacesException ("When creating an instance of module " + moduleClass.getCanonicalName (), ex);
			}

			Collection<Class<? extends FacesModule>> deps = module.getDependencies ();
			for (Class<? extends FacesModule> depModuleClass : deps) {
				if (depModuleClass == null) {
					continue;
				}

				facesLoggerWrapper.fine ("Loading dependency for module " + moduleClass.getCanonicalName () + ": " + depModuleClass.getCanonicalName ());
				loadModule (depModuleClass);
			}

			module.initAfterDependenciesLoaded ();

			loadingModulesClasses.remove (moduleClass);
			loadedModules.put (moduleClass, module);
		}

		facesLoggerWrapper.exiting (module);
		return module;
	}

	public Map<Class<? extends FacesModule>, FacesModule> getLoadedModulesCopy () {
		LinkedHashMap<Class<? extends FacesModule>, FacesModule> copy;
		synchronized (lock) {
			copy = new LinkedHashMap<> (loadedModules);
		}

		return copy;
	}

	void finishModules () {

		// Will try to finish modules in the reverse order to that they had been loaded, so the dependencies remain untouched
		ArrayList<FacesModule> modules;
		synchronized (lock) {
			modules = new ArrayList<> (loadedModules.values ());
		}
		for (int i = modules.size () - 1; i >= 0; i--) {
			FacesModule module = modules.get (i);
			facesLoggerWrapper.fine ("Finishing module " + module.getClass ().getCanonicalName ());

			try {
				module.finish ();
			} catch (Throwable ex) {
				facesLoggerWrapper.throwing ("Exception when finishing module " + module.getClass ().getCanonicalName (), ex);
			}
		}
	}

}
