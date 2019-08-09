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
package org.eclipse.codewind.openapi.test.server.go;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.server.AbstractServerGenerateTest;
import org.junit.Test;

public class GoServerTest extends AbstractServerGenerateTest {

	public GoServerTest() {
		super("go-server");	
		newProjectName = "GoServerProject";
		targetOutputFolder = "/GoServerProject/f1";
		language = "Go";
		generatorType = "go-server";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
	}

	@Test
    public void testGoServerGenerator() {
    	super.doTest();
    }
	
	protected void verify() {
		super.verify();
		verifyGeneratedFile("api/openapi.yaml");
		verifyGeneratedFile("Dockerfile");
		verifyGeneratedFile("go/api_pets.go");
		verifyGeneratedFile("go/logger.go");
		verifyGeneratedFile("go/model_error.go");
		verifyGeneratedFile("go/model_pet.go");
		verifyGeneratedFile("go/routers.go");
		verifyGeneratedFile("main.go");
		verifyGeneratedFile("README.md");
	}
}
