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
package org.eclipse.codewind.openapi.ui.test.wizard;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.ui.test.BaseTestCase;
import org.eclipse.codewind.openapi.ui.test.UIConstants;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WizardTest extends BaseTestCase {

	public WizardTest() {
	}
	
	@BeforeClass
	public static void beforeClass() {
		SWTWorkbenchBot swtWorkbenchBot = new SWTWorkbenchBot();
		try
		{
			swtWorkbenchBot.viewByTitle("Welcome").close();
		} catch (Exception e) {
			
		}
	}

	@Test
	public void testWorkingYAMLPHPCLient01() {
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		this.newProjectName = "ClientStubPHPProject";
		setup();
		testOpenAPIWizardHappyPath(UIConstants.MENU_GEN_CLIENT, "PHP","composer.json");
	}
	
	@Test
	public void testWorkingYAMLNodeJSServer02() {
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		this.newProjectName = "ServerStubNodeProject";
		setup();
		testOpenAPIWizardHappyPath(UIConstants.MENU_GEN_SERVER, "Node.js","package.json");
	}
	
	@Test
	public void testWorkingYAMLHTML03() {
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		this.newProjectName = "genHtmlProject";
		setup();
		testOpenAPIWizardHappyPath(UIConstants.MENU_HTML, null,"index.html");
	}
	

	
	@After
	public void deleteTestProject() {
		IProgressMonitor monitor = new NullProgressMonitor();
		this.testProject = ResourcesPlugin.getWorkspace().getRoot().getProject(this.newProjectName);
		try {
			this.testProject.delete(true, monitor);
		} catch (CoreException e) {
		}
		assertFalse("Project deletion", testProject.exists());
	}
}
