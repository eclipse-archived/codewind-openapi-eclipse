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
package org.eclipse.codewind.openapi.test.server.python;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.server.AbstractServerGenerateTest;
import org.junit.Test;

public class PythonFlaskTest extends AbstractServerGenerateTest {

	public PythonFlaskTest() {
		super("python-flask");
		newProjectName = "PythonFlaskProject";
		targetOutputFolder = "/PythonFlaskProject";
		language = "Python";
		generatorType = "python-flask";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "abc/def/openapi.yaml";
	}

	@Test
    public void testPythonFlaskGenerator() {
    	super.doTest();
    }

	protected void verify() {
		super.verify();
		verifyGeneratedFile("Dockerfile");
		verifyGeneratedFile("openapi_server/encoder.py");
		verifyGeneratedFile("openapi_server/typing_utils.py");
		verifyGeneratedFile("openapi_server/util.py");
		verifyGeneratedFile("openapi_server/controllers/pets_controller.py");
		verifyGeneratedFile("openapi_server/controllers/security_controller_.py");
		verifyGeneratedFile("openapi_server/models/base_model_.py");
		verifyGeneratedFile("openapi_server/models/error.py");
		verifyGeneratedFile("openapi_server/models/pet.py");
		verifyGeneratedFile("openapi_server/openapi/openapi.yaml");
		verifyGeneratedFile("openapi_server/test/test_pets_controller.py");
		verifyGeneratedFile("README.md");
	}
}
