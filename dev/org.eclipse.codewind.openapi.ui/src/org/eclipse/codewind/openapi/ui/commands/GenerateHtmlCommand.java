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
package org.eclipse.codewind.openapi.ui.commands;

import java.util.List;

import org.eclipse.codewind.openapi.core.commands.CommandRunner;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

public class GenerateHtmlCommand extends AbstractOpenApiGeneratorCommand {

	public GenerateHtmlCommand() {
		// empty
	}

	public GenerateHtmlCommand(ISchedulingRule rule) {
		super(rule);
	}

	@Override
	protected CommandRunner getCommandRunner(List<String> mainArgsList) {
		return new CommandRunner(mainArgsList, "HtmlGenerator"); //$NON-NLS-1$
	}

	@Override
	protected void populateArgsList(List<String> mainArgsList) {
		mainArgsList.add("-jar"); //$NON-NLS-1$
		mainArgsList.add(codegenJar);
		mainArgsList.add("generate"); //$NON-NLS-1$
		mainArgsList.add("-g"); //$NON-NLS-1$
		mainArgsList.add("html2"); //$NON-NLS-1$
		mainArgsList.add("-i"); //$NON-NLS-1$
		if (this.openApiFile != null) {
			mainArgsList.add(argSurround + openApiFile.getLocation().toOSString() + argSurround);
		}
		// The output location
		mainArgsList.add("-o"); //$NON-NLS-1$
		if (outputFolder.getLocation() != null) {
			mainArgsList.add(argSurround + outputFolder.getLocation().toOSString() + argSurround);	
		} else {
			mainArgsList.add(argSurround + outputFolder.getFullPath().toOSString() + argSurround);
		}
	}
}
