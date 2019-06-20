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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public abstract class AbstractGenerateWizard extends Wizard implements INewWizard {
	protected AbstractGenerateWizardPage page;
	protected ISelection selection;
	
	public AbstractGenerateWizard() {
		// Empty
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	protected abstract AbstractOpenApiGeneratorCommand getCommand();
	
	@Override
	public void addPage(IWizardPage p) {
		this.page = (AbstractGenerateWizardPage)p;
		super.addPage(page);
	}

	@Override
	public boolean performFinish() {
		AbstractOpenApiGeneratorCommand cmd = getCommand();
		IProject project = page.getProject();
		IResource ignoreFile;
		if (page.getOutputFolder().equals(project.getFullPath().toString())) {
			ignoreFile = project.findMember(".openapi-generator-ignore"); //$NON-NLS-1$
		} else {
			IFolder outputFolder = (IFolder) ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(page.getOutputFolder()));
			ignoreFile = outputFolder.findMember(".openapi-generator-ignore"); //$NON-NLS-1$

		}		
		if (ignoreFile != null && ignoreFile.exists()) {
        	boolean openConfirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.INFO_FILES_EXIST_TITLE, Messages.INFO_FILES_EXIST_DESCRIPTION);
        	if (!openConfirm) {
        		return false;
        	}
		}
		try {
			getContainer().run(true, false, cmd);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			realException.printStackTrace();
			return false;
		}
		return true;
	}

}
