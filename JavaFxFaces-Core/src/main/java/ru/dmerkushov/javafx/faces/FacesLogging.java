/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.javafx.faces;

import java.util.logging.Handler;
import ru.dmerkushov.loghelper.LogHelper;
import ru.dmerkushov.loghelper.LoggerWrapper;
import ru.dmerkushov.loghelper.formatter.DefaultFormatter;

/**
 *
 * @author Dmitriy Merkushov <d.merkushov@gmail.com>
 */
public class FacesLogging {

	public static final LoggerWrapper facesLoggerWrapper = LogHelper.getLoggerWrapper ("JavaFxFaces");

	static void configure () {
		facesLoggerWrapper.setLevel (FacesConfiguration.getLoggingLevel ());

		DefaultFormatter lf = new DefaultFormatter ();

		for (Handler handler : facesLoggerWrapper.getLogger ().getHandlers ()) {
			handler.setFormatter (lf);
			handler.setLevel (FacesConfiguration.getLoggingLevel ());
		}

		facesLoggerWrapper.configureByDefault (null);
	}

}
