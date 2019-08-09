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
package org.eclipse.codewind.openapi.test.client.go;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.client.AbstractClientGenerateTest;
import org.junit.Test;

public class GoTest extends AbstractClientGenerateTest {

	public GoTest() {
		super("go");
		newProjectName = "GoProject";
		targetOutputFolder = "/GoProject/client";
		language = "Go";
		generatorType = "go";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
	}

	@Test
	public void testGoGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("api/openapi.yaml");
		verifyGeneratedFile("api_pets.go");
		verifyGeneratedFile("client.go");
		verifyGeneratedFile("configuration.go");
		verifyGeneratedFile("docs/Error.md");
		verifyGeneratedFile("docs/Pet.md");
		verifyGeneratedFile("docs/PetsApi.md");
		verifyGeneratedFile("model_error.go");
		verifyGeneratedFile("model_pet.go");
		verifyGeneratedFile("response.go");
	}
}
