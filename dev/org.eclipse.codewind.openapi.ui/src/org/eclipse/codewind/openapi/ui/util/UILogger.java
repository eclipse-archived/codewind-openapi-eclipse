/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.codewind.openapi.ui.util;

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugOptionsListener;

public class UILogger implements DebugOptionsListener {

	public static boolean ERROR = false;
	public static boolean WARNING = false;
	public static boolean INFO = false;

	// tracing levels
	public final static String ERROR_LEVEL = "/debug/error"; //$NON-NLS-1$
	public final static String WARNING_LEVEL = "/debug/warning"; //$NON-NLS-1$
	public final static String INFO_LEVEL = "/debug/info"; //$NON-NLS-1$

	private static UILogger logger;
	
	private UILogger() {
		logger = this;
	}
	
	public static UILogger instance() {
		if (logger == null) {
			logger = new UILogger();
		}
		return logger;
	}

	@Override
	public void optionsChanged(DebugOptions options) {
		UILogger.ERROR = options.getBooleanOption(Activator.PLUGIN_ID + UILogger.ERROR_LEVEL, false);
		UILogger.WARNING = options.getBooleanOption(Activator.PLUGIN_ID + UILogger.WARNING_LEVEL, false);
		UILogger.INFO = options.getBooleanOption(Activator.PLUGIN_ID + UILogger.INFO_LEVEL, false);
	}
}
