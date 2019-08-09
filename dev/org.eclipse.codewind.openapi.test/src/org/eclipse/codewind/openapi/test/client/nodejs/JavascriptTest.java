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
package org.eclipse.codewind.openapi.test.client.nodejs;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.client.AbstractClientGenerateTest;
import org.junit.Test;

public class JavascriptTest extends AbstractClientGenerateTest {

	public JavascriptTest() {
		super("javascript");
		newProjectName = "JsProject";
		targetOutputFolder = "/JsProject";
		language = "Node.js";
		generatorType = "javascript";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "def1/def2/jsopenapi.yaml";
	}

	@Test
	public void testJavascriptGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("docs/Error.md");
		verifyGeneratedFile("docs/Pet.md");
		verifyGeneratedFile("docs/PetsApi.md");
		verifyGeneratedFile("package.json");
		verifyGeneratedFile("src/api/PetsApi.js");
		verifyGeneratedFile("src/ApiClient.js");
		verifyGeneratedFile("src/model/Error.js");
		verifyGeneratedFile("src/model/Pet.js");
		verifyGeneratedFile("test/api/PetsApi.spec.js");
		verifyGeneratedFile("test/model/Error.spec.js");
		verifyGeneratedFile("test/model/Pet.spec.js");
	}
}
