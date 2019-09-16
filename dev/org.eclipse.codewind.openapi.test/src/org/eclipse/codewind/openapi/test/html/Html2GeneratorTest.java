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
package org.eclipse.codewind.openapi.test.html;

import org.eclipse.codewind.openapi.test.Constants;
import org.junit.Test;

public class Html2GeneratorTest extends AbstractHtmlGeneratorTest {

	public Html2GeneratorTest() {
		super("Definition at root of project");
	}

	@Test
    public void testDefinitionAtRoot() {
		newProjectName = "HtmlProject01";
		targetOutputFolder = "/HtmlProject01";
		language = null;
		generatorType = null;
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "openapi.yaml";
    	super.doTest();
    }
	
	@Test
    public void testDefinitionInSubFolder() {
		newProjectName = "HtmlProject02";
		targetOutputFolder = "/HtmlProject02";
		language = null;
		generatorType = null;
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "f1/f2/openapi.yaml";
		super.doTest();
    }
	
	@Test
    public void testDifferentDefinitionName() {
		newProjectName = "HtmlProject03";
		targetOutputFolder = "/HtmlProject03";
		language = null;
		generatorType = null;
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "abcdefghijklmnopqrstuvwxyz.yaml";
		super.doTest();
    }

	@Test
    public void testOutputSubFolder() {
		newProjectName = "HtmlProject04";
		targetOutputFolder = "/HtmlProject04/o1/o2/o3/o4/o5";
		language = null;
		generatorType = null;
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "petstore.yaml";
		super.doTest();
    }

	@Test
    public void testAllInSubFolder() {
		newProjectName = "HtmlProject05";
		targetOutputFolder = "/HtmlProject05/o1/o2/o3/o4/o5/o6";
		language = null;
		generatorType = null;
		sourceDefinition = Constants.PETSTORE_30;
		targetDefinitionInProject = "d1/d2/d3/d4/d5/d6/d7/petstore.yaml";
		super.doTest();
    }
}
