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

public class GenerateServerWizardPage extends AbstractGenerateWizardPage {

	public GenerateServerWizardPage(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.SERVER_WIZARD_PAGE_TITLE);
		setDescription(Messages.SERVER_WIZARD_PAGE_DESCRIPTION);
		this.selection = selection;
	}

	@Override
	protected void populateGeneratorTypesCombo(String language) {
		generatorTypes.removeAll();
		// For specific Codewind overrides, populate the combo here.

		// Populate with all known generators
		for (int i = 0; i < Constants.ALL_SERVER_LANGUAGES.length; i++) {
			if (Constants.ALL_SERVER_LANGUAGES[i][0].equals(language)) {
				for (int j = 1; j < Constants.ALL_SERVER_LANGUAGES[i].length; j++) {
					generatorTypes.add(Constants.ALL_SERVER_LANGUAGES[i][j]);						
				}
				break;
			}
		}

		if (language == "Java") {
			// Spring
			if (isCodewindProject && this.codewindProjectTypeId.equals(Constants.SPRING_PROJECT_TYPE_ID)) {
				if (generatorTypes.indexOf(Constants.SPRING_SERVER) >= 0) {
					generatorTypes.remove(Constants.SPRING_SERVER);									
				}
				generatorTypes.add(Constants.SPRING_SERVER, 0); // Put it first
			}

			// For Liberty
			if (isCodewindProject && this.codewindProjectTypeId.equals(Constants.LIBERTY_PROJECT_TYPE_ID) || this.codewindProjectTypeId.equals(Constants.JAVA_DOCKER_PROJECT_TYPE_ID)) {
				int length = Constants.LIBERTY_AND_DOCKER_SERVER_GENERATORS.length;
				for (int i = 0; i < length; i++) {
					if (generatorTypes.indexOf(Constants.LIBERTY_AND_DOCKER_SERVER_GENERATORS[i]) >= 0) {
						generatorTypes.remove(Constants.LIBERTY_AND_DOCKER_SERVER_GENERATORS[i]);									
					}
				}
				for (int i = length - 1; i >= 0; i--) {
					generatorTypes.add(Constants.LIBERTY_AND_DOCKER_SERVER_GENERATORS[i], 0); // Put them at the top of the list						
				}
			}
		}
		if (generatorTypes.getItemCount() > 0) {
			generatorTypes.select(0);										
		}
	}

	@Override
	protected void fillLanguagesCombo() {
		languages.removeAll();
		for (int i = 0; i < Constants.ALL_SERVER_LANGUAGES.length; i++) {
			languages.add(Constants.ALL_SERVER_LANGUAGES[i][0]);
		}
	}
}