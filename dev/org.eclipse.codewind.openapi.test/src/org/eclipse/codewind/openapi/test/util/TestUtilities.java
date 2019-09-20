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
package org.eclipse.codewind.openapi.test.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

import org.eclipse.codewind.openapi.test.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;

public class TestUtilities {

	public TestUtilities() {
		// Empty
	}

	/**
	 * Create a General Project
	 * 
	 * @param projectName
	 * @return
	 * @throws CoreException
	 */
	public static IProject createGeneralProject(String projectName) throws CoreException {		
		IProgressMonitor monitor = new NullProgressMonitor();
		IProject testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		try {
			testProject.create(monitor);
			testProject.open(monitor);
		} catch (CoreException e) {
			throw e;
		}
		return testProject;
	}
	
	/**
	 * 
	 * @param sourceFile - source OpenAPI definition in the test plugin resources folder
	 * @param destinationFile - path of target OpenAPI definition in the test project but excludes 
	 *                          the project name
	 */
	public static IFile copyDefinitionToProject(String sourceFile, String destinationFile, IProject testProject) throws CoreException, FileNotFoundException, IOException {
		NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
		try {
			URL url = Activator.getDefault().getBundle().getEntry(sourceFile);
			URL fileURL = FileLocator.toFileURL(url);
			
			StringTokenizer st = new StringTokenizer(destinationFile, "/");
			StringBuffer sb = new StringBuffer();
			IFolder aFolder = null;
			while (st.hasMoreTokens()) {
				String nextToken = st.nextToken();
				if (!st.hasMoreTokens()) {
					break;
				}
				aFolder = testProject.getFolder(new Path(sb.toString() + "/" + nextToken));
				aFolder.create(true, true, nullProgressMonitor);
				sb.append("/" + nextToken);
			}
			IFile destFile = testProject.getFile(destinationFile);			
			InputStream in = new BufferedInputStream(new FileInputStream(fileURL.getFile()));
			destFile.create(in,  true, nullProgressMonitor);

			testProject.refreshLocal(IProject.DEPTH_ONE, nullProgressMonitor);
			IFile defFile =  testProject.getFile(destinationFile);
			Assert.assertTrue("Copied file exists in workspace", defFile.exists());
			Assert.assertEquals("Definition is in the test project", testProject, defFile.getProject()); 
			return defFile;
		} catch (CoreException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
}
