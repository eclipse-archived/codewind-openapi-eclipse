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
package org.eclipse.codewind.openapi.test;

import org.eclipse.codewind.openapi.test.client.go.GoTest;
import org.eclipse.codewind.openapi.test.client.java.JavaTest;
import org.eclipse.codewind.openapi.test.client.java.JaxrsCxfClientTest;
import org.eclipse.codewind.openapi.test.client.nodejs.JavascriptTest;
import org.eclipse.codewind.openapi.test.client.nodejs.TypescriptNodeTest;
import org.eclipse.codewind.openapi.test.client.python.PythonTest;
import org.eclipse.codewind.openapi.test.client.swift.Swift3Test;
import org.eclipse.codewind.openapi.test.client.swift.Swift4Test;
import org.eclipse.codewind.openapi.test.html.Html2GeneratorTest;
import org.eclipse.codewind.openapi.test.server.go.GoGinServerTest;
import org.eclipse.codewind.openapi.test.server.go.GoServerTest;
import org.eclipse.codewind.openapi.test.server.java.JaxrsCxfTest;
import org.eclipse.codewind.openapi.test.server.java.JaxrsJerseyTest;
import org.eclipse.codewind.openapi.test.server.java.JaxrsRestEasyTest;
import org.eclipse.codewind.openapi.test.server.java.JaxrsSpecTest;
import org.eclipse.codewind.openapi.test.server.java.SpringTest;
import org.eclipse.codewind.openapi.test.server.nodejs.NodeJsServerTest;
import org.eclipse.codewind.openapi.test.server.python.PythonFlaskTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	GoTest.class,
	JaxrsCxfClientTest.class,
	JavaTest.class,
	JavascriptTest.class,
	TypescriptNodeTest.class,
	PythonTest.class,
	Swift3Test.class,
	Swift4Test.class,
	GoGinServerTest.class,
	GoServerTest.class,
	JaxrsSpecTest.class,
	JaxrsCxfTest.class,
	JaxrsJerseyTest.class,
	JaxrsRestEasyTest.class,
	SpringTest.class,
	NodeJsServerTest.class,
	PythonFlaskTest.class,
	Html2GeneratorTest.class
})
public class AllTests {

	public AllTests() {
		// empty
	}
}
