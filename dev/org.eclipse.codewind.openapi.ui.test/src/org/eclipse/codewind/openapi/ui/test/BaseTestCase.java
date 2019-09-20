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
package org.eclipse.codewind.openapi.ui.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.util.TestUtilities;
import org.eclipse.codewind.openapi.ui.test.menus.utils.UITestUtilities;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

public class BaseTestCase extends SWTBotTestCase {

	protected SWTWorkbenchBot swtWorkbenchBot = new SWTWorkbenchBot();
	
	protected IProject testProject;
	protected IFile definitionFile;
	
	// Configurable options to test and should be overridden
	protected String newProjectName = "TestProject";
	protected String sourceDefinition = Constants.PETSTORE_30;
	protected String targetDefinitionInProject = "petstore.yaml";
	
	public BaseTestCase() {
		// Empty
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

	protected void setup() {
		createGeneralProject(this.newProjectName);
		copyDefinitionToProject(this.sourceDefinition, this.targetDefinitionInProject);
	}
	
	protected void doTestInProjectExplorer(boolean openApiMenuShouldAppear) {
		SWTBotView view = UITestUtilities.getProjectExplorerView();
		StringTokenizer token = new StringTokenizer(targetDefinitionInProject, "/");
		Stack<String> pathSegments = new Stack<String>();
		while (token.hasMoreElements()) {
			pathSegments.add(0, token.nextToken());
		}
		verifyContextMenu(openApiMenuShouldAppear, view, newProjectName, pathSegments);
	}
	
	// Accept first child node of root node
	protected void verifyContextMenu(boolean expectedOpenApiMenuExists, SWTBotView view, String rootNode, Stack<String> pathSegments) {
		SWTBotTreeItem targetTreeItem = UITestUtilities.findTreeItem(rootNode, view, pathSegments);
		verifyContextMenu(expectedOpenApiMenuExists, targetTreeItem);
	}

	// Accept a path to the child node relative to the root node
	protected void verifyContextMenu(boolean expectedOpenApiMenuExists, SWTBotView view, String rootNode, String childNode) {
		SWTBotTreeItem targetTreeItem = UITestUtilities.findTreeItem(rootNode, view, childNode);
		verifyContextMenu(expectedOpenApiMenuExists, targetTreeItem);
	}
	
	private void verifyContextMenu(boolean expectedOpenApiMenuExists, SWTBotTreeItem targetTreeItem) {
		assertNotNull("The test file should be in the project and the tree node exists", targetTreeItem);
		try {
			SWTBotMenu contextMenu = targetTreeItem.contextMenu(UIConstants.MENU_OPENAPI_GENERATE);
			if (expectedOpenApiMenuExists) {
				assertNotNull("OpenAPI Generate menu should exist", contextMenu);			
				List<String> cascadeMenuItems = contextMenu.menuItems();
				for (String s: cascadeMenuItems) {
					System.out.println(s);
				}
				assertTrue("There should be three menu actions", cascadeMenuItems.size() == 3);
				assertTrue("Client API stub... menu exists", cascadeMenuItems.get(0).equals(UIConstants.MENU_GEN_CLIENT));
				assertTrue("Server API stub... menu exists", cascadeMenuItems.get(1).equals(UIConstants.MENU_GEN_SERVER));
				assertTrue("HTML documentation...", cascadeMenuItems.get(2).equals(UIConstants.MENU_HTML));
			}
		} catch (Exception e) {
			if (!expectedOpenApiMenuExists) {
				assertTrue("OpenAPI Generate menu should not be available on non-OpenAPI 3.0 documents", e instanceof WidgetNotFoundException);
				return;
			}
			throw e; // Fail test if test otherwise
		}
	}
}
