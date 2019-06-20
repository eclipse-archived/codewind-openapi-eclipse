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

import org.eclipse.codewind.openapi.ui.wizard.GenerateClientWizard;
import org.eclipse.codewind.openapi.ui.wizard.GenerateClientWizardPage;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;

public class GenerateClientCommandHandler extends AbstractGenerateCommandHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return invokeWizard(event);
	}

	@Override
	protected IWizardPage getWizardPage(ISelection currentSelection) {
		return new GenerateClientWizardPage(currentSelection);		
	}

	@Override
	protected INewWizard getWizard(ISelection currentSelection) {
		GenerateClientWizard wiz = new GenerateClientWizard();
		wiz.addPage(getWizardPage(currentSelection));
		return wiz;
	}
}
