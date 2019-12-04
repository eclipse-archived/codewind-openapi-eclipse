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
package org.eclipse.codewind.openapi.ui.commands;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.codewind.openapi.core.IOpenApiConstants;
import org.eclipse.codewind.openapi.core.commands.CommandRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public abstract class AbstractOpenApiGeneratorCommand extends WorkspaceModifyOperation {

	protected IFile openApiFile;	
	protected String outputFolderString;
	protected IFolder outputFolder = null;
	
	protected String codegenJar = null;
	protected String configJson = null;
	protected String argSurround = ""; //$NON-NLS-1$
	protected String pluginLocation = ""; //$NON-NLS-1$
	protected boolean isWindows = true;
	protected IProject project = null;

	protected String language = null;
	protected String generatorType = null;
	protected boolean pomFileExists = false;
	
	public AbstractOpenApiGeneratorCommand() {
		// empty
	}

	public AbstractOpenApiGeneratorCommand(ISchedulingRule rule) {
		super(rule);
	}

	public void setProject(IProject project) {
		this.project = project;
	}
	
	public void setOpenApiFile(IFile openApiFile) {
		this.openApiFile = openApiFile;
	}

	public void setOutputFolder(IFolder outputFolder) {
		this.outputFolder = outputFolder;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public void setGeneratorType(String generatorType) {
		this.generatorType = generatorType;
	}
	
	public void setOutputFolderString(String outputFolderString) {
		this.outputFolderString = outputFolderString;
	}
	
	public String getOutputFolderString() {
		return outputFolderString;
	}

	public IFolder getOutputFolder() {
		return outputFolder;
	}

	public boolean getPomFileExists() {
		return pomFileExists;
	}
	
	public void setPomFileExists(boolean exists) {
		this.pomFileExists = exists;
	}
	
	@Override
	protected void execute(IProgressMonitor monitor)
			throws CoreException, InvocationTargetException, InterruptedException {
		try {
			monitor.beginTask("Generating", 15);
			List<String> mainArgsList = new ArrayList<String>();		
			IStatus result = commonSetup(monitor);
			monitor.worked(5);
		    if (!result.isOK()) {
		    	Activator.log(IStatus.ERROR, "Failed to set up command");
		    	return;
		    }
		    monitor.worked(1);
			populateArgsList(mainArgsList);
			monitor.worked(4);
			result = doExecute(mainArgsList, monitor);
			monitor.worked(5);
		} finally {
			monitor.done();
		}
 	}

	protected abstract CommandRunner getCommandRunner(List<String> mainArgsList);
	protected abstract void populateArgsList(List<String> mainArgsList);
	
	protected IStatus commonSetup(IProgressMonitor monitor) {
		String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		isWindows = osName.indexOf("win") >= 0; //$NON-NLS-1$
		
		// only surround windows command line arguments with double quotes
		if (isWindows) {
			argSurround = "\""; //$NON-NLS-1$
		}
		
		try {
			URL coreURL = FileLocator.resolve(Activator.getDefault().getBundle().getEntry("/")); //$NON-NLS-1$
			pluginLocation = coreURL.getFile();
			if (isWindows && pluginLocation != null && pluginLocation.startsWith("/")) { //$NON-NLS-1$
				pluginLocation = pluginLocation.substring(1);
			}
		} catch (IOException e) {
			return new Status(IStatus.ERROR, org.eclipse.codewind.openapi.ui.Activator.PLUGIN_ID, e.getMessage());
		}
		
		String jarOverride = System.getProperty(IOpenApiConstants.OPENAPI_CLI_OVERRIDE_PROPERTY);
		// eg. -Dorg.eclipse.codewind.openapi.cli.jar.path=/Users/foo/openapi/openapi-generator-cli-4.0.0.jar
		// Allow users to override and provide their own codegen jar
		if (jarOverride != null && jarOverride.contains("openapi-generator-cli")) {
			codegenJar = argSurround + jarOverride + argSurround;
		} else {
			// else, use as default, the codegen jar that we bundle
			codegenJar = argSurround + pluginLocation + "lib/" + IOpenApiConstants.CODEGEN_JAR_401 + argSurround; //$NON-NLS-1$		
		}
		
		if (isWindows) {
			codegenJar = codegenJar.replace("/", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		if (outputFolderString.equals(project.getFullPath().toString())) {
			this.outputFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(project.getLocation());
		} else {
			this.outputFolder = (IFolder) ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(outputFolderString));
		}	    
	    return new Status(IStatus.OK, org.eclipse.codewind.openapi.ui.Activator.PLUGIN_ID, "Setup successful"); //$NON-NLS-1$	
	}
	
	protected IStatus doExecute(List<String> mainArgsList, IProgressMonitor monitor) {
		IStatus result = null;
		CommandRunner cmdRunner = getCommandRunner(mainArgsList);
		result = cmdRunner.run();
		
		if (result.isOK()) {
			try {
				if (outputFolderString.equals(project.getFullPath().toString())) {
					project.refreshLocal(IResource.DEPTH_INFINITE, monitor);	
				} else {
					outputFolder.refreshLocal(IResource.DEPTH_INFINITE, monitor);					
				}
			} catch (CoreException e) {
				result = new Status(IStatus.ERROR, org.eclipse.codewind.openapi.ui.Activator.PLUGIN_ID, e.getMessage());
			}
		}
		return result;		
	}
}
