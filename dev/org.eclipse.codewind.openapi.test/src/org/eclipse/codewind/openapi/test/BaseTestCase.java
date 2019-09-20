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
package org.eclipse.codewind.openapi.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import org.eclipse.codewind.openapi.test.util.TestUtilities;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import junit.framework.TestCase;

public class BaseTestCase extends TestCase {

	protected IProject testProject;
	protected IContainer outputFolder;
	protected IFile definitionFile;

	// Configurable options to test and should be overridden
	protected String newProjectName = "TestProject";
	protected String sourceDefinition = Constants.PETSTORE_30;
	protected String targetDefinitionInProject = "petstore.yaml";

	public BaseTestCase() {
		// Empty
	}

	public BaseTestCase(String name) {
		super(name);
	}

	protected void createGeneralProject(String projectName) {
		try {
			this.testProject = TestUtilities.createGeneralProject(projectName);
		} catch (CoreException e) {
			fail();
		}
		assertNotNull("Project creation", this.testProject);
		assertEquals("Test project name", projectName, this.testProject.getName());
	}
	
	/**
	 * 
	 * @param sourceFile - source OpenAPI definition in the test plugin resources folder
	 * @param destinationFile - path of target OpenAPI definition in the test project but excludes 
	 *                          the project name
	 */
	protected void copyDefinitionToProject(String sourceFile, String destinationFile) {
		try {
			this.definitionFile = TestUtilities.copyDefinitionToProject(sourceFile, destinationFile, this.testProject);			
			assertTrue("Copied file exists in workspace", this.definitionFile.exists());
			assertEquals("Definition is in the test project", this.testProject, this.definitionFile.getProject()); 
		} catch (CoreException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	protected void setOutputFolder(String folderName) {		
		IProgressMonitor monitor = new NullProgressMonitor();
		try {
			if (folderName.equals(this.testProject.getFullPath().toString())) {
			    this.outputFolder = this.testProject;
			} else {
				StringTokenizer st = new StringTokenizer(folderName, "/");
				StringBuffer sb = new StringBuffer(testProject.getFullPath().toString());
				IFolder aFolder = null;
				while (st.hasMoreTokens()) {
					String nextToken = st.nextToken();
					if (!nextToken.equals(this.testProject.getName())) {
						aFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(sb.toString() + "/" + nextToken));
						aFolder.create(true, true, monitor);
						sb.append("/" + nextToken);
					}
				}
				this.testProject.refreshLocal(IProject.DEPTH_INFINITE, monitor);
				this.outputFolder = aFolder;
				assertTrue("New folder created", this.outputFolder.exists());				
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	protected void verifyGeneratedFile(String generatedFile) {
		IResource aFile = this.outputFolder.findMember(generatedFile);
		assertNotNull(generatedFile + " file null test", aFile);
		assertTrue(generatedFile + " file exists test", aFile.exists());
	}
}
