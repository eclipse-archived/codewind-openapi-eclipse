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

import org.eclipse.codewind.openapi.ui.Constants;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.jface.viewers.ISelection;

public class GenerateClientWizardPage extends AbstractGenerateWizardPage {

	public GenerateClientWizardPage(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.CLIENT_WIZARD_PAGE_TITLE);
		setDescription(Messages.CLIENT_WIZARD_PAGE_DESCRIPTION);
		this.selection = selection;
	}
	
	@Override
	protected void populateGeneratorTypesCombo(String language) {
		generatorTypes.removeAll();
		for (int i = 0; i < Constants.ALL_CLIENT_LANGUAGES.length; i++) {
			if (Constants.ALL_CLIENT_LANGUAGES[i][0].equals(language)) {
				for (int j = 1; j < Constants.ALL_CLIENT_LANGUAGES[i].length; j++) {
					generatorTypes.add(Constants.ALL_CLIENT_LANGUAGES[i][j]);						
				}
				if (generatorTypes.getItemCount() > 0) {
					generatorTypes.select(0);										
				}
				break;
			}
		}
	}
	
	@Override
	protected void fillLanguagesCombo() {
		languages.removeAll();
		for (int i = 0; i < Constants.ALL_CLIENT_LANGUAGES.length; i++) {
			languages.add(Constants.ALL_CLIENT_LANGUAGES[i][0]);
		}
	}
}