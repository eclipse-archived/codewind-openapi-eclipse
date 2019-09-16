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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.core.runtime.NullProgressMonitor;

public abstract class AbstractGenerateTest extends BaseTestCase {

	// Configurable options to test and should be overridden
	protected String newProjectName = "TestProject";
	protected String targetOutputFolder = "/TestProject";
	protected String language = "Java";
	protected String generatorType = "java";
	protected String sourceDefinition = Constants.PETSTORE_30;
	protected String targetDefinitionInProject = "petstore.yaml";
	
	public AbstractGenerateTest() {
		// Empty
	}

	public AbstractGenerateTest(String name) {
		super(name);
	}
	
	protected abstract AbstractOpenApiGeneratorCommand getCommand();


	protected void doTest() {
		createGeneralProject(this.newProjectName);
		setOutputFolder(this.targetOutputFolder);
		copyDefinitionToProject(this.sourceDefinition, this.targetDefinitionInProject);

		// --- Initialize the command ---------
		AbstractOpenApiGeneratorCommand cmd = getCommand();
		cmd.setProject(this.testProject);
		cmd.setLanguage(this.language);					
		cmd.setGeneratorType(this.generatorType);
		cmd.setOutputFolderString(this.outputFolder.getFullPath().toString());
		cmd.setOpenApiFile(this.definitionFile);
		// -------------------------------------
		
		try {
			cmd.run(new NullProgressMonitor());
			assertTrue("Output folder exists", this.outputFolder.exists());				
			
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		verify();
	}

	protected void verify() {
		verifyGeneratedFile(".openapi-generator-ignore");
	}

}
