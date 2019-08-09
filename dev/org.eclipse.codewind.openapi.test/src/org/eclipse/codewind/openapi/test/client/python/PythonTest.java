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
package org.eclipse.codewind.openapi.test.client.python;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.client.AbstractClientGenerateTest;
import org.junit.Test;

public class PythonTest extends AbstractClientGenerateTest {

	public PythonTest() {
		super("python");
		newProjectName = "PythonProject";
		targetOutputFolder = "/PythonProject/folder";
		language = "Python";
		generatorType = "python";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "openapi.yaml";
	}

	@Test
	public void testPythonGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("docs/Error.md");
		verifyGeneratedFile("docs/Pet.md");
		verifyGeneratedFile("docs/PetsApi.md");
		verifyGeneratedFile("setup.py");
		verifyGeneratedFile("openapi_client/api_client.py");
		verifyGeneratedFile("openapi_client/configuration.py");
		verifyGeneratedFile("openapi_client/exceptions.py");
		verifyGeneratedFile("openapi_client/rest.py");
		verifyGeneratedFile("openapi_client/api/pets_api.py");
		verifyGeneratedFile("openapi_client/models/error.py");
		verifyGeneratedFile("openapi_client/models/pet.py");
		verifyGeneratedFile("test/test_error.py");
		verifyGeneratedFile("test/test_pet.py");
		verifyGeneratedFile("test/test_pets_api.py");
		verifyGeneratedFile("README.md");
	}
}
