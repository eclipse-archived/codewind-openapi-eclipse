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
package org.eclipse.codewind.openapi.ui.test.menus;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.ui.test.BaseTestCase;
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
public class ContextMenuTest extends BaseTestCase {

	public ContextMenuTest() {
		this.newProjectName = "TestProject";
	}
	
	@BeforeClass
	public static void beforeClass() {
		SWTWorkbenchBot swtWorkbenchBot = new SWTWorkbenchBot();
		swtWorkbenchBot.viewByTitle("Welcome").close();
	}

	@Test
	public void testContextMenu01() {
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		setup();
		doTestInProjectExplorer(true);
	}

	@Test
	public void testContextMenu02() {
		sourceDefinition = Constants.GEN_JSON_30;
		targetDefinitionInProject = Constants.GEN_JSON_30;		
		setup();
		doTestInProjectExplorer(true);
	}

	@Test
	public void testContextMenu03() {
		sourceDefinition = Constants.GEN_YAML_30;
		targetDefinitionInProject = Constants.GEN_YAML_30;
		setup();
		doTestInProjectExplorer(true);
	}

	@Test
	public void testContextMenu04() {
		sourceDefinition = Constants.GEN_YML_30;
		targetDefinitionInProject = Constants.GEN_YML_30;
		setup();
		doTestInProjectExplorer(true);
	}

	@Test
	public void testContextMenu05() {
		sourceDefinition = Constants.PETSTORE_OAS_30;
		targetDefinitionInProject = Constants.PETSTORE_OAS_30;
		setup();
		doTestInProjectExplorer(true);
	}
	
	@Test
	public void testContextMenu06() {
		sourceDefinition = Constants.PETSTORE_JSON_30;
		targetDefinitionInProject = Constants.PETSTORE_JSON_30;
		setup();
		doTestInProjectExplorer(true);
	}

	@Test
	public void testContextMenu07() {
		sourceDefinition = Constants.SWAGGER_JSON_30;
		targetDefinitionInProject = Constants.SWAGGER_JSON_30;
		setup();
		doTestInProjectExplorer(true);
	}
	
	// Non OpenAPI 3.0 documents including Swagger 2.0 documents

	@Test
	public void testForNoContextMenu01() {
		sourceDefinition = Constants.SWAGGER_JSON_20;
		targetDefinitionInProject = Constants.SWAGGER_JSON_20;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu02() {
		sourceDefinition = Constants.SWAGGER_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER_YAML_20;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu03() {
		sourceDefinition = Constants.SWAGGER001_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER001_YAML_20;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu04() {
		sourceDefinition = Constants.SWAGGER002_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER002_YAML_20;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu05() {
		sourceDefinition = Constants.ANY_JSON_FILE;
		targetDefinitionInProject = Constants.ANY_JSON_FILE;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu06() {
		sourceDefinition = Constants.ANY_YAML_FILE;
		targetDefinitionInProject = Constants.ANY_YAML_FILE;
		setup();
		doTestInProjectExplorer(false);
	}

	@Test
	public void testForNoContextMenu07() {
		sourceDefinition = Constants.REGULAR_JSON_FILE;
		targetDefinitionInProject = Constants.REGULAR_JSON_FILE;
		setup();
		doTestInProjectExplorer(false);
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
