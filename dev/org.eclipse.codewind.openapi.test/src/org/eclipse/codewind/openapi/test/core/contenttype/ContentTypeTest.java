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
package org.eclipse.codewind.openapi.test.core.contenttype;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentTypeTest extends AbstractContentTypeTest {

	private String expectedContentTypeId = null;
	
	private static String WST_JSON_CONTENT_TYPE = "org.eclipse.wst.json.core.jsonsource";
	
	
	public ContentTypeTest() {
		super("ContentTypeTest");
		this.newProjectName = "TestProject";
	}
	
	// Valid 3.0 documents
	
	@Test
	public void testContentType01() {
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		expectedContentTypeId = Constants.CONTENTTYPE_YAML;
		super.doTest();
	}

	@Test
	public void testContentType02() {
		sourceDefinition = Constants.GEN_JSON_30;
		targetDefinitionInProject = Constants.GEN_JSON_30;
		expectedContentTypeId = Constants.CONTENTTYPE_JSON;

		super.doTest();
	}

	@Test
	public void testContentType03() {
		sourceDefinition = Constants.GEN_YAML_30;
		targetDefinitionInProject = Constants.GEN_YAML_30;
		expectedContentTypeId = Constants.CONTENTTYPE_YAML;
		super.doTest();
	}
	
	@Test
	public void testContentType04() {
		sourceDefinition = Constants.GEN_YML_30;
		targetDefinitionInProject = Constants.GEN_YML_30;
		expectedContentTypeId = Constants.CONTENTTYPE_YAML;
		super.doTest();
	}

	@Test
	public void testContentType05() {
		sourceDefinition = Constants.PETSTORE_OAS_30;
		targetDefinitionInProject = Constants.PETSTORE_OAS_30;
		expectedContentTypeId = Constants.CONTENTTYPE_YAML;
		super.doTest();
	}

	@Test
	public void testContentType06() {
		sourceDefinition = Constants.PETSTORE_JSON_30;
		targetDefinitionInProject = Constants.PETSTORE_JSON_30;
		expectedContentTypeId = Constants.CONTENTTYPE_JSON;
		super.doTest();
	}

	@Test
	public void testContentType07() {
		sourceDefinition = Constants.SWAGGER_JSON_30;
		targetDefinitionInProject = Constants.SWAGGER_JSON_30;
		expectedContentTypeId = Constants.CONTENTTYPE_JSON;
		super.doTest();
	}

	// Non OpenAPI 3.0 documents including Swagger 2.0 documents
	
	@Test
	public void testOther01() {
		sourceDefinition = Constants.SWAGGER_JSON_20;
		targetDefinitionInProject = Constants.SWAGGER_JSON_20;
		expectedContentTypeId = WST_JSON_CONTENT_TYPE; // Default JSON content type (Not OpenAPI)
		super.doTest();
	}

	@Test
	public void testOther02() {
		sourceDefinition = Constants.SWAGGER_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER_YAML_20;
		expectedContentTypeId = null;  // No YAML content type
		super.doTest();
	}
	
	@Test
	public void testOther03() {
		sourceDefinition = Constants.SWAGGER001_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER001_YAML_20;
		expectedContentTypeId = null;  // No YAML content type
		super.doTest();
	}

	@Test
	public void testOther04() {
		sourceDefinition = Constants.SWAGGER002_YAML_20;
		targetDefinitionInProject = Constants.SWAGGER002_YAML_20;
		expectedContentTypeId = null;  // No YAML content type
		super.doTest();
	}

	@Test
	public void testOther05() {
		sourceDefinition = Constants.ANY_JSON_FILE;
		targetDefinitionInProject = Constants.ANY_JSON_FILE;
		expectedContentTypeId = WST_JSON_CONTENT_TYPE; // Default JSON content type (Not OpenAPI)
		super.doTest();
	}

	@Test
	public void testOther06() {
		sourceDefinition = Constants.ANY_YAML_FILE;
		targetDefinitionInProject = Constants.ANY_YAML_FILE;
		expectedContentTypeId = null;
		super.doTest();
	}

	@Test
	public void testOther07() {
		sourceDefinition = Constants.REGULAR_JSON_FILE;
		targetDefinitionInProject = Constants.REGULAR_JSON_FILE;
		expectedContentTypeId = WST_JSON_CONTENT_TYPE; // Default JSON content type (Not OpenAPI)
		super.doTest();
	}

	@Override
	protected void verify() {
		verifyContentType(targetDefinitionInProject, expectedContentTypeId);
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
