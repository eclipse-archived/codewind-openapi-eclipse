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
package org.eclipse.codewind.openapi.core;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.codewind.openapi.core.util.CoreLogger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.codewind.openapi.core"; //$NON-NLS-1$
	
	private static BundleContext context;
	private static Activator plugin;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		plugin = this;
		context.registerService(DebugOptionsListener.class, CoreLogger.instance(), null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static Activator getDefault() {
		return plugin;
	}
	
	public static void log(int severity, String message) {
		switch (severity) {
			case IStatus.INFO:
				if (CoreLogger.INFO) {
					plugin.getLog().log(new Status(severity, PLUGIN_ID, message));
				}
				break;
			case IStatus.ERROR:
				if (CoreLogger.ERROR) {
					plugin.getLog().log(new Status(severity, PLUGIN_ID, message));					
				}
				break;
			case IStatus.WARNING:
				if (CoreLogger.WARNING) {
					plugin.getLog().log(new Status(severity, PLUGIN_ID, message));					
				}
				break;
			default:
		}
	}
	
	public static void log(int severity, Exception e) {
		if (CoreLogger.ERROR) {
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			e.printStackTrace(pw);
			plugin.getLog().log(new Status(severity, PLUGIN_ID, writer.toString()));				
		}
	}

}
