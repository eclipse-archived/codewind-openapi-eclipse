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

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.codewind.openapi.ui.Constants;
import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.codewind.openapi.ui.commands.GenerateHtmlCommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class GenerateHtmlWizard extends AbstractGenerateWizard {

	private GenerateHtmlCommand cmd;

	public GenerateHtmlWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	protected AbstractOpenApiGeneratorCommand getCommand() {
		cmd = new GenerateHtmlCommand();
		IProject project = page.getProject();
		cmd.setProject(project);
		cmd.setGeneratorType(null);
		cmd.setOpenApiFile(page.getSelectedOpenApiFile());
		cmd.setOutputFolderString(page.getOutputFolder());
		return cmd;
	}

	@Override
	public boolean performFinish() {
		boolean rc = super.performFinish();
		if (rc) {
			openInEditor();
		}
		return rc;
	}
	
	private void openInEditor() {
		String outputFolderString = cmd.getOutputFolderString();
		IResource htmlFile = ResourcesPlugin.getWorkspace().getRoot().findMember(outputFolderString + "/" + Constants.HTML_DOCUMENTATION_FILE);
		if (htmlFile != null && htmlFile instanceof IFile && htmlFile.exists()) {
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput((IFile)htmlFile), "org.eclipse.ui.browser.editor");
			} catch (PartInitException e) {
				Activator.log(IStatus.INFO, e);
			}
		}
	}
}
