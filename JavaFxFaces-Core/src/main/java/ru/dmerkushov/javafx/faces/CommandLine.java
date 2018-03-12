/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
class CommandLine {

	////////////////////////////////////////////////////////////////////////////
	// CommandLine is a singleton class
	////////////////////////////////////////////////////////////////////////////
	private static CommandLine _instance;

	/**
	 * Get the single instance of CommandLine
	 *
	 * @return The same instance of CommandLine every time the method is called
	 */
	public static synchronized CommandLine getInstance () {
		if (_instance == null) {
			_instance = new CommandLine ();
		}
		return _instance;
	}

	private CommandLine () {
	}
	////////////////////////////////////////////////////////////////////////////

	@Option (name = "-e", aliases = {"--environment"}, required = false, usage = "preferences (java prefs) environment name")
	String configEnvName = "javafxfaces";

	public boolean parseCommandLine (String... args) {
		CmdLineParser parser = new CmdLineParser (this);

		try {
			parser.parseArgument (args);
		} catch (CmdLineException ex) {
			System.err.println (ex.getMessage ());
			parser.printUsage (System.err);
			return false;
		}

		return true;
	}
}
