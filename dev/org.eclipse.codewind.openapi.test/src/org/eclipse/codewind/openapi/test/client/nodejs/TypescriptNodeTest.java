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

public class TypescriptNodeTest extends AbstractClientGenerateTest {

	public TypescriptNodeTest() {
		super("typescript-node");
		newProjectName = "TsProject";
		targetOutputFolder = "/TsProject/output";
		language = "Node.js";
		generatorType = "typescript-node";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "tsopenapi.yaml";
	}

	@Test
	public void testTypescriptNodeGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("api/apis.ts");
		verifyGeneratedFile("api/petsApi.ts");
		verifyGeneratedFile("api.ts");
		verifyGeneratedFile("model/modelError.ts");
		verifyGeneratedFile("model/models.ts");
		verifyGeneratedFile("model/pet.ts");
	}
}
