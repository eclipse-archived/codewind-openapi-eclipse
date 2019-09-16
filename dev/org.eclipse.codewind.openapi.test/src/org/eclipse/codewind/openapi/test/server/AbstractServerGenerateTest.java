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
package org.eclipse.codewind.openapi.test.server;

import org.eclipse.codewind.openapi.test.AbstractGenerateTest;
import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.codewind.openapi.ui.commands.GenerateServerCommand;

public abstract class AbstractServerGenerateTest extends AbstractGenerateTest {

	public AbstractServerGenerateTest(String name) {
		super(name);
	}

	@Override
	protected AbstractOpenApiGeneratorCommand getCommand() {
		return new GenerateServerCommand();
	}
}
