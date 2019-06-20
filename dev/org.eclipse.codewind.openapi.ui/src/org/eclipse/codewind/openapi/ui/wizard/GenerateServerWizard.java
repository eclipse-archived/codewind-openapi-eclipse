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
package org.eclipse.codewind.openapi.ui.wizard;

import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.codewind.openapi.ui.commands.GenerateServerCommand;
import org.eclipse.core.resources.IProject;

public class GenerateServerWizard extends AbstractGenerateWizard {

	public GenerateServerWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	protected AbstractOpenApiGeneratorCommand getCommand() {
		GenerateServerCommand cmd = new GenerateServerCommand();
		IProject project = page.getProject();
		cmd.setProject(project);
		cmd.setGeneratorType(page.getGeneratorType());
		cmd.setOpenApiFile(page.getSelectedOpenApiFile());
		cmd.setOutputFolderString(page.getOutputFolder());
		return cmd;
	}
}
