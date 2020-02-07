/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
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
package org.eclipse.codewind.openapi.ui.commands;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.codewind.openapi.core.commands.CommandRunner;
import org.eclipse.codewind.openapi.ui.Activator;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

public class GenerateServerCommand extends AbstractOpenApiGeneratorCommand {

	public GenerateServerCommand() {
		// empty
	}

	public GenerateServerCommand(ISchedulingRule rule) {
		super(rule);
	}

	@Override
	protected CommandRunner getCommandRunner(List<String> mainArgsList) {
		return new CommandRunner(mainArgsList, "ServerGenerator"); //$NON-NLS-1$
	}

	@Override
	protected void populateArgsList(List<String> mainArgsList) {
		mainArgsList.add("-jar"); //$NON-NLS-1$
		mainArgsList.add(codegenJar);
		mainArgsList.add("generate"); //$NON-NLS-1$
		mainArgsList.add("-g"); //$NON-NLS-1$
		mainArgsList.add(generatorType);
		
		boolean isLiberty = false;
		boolean isAppsodyLiberty = false;
        
		IResource serverProjectFile = project.findMember("/src/main/liberty/config/server.xml"); //$NON-NLS-1$
		if (serverProjectFile != null) {
			isLiberty = serverProjectFile.exists();
			
			IResource appsodyProjectFile = project.findMember(".appsody-config.yaml"); //$NON-NLS-1$
			
			if (appsodyProjectFile != null) {
				isAppsodyLiberty = isLiberty && appsodyProjectFile.exists();
			}	
		}
		
		if (generatorType.equals("jaxrs-spec") && (codewindProjectTypeId != null && codewindProjectTypeId.toLowerCase().indexOf("liberty") >= 0) //$NON-NLS-1$ //$NON-NLS-2$ 
				|| (codewindProjectTypeId != null && codewindProjectTypeId.toLowerCase().indexOf("docker") >= 0 && isLiberty) //$NON-NLS-1$ 
				|| (codewindProjectTypeId != null && codewindProjectTypeId.toLowerCase().indexOf("appsodyextension") >= 0 && isAppsodyLiberty)) { //$NON-NLS-1$
			mainArgsList.add("--library"); //$NON-NLS-1$
			mainArgsList.add("openliberty"); //$NON-NLS-1$
			mainArgsList.add("--enable-post-process-file"); //$NON-NLS-1$
		}
		
		mainArgsList.add("-i"); //$NON-NLS-1$
		if (this.openApiFile != null) {
			mainArgsList.add(argSurround + openApiFile.getLocation().toOSString() + argSurround);
		}
		// The output location
		mainArgsList.add("-o"); //$NON-NLS-1$
		if (outputFolder.getLocation() != null) {
			mainArgsList.add(argSurround + outputFolder.getLocation().toOSString() + argSurround);	
		} else {
			mainArgsList.add(argSurround + outputFolder.getFullPath().toOSString() + argSurround);
		}
		if (generatorType.equals("spring")) {
			try {
				mainArgsList.add("-t");
				URL resource = org.eclipse.codewind.openapi.core.Activator.getDefault().getBundle().getEntry("templates/JavaSpring401");
				URL fileURL = FileLocator.toFileURL(resource);
				String path = fileURL.getFile();
				if (isWindows && path != null && path.startsWith("/")) {
					path = path.substring(1);
				}
				mainArgsList.add(argSurround + path + argSurround);
				if (pomFileExists) {
					mainArgsList.add("--additional-properties");
					mainArgsList.add("mavenPomExists=true");					
				}
			} catch (IOException e) {
				Activator.log(IStatus.ERROR, e);
			}
		}
		Activator.log(IStatus.INFO, mainArgsList.toString());
	}
}