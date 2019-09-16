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

import org.eclipse.codewind.openapi.core.util.Util;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GenerateHtmlWizardPage extends AbstractGenerateWizardPage {

	private ISelection selection;

	public GenerateHtmlWizardPage(ISelection selection) {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.HTML_WIZARD_PAGE_TITLE);
		setDescription(Messages.HTML_WIZARD_PAGE_DESCRIPTION);
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		
		Label label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_PROJECT);

		projectText = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		projectText.setLayoutData(gd);
		projectText.setEnabled(true);
		projectText.addModifyListener(e -> dialogChanged());

		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_DEFINITION);

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(e -> dialogChanged());
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		
		Button button = new Button(container, SWT.PUSH);
		button.setText(Messages.WIZARD_PAGE_BROWSE_FILE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleFileBrowse();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_OUTPUT_FOLDER);
		label.setToolTipText(Messages.WIZARD_PAGE_OUTPUT_FOLDER_TOOLTIP);

		outputFolder = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		outputFolder.setLayoutData(gd);
		outputFolder.setToolTipText(Messages.WIZARD_PAGE_OUTPUT_FOLDER_TOOLTIP);
		outputFolder.addModifyListener(e -> dialogChanged());

		Button outputFolderBrowse = new Button(container, SWT.PUSH);
		outputFolderBrowse.setText(Messages.WIZARD_PAGE_BROWSE_FOLDER);
		outputFolderBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}
	
	protected void initialize() {
		fileText.setText("");
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IFile) {
				preselectedOpenApiFile = (IFile) obj;
				project = preselectedOpenApiFile.getProject();
				fileText.setText(preselectedOpenApiFile.getFullPath().toString());
			} else if (obj instanceof IContainer) {
				IContainer container = (IContainer) obj;
				project = container.getProject();
			} else {
				project = Util.getProject(obj);
			}
			if (project != null) {
				initWithDefinitionAtRoot();
				projectText.setText(project.getName());
				outputFolder.setText(project.getFullPath().toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	protected void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus(Messages.WIZARD_PAGE_PROJECT_NOT_PROVIDED);
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus(Messages.WIZARD_PAGE_PROJECT_NOT_IMPORTED);
			return;
		}
		if (!container.isAccessible()) {
			updateStatus(Messages.WIZARD_PAGE_PROJECT_NOT_ACCESSIBLE);
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(Messages.WIZARD_PAGE_SELECT_DEFINITION);
			return;
		}
		if (outputFolder.getText().length() == 0) {
			updateStatus(Messages.WIZARD_PAGE_SELECT_OUTPUT_FOLDER);
			return;				
		}		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	protected void populateGeneratorTypesCombo(String language) {
		// N/A
	}

	@Override
	protected void fillLanguagesCombo() {
		// N/A
	}

}