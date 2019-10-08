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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.codewind.openapi.core.maven.Constants;
import org.eclipse.codewind.openapi.core.maven.MavenTracker;
import org.eclipse.codewind.openapi.core.maven.Utils;
import org.eclipse.codewind.openapi.ui.Activator;
import org.eclipse.codewind.openapi.ui.Constants.PROJECT_TYPE;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class PostGenCommand extends WorkspaceModifyOperation {

	private boolean ignoreFileExists;
	private String outputFolder;
	private IProject project;
	private PROJECT_TYPE projectType;
	private String tempOrigFileName;
	
	
	public PostGenCommand(IProject project, PROJECT_TYPE projectType, boolean ignoreFileExists, String outputFolder, String tempOrigFileName) {
		this.project = project;
		this.projectType = projectType;
		this.ignoreFileExists = ignoreFileExists;
		this.outputFolder = outputFolder;
		this.tempOrigFileName = tempOrigFileName;
	}

	@Override
	protected void execute(IProgressMonitor monitor)
			throws CoreException, InvocationTargetException, InterruptedException {
		postCodeGenForMaven(monitor);		
	}
	
	private boolean postCodeGenForMaven(IProgressMonitor monitor) {
		boolean isOutputFolderProjectRoot = outputFolder.equals(project.getFullPath().toString());
		// Post-code gen will only be done in the project if code gen was not done before (determined by the presence of the .openapi-generator-ignore file)
		if (projectType == PROJECT_TYPE.MAVEN && isOutputFolderProjectRoot && !ignoreFileExists) {
			try {
				IFile originalPomFile = project.getFile(tempOrigFileName);
				IFile generatedPomFile = project.getFile(Constants.POM_FILE_NAME); // After code gen, the pom.xml will be in the project root

				MavenTracker mt = new MavenTracker(originalPomFile, generatedPomFile, project);
				mt.mergeModels();

				monitor.beginTask(Messages.JOB_UPDATING_MAVEN_PROJECT, 100);

				Utils.updateProject(new IProject[] {project}, false, monitor);
				
				monitor.beginTask(Messages.JOB_UPDATING_CLASSPATH, 100);
				postCodeGenUpdateClasspath(monitor);

			} catch (Exception e) {
				Activator.log(IStatus.INFO, "Exception in postCodeGenForMaven", e); //$NON-NLS-1$
			}			
		}
		return true;
	}
	
	private void postCodeGenUpdateClasspath(IProgressMonitor monitor) {
		// Generator Type specific behaviour.  Java Only.
		IResource srcGenJavaPath = project.findMember(org.eclipse.codewind.openapi.core.maven.Constants.SRC_GEN_PATH);
		// Some generators generate the source into the src/gen/java path.  This source must be added to the Eclipse project's build path
		if (srcGenJavaPath != null && srcGenJavaPath.exists()) {
			addGenJavaSourcePathToClasspath(monitor);
		}
	}
	
	// For Maven Projects
	private void addGenJavaSourcePathToClasspath(IProgressMonitor monitor) {
		try {			
			IJavaProject javaProject = JavaCore.create(this.project);
		    IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
		    List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
		    boolean isAlreadyAdded = false;
		    for (int i = 0; i < rawClasspath.length; i++) {
		    	IClasspathEntry iClasspathEntry = rawClasspath[i];
		    	IPath cePath = iClasspathEntry.getPath();
		    	if (iClasspathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
		    		if (cePath.toString().endsWith(Constants.SRC_GEN_PATH)) {
		    			isAlreadyAdded = true;
		    		}
		    	}
				cpEntries.add(iClasspathEntry);
		    }
		    if (!isAlreadyAdded) {
			    IPath p = new Path(this.project.getName()).append(Constants.SRC_GEN_PATH).makeAbsolute();
			    List<IClasspathAttribute> attrs = new ArrayList<IClasspathAttribute>();
			    attrs.add(JavaCore.newClasspathAttribute("maven.pomderived", "true")); //$NON-NLS-1$ //$NON-NLS-2$
			    IClasspathEntry cpEntry = JavaCore.newSourceEntry(p, null, null, new Path(this.project.getName()).append("/target/classes").makeAbsolute(), (IClasspathAttribute [])attrs.toArray(new IClasspathAttribute[0])); //$NON-NLS-1$
			    cpEntries.add(cpEntry);		    	
		    }		    
		    javaProject.setRawClasspath((IClasspathEntry[])cpEntries.toArray(new IClasspathEntry[0]), monitor);
		} catch (Exception e) {
			Activator.log(IStatus.INFO, "addGenJavaSourcePathToClasspath issue: ", e); //$NON-NLS-1$
		}
	}
}
