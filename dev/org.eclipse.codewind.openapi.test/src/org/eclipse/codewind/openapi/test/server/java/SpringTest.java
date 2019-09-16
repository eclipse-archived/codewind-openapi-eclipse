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
package org.eclipse.codewind.openapi.test.server.java;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.server.AbstractServerGenerateTest;
import org.junit.Test;

public class SpringTest extends AbstractServerGenerateTest {

	public SpringTest() {
		super("spring");
		newProjectName = "SpringProject";
		targetOutputFolder = "/SpringProject";
		language = "Java";
		generatorType = "spring";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "openapi.yaml";
	}

	@Test
    public void testSpringGenerator() {
    	super.doTest();
    }
	
	protected void verify() {
		super.verify();
		verifyGeneratedFile("pom.xml");
		verifyGeneratedFile("src");
		verifyGeneratedFile("src/main/java/org/openapitools/api/ApiUtil.java");
		verifyGeneratedFile("src/main/java/org/openapitools/api/PetsApi.java");
		verifyGeneratedFile("src/main/java/org/openapitools/api/PetsApiController.java");
		verifyGeneratedFile("src/main/java/org/openapitools/configuration/HomeController.java");
		verifyGeneratedFile("src/main/java/org/openapitools/configuration/OpenAPIDocumentationConfig.java");
		verifyGeneratedFile("src/main/java/org/openapitools/model/Error.java");
		verifyGeneratedFile("src/main/java/org/openapitools/model/Pet.java");
		verifyGeneratedFile("src/main/java/org/openapitools/OpenAPI2SpringBoot.java");
		verifyGeneratedFile("src/main/java/org/openapitools/RFC3339DateFormat.java");
		verifyGeneratedFile("src/main/resources/application.properties");
	}
}
