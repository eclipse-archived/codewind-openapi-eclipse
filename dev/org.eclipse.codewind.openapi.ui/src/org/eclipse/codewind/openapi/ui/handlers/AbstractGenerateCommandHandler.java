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
package org.eclipse.codewind.openapi.ui.handlers;

import java.lang.reflect.Field;

import org.eclipse.codewind.openapi.ui.Activator;
import org.eclipse.codewind.openapi.ui.Constants;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractGenerateCommandHandler extends AbstractHandler {

	public AbstractGenerateCommandHandler() {
		super();
	}
	
	protected abstract IWizardPage getWizardPage(ISelection currentSelection);
	protected abstract INewWizard getWizard(ISelection currentSelection);

	protected IProject getIProject(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		String projectName = null;
		if (selection instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) selection;
			Object obj = ts.getFirstElement();
			Class<?> clazz = obj.getClass();
			try {
				Field field = clazz.getField("name");
				Object object = field.get(obj);
				if (object instanceof String) {
					projectName = (String)object;
				}
			} catch (Exception e) {
				Activator.log(IStatus.INFO, e.getMessage());
			}
		}
		if (projectName != null) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}
		return null;
	}
	
	protected Object invokeWizard(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		String activePartId = HandlerUtil.getActivePartId(event);
		if (activePartId.equals(Constants.CODEWIND_EXPLORER_VIEW)) { //$NON-NLS-1$
			IProject codewindProject = getIProject(event); // Enable in extension prevents this anyway
			if (codewindProject == null || !codewindProject.exists() && activePartId.equals(Constants.CODEWIND_EXPLORER_VIEW)) {
				MessageDialog.openInformation(
				window.getShell(),
				Messages.INFO_OPENAPI_TITLE,
				Messages.INFO_PROJECT_NOT_IMPORTED);
				return null;
			}
			// If in the codewind explorer view, set the selection to the model object
			if (currentSelection instanceof TreeSelection) {
				TreeSelection ts = (TreeSelection) currentSelection;
				currentSelection = new StructuredSelection(ts.getFirstElement());				
			}
		}
		if (currentSelection == null) {
			MessageDialog.openInformation(
				window.getShell(),
				Messages.INFO_OPENAPI_TITLE,
				Messages.INFO_INVALID_SELECTION);
				return null;
		}
		INewWizard wiz = getWizard(currentSelection);
		WizardDialog wizardDialog = new WizardDialog(window.getShell(), wiz);
		wizardDialog.open();	
		return null;
	}
}
