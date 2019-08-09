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
package org.eclipse.codewind.openapi.test.client.java;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.client.AbstractClientGenerateTest;
import org.junit.Test;

public class JavaTest extends AbstractClientGenerateTest {

	public JavaTest() {
		super("java");
		newProjectName = "JavaProject";
		targetOutputFolder = "/JavaProject/a/b";
		language = "Java";
		generatorType = "java";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "openapi.yaml";
	}

	@Test
	public void testJavaGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("docs/Error.md");
		verifyGeneratedFile("docs/Pet.md");
		verifyGeneratedFile("docs/PetsApi.md");
		verifyGeneratedFile("pom.xml");
		verifyGeneratedFile("src");
		verifyGeneratedFile("src/main/java/org/openapitools/client/api/PetsApi.java");
		verifyGeneratedFile("src/main/java/org/openapitools/client/ApiCallback.java");
		verifyGeneratedFile("src/main/java/org/openapitools/client/ApiClient.java");
		verifyGeneratedFile("src/main/java/org/openapitools/client/auth");
		verifyGeneratedFile("src/main/java/org/openapitools/client/model/Error.java");
		verifyGeneratedFile("src/main/java/org/openapitools/client/model/Pet.java");
	}
}
