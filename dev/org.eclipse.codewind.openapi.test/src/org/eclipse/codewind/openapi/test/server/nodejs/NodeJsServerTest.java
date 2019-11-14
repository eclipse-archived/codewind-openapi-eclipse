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
package org.eclipse.codewind.openapi.test.server.nodejs;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.server.AbstractServerGenerateTest;
import org.junit.Test;

public class NodeJsServerTest extends AbstractServerGenerateTest {

	public NodeJsServerTest() {
		super("nodejs-server");
		newProjectName = "NodejsServerProject";
		targetOutputFolder = "/NodejsServerProject";
		language = "Node.js";
		generatorType = "nodejs-express-server";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "a/b/c/mypetstore.yaml";
	}

	@Test
    public void testNodejsServerGenerator() {
    	super.doTest();
    }

	protected void verify() {
		super.verify();
		verifyGeneratedFile("package.json");
		verifyGeneratedFile("README.md");
		verifyGeneratedFile("index.js");
		verifyGeneratedFile("api/openapi.yaml");
		verifyGeneratedFile("controllers/PetsController.js");
		verifyGeneratedFile("services/PetsService.js");
		verifyGeneratedFile("utils/openapiRouter.js");
	}
}
