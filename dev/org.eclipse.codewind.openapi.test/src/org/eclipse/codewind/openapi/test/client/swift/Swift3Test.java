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
package org.eclipse.codewind.openapi.test.client.swift;

import org.eclipse.codewind.openapi.test.Constants;
import org.eclipse.codewind.openapi.test.client.AbstractClientGenerateTest;
import org.junit.Test;

public class Swift3Test extends AbstractClientGenerateTest {

	public Swift3Test() {
		super("swift3-deprecated");
		newProjectName = "Swift3Proj";
		targetOutputFolder = "/Swift3Proj/folder";
		language = "Swift";
		generatorType = "swift3-deprecated";
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "swift3openapi.yaml";
	}

	@Test
	public void testSwift3DeprecatedGenerator() {
		super.doTest();
	}

	protected void verify() {
		super.verify();
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/AlamofireImplementations.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/APIHelper.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/APIs/PetsAPI.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/APIs.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/Configuration.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/Extensions.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/Models/ModelError.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/Models/Pet.swift");
		verifyGeneratedFile("OpenAPIClient/Classes/OpenAPIs/Models.swift");
	}
}
