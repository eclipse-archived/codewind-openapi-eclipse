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
package org.eclipse.codewind.openapi.core.maven;

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2e.core.ui.internal.UpdateMavenProjectJob;

@SuppressWarnings("restriction")
public class Utils {

	public Utils() {
		// Empty
	}
	
	public static boolean isMavenProject(IProject project) throws CoreException{
        try {
            return project.hasNature(Constants.MAVEN_PROJECT_NATURE);
        } catch (CoreException e) {
        	Activator.log(IStatus.INFO, "Error when checking for Maven project nature"); //$NON-NLS-1$
        	throw e;
        }
	}

	public static boolean hasJavaNature(IProject project) throws CoreException{
        try {
            return project.hasNature(JavaCore.NATURE_ID);
        } catch (CoreException e) {
        	Activator.log(IStatus.INFO, "Error when checking for Maven project nature"); //$NON-NLS-1$
        	throw e;
        }
	}

	/**
	 * Update Maven Project
	 * If not scheduled, this must be called within the context of a job, otherwise, a new job will be scheduled.
	 * 
	 * @param projects
	 * @param monitor
	 */
	public static void updateProject(IProject[] projects, boolean doSchedule, IProgressMonitor monitor) {
		// TODO: this Job is internal - this is the only workaround to do an Update Maven project
		if (!doSchedule) {
			new UpdateMavenProjectJob(projects).run(monitor);
		} else {
			new UpdateMavenProjectJob(projects).schedule();
		}
	}
	
	public static boolean isSupportedMavenProject(IProject project) {
        try {
            // Check if it's a maven project first
            if (!isMavenProject(project)) {
                return false;
            }
            IFile pom = project.getFile(Constants.POM_FILE_NAME);
            if (pom == null || !pom.exists()) {
            	Activator.log(IStatus.INFO, "Project has Maven nature but the pom.xml file does not exist"); //$NON-NLS-1$
                return false;
            }
        } catch (CoreException e) {
        	return false;
        }
        return true;
	}
}
