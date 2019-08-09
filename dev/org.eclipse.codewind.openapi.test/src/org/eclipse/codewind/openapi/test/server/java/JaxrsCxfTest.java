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

public class JaxrsCxfTest extends AbstractServerGenerateTest {

	public JaxrsCxfTest() {
		super("jaxrs-cxf");
		newProjectName = "JaxrsCxfServerProject";
		targetOutputFolder = "/JaxrsCxfServerProject/server";
		language = "Java";
		generatorType = "jaxrs-cxf";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "openapi-petstore.yaml";
	}

	@Test
    public void testJaxrsCxfGenerator() {
    	super.doTest();
    }
	
	protected void verify() {
		super.verify();
		verifyGeneratedFile("pom.xml");
		verifyGeneratedFile("src");
		verifyGeneratedFile("src/gen/java/org/openapitools/api/PetsApi.java");
		verifyGeneratedFile("src/gen/java/org/openapitools/model/Error.java");
		verifyGeneratedFile("src/gen/java/org/openapitools/model/Pet.java");
		verifyGeneratedFile("src/main/java/org/openapitools/api/impl/PetsApiServiceImpl.java");
		verifyGeneratedFile("src/test/java/org/openapitools/api/PetsApiTest.java");
	}
}
