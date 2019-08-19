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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public abstract class AbstractGenerateWizardPage extends WizardPage {

	protected Text projectText;
	protected Text fileText;
	protected Text outputFolder;

	protected ISelection selection;
	
	protected IProject project;

	protected IFile preselectedOpenApiFile;

	protected Combo generatorTypes;
	protected Combo languages;
	
	protected boolean isCodewindProject = false;
	protected String codewindProjectLanguage = ""; //$NON-NLS-1$
	
	public AbstractGenerateWizardPage(String pageName) {
		super(pageName);
	}

	public AbstractGenerateWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public String getContainerName() {
		return projectText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getGeneratorType() {
		return generatorTypes.getText();
	}

	public IFile getSelectedOpenApiFile() {
		return preselectedOpenApiFile;
	}
	
	public String getOutputFolder() {
		if (this.outputFolder.getText().equals(".")) { //$NON-NLS-1$
			return project.getFullPath().toString();
		} else {
			return this.outputFolder.getText();
		}
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
		projectText.addModifyListener(e -> dialogChanged(e));

		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_DEFINITION);

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(e -> dialogChanged(e));
		
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
		outputFolder.addModifyListener(e -> dialogChanged(e));

		Button outputFolderBrowse = new Button(container, SWT.PUSH);
		outputFolderBrowse.setText(Messages.WIZARD_PAGE_BROWSE_FOLDER);
		outputFolderBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_LANGUAGE);

		languages = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		languages.setLayoutData(gd);
		languages.addModifyListener(e -> dialogChanged(e));
		
		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_GENERATOR_TYPE);
		label.setToolTipText(Messages.WIZARD_PAGE_GENERATOR_TYPE_TOOLTIP);

		generatorTypes = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		generatorTypes.setToolTipText(Messages.WIZARD_PAGE_GENERATOR_TYPE_TOOLTIP);
		generatorTypes.setLayoutData(gd);
		generatorTypes.addModifyListener(e -> dialogChanged(e));

		initialize();
		dialogChanged(null);
		setControl(container);
	}
	
	protected abstract void populateGeneratorTypesCombo(String language);
	protected abstract void fillLanguagesCombo();
	
	protected void initialize() {
		fileText.setText(""); //$NON-NLS-1$
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
			} else if (obj instanceof IJavaProject) {
				IJavaProject javaProj = (IJavaProject)obj;
				project = javaProj.getProject();
			} else {
				String possibleLanguage = Util.getProjectLanguage(obj);
				if (possibleLanguage.length() > 0) {
					codewindProjectLanguage = possibleLanguage;
				}
				project = Util.getProject(obj);
			}
			if (project != null) {
				initWithDefinitionAtRoot(); //$NON-NLS-1$
				projectText.setText(project.getName());
			}
		}		
		if (project != null) {
			isCodewindProject = Util.isCodewindProject(project);
			if (isCodewindProject) {
				if (codewindProjectLanguage.length() == 0) {
					codewindProjectLanguage = Util.getProjectLanguage(project);
				}
				if (codewindProjectLanguage.length() > 0) {
					switch(codewindProjectLanguage) {
	                	case "go": //$NON-NLS-1$
	                    case "Go": //$NON-NLS-1$
	                    	codewindProjectLanguage = "Go"; //$NON-NLS-1$
							populateGeneratorTypesCombo("Go"); //$NON-NLS-1$
	                        break;
						case "java": //$NON-NLS-1$
						case "Java": //$NON-NLS-1$
	                    	codewindProjectLanguage = "Java"; //$NON-NLS-1$
							populateGeneratorTypesCombo("Java"); //$NON-NLS-1$
							break;
	                    case "nodejs": //$NON-NLS-1$
	                    case "Node.js": //$NON-NLS-1$
	                    	codewindProjectLanguage = "Node.js"; //$NON-NLS-1$
							populateGeneratorTypesCombo("Node.js"); //$NON-NLS-1$
	                        break;
	                    case "swift": //$NON-NLS-1$
	                    case "Swift": //$NON-NLS-1$
	                    	codewindProjectLanguage = "Swift"; //$NON-NLS-1$
	                    	populateGeneratorTypesCombo("Swift"); //$NON-NLS-1$
	                        break;
	                    case "python": //$NON-NLS-1$
	                    case "Python": //$NON-NLS-1$
	                    	codewindProjectLanguage = "Python"; //$NON-NLS-1$
	                    	populateGeneratorTypesCombo("Python"); //$NON-NLS-1$
	                        break;
						default: {
							
						}
					}
					languages.add(codewindProjectLanguage);
					languages.setText(codewindProjectLanguage);
					languages.select(0);
				} else {
					fillLanguagesCombo();  // Language undefined in codewind project
				}
			} else {
				fillLanguagesCombo(); // Regular project in the workspace
			}
			if ("swift".equals(codewindProjectLanguage) || "Swift".equals(codewindProjectLanguage)) { //$NON-NLS-1$ //$NON-NLS-2$
				outputFolder.setText(project.getFullPath().toString() + "/Sources"); //$NON-NLS-1$
			} else {
				outputFolder.setText(project.getFullPath().toString());					
			}
		}
	}
	
	/**
	 * Ensures that both text fields are set.
	 */

	protected void dialogChanged(ModifyEvent e) {
		if (e != null) {
			if (e.getSource() == languages) {
				languages.getText();
				populateGeneratorTypesCombo(languages.getText());
			}
		}
		updateStatus(null);
		setPageComplete(true);
		IResource container = project;
		String fileName = getFileName();

		if (projectText.getText().length() == 0) {
			updateStatus(Messages.WIZARD_PAGE_PROJECT_NOT_PROVIDED);
			return;
		}
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
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
		if (languages.getText().length() == 0 && languages.getSelectionIndex() < 0) {
			addInfoStatus(Messages.WIZARD_PAGE_SELECT_LANGUAGE);
			return;
		}
		if (generatorTypes.getText().length() == 0 && generatorTypes.getSelectionIndex() < 0) {
			addInfoStatus(Messages.WIZARD_PAGE_SELECT_GENERATOR_TYPE);
			return;
		}
		updateStatus(null);
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	private void addInfoStatus(String message) {
		setErrorMessage(message);
		setPageComplete(false);
	}

	protected void handleFileBrowse() {
		ElementTreeSelectionDialog ed = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		ed.setInput(project);
		ed.setAllowMultiple(false);
		ed.setMessage(Messages.BROWSE_DIALOG_MESSAGE_SELECT_DEFINITION);
		ed.addFilter(new ViewerFilter() {			
			@Override
			public boolean select(Viewer arg0, Object arg1, Object resource) {
				if (resource instanceof IFile) {
					IFile f = (IFile) resource;
					String fileName = f.getName();
					if ((fileName.endsWith(".yaml") || fileName.endsWith(".yml") || fileName.endsWith(".json")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						&& !fileName.startsWith(".")
						&& !fileName.toLowerCase().equals("package.json")
						&& !fileName.toLowerCase().equals("package-lock.json")
						&& !fileName.toLowerCase().equals("chart.yaml")
						&& !fileName.toLowerCase().equals("nodemon.json")
						&& !fileName.toLowerCase().equals("manifest.yml")
						&& !fileName.toLowerCase().equals("devfile.yaml")) {
						return true;
					}
				} else {
					return true; // Show folders
				}
				return false;
			}
		});
		ed.setTitle(Messages.BROWSE_DIALOG_TITLE_SELECT_DEFINITION);
		if (ed.open() == ElementTreeSelectionDialog.OK) {
			Object[] result = ed.getResult();
			if (result.length == 1) {
				preselectedOpenApiFile = (IFile)result[0];
				fileText.setText(preselectedOpenApiFile.getFullPath().toString());
			}
		}
	}

	protected void handleBrowse() {
		
		ElementTreeSelectionDialog ed = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
		ed.setInput(project);
		ed.setAllowMultiple(false);
		ed.setMessage(Messages.BROWSE_DIALOG_MESSAGE_SELECT_FOLDER);
		ed.setTitle(Messages.BROWSE_DIALOG_TITLE_SELECT_FOLDER);
		String out = outputFolder.getText();
		if (out.startsWith(project.getFullPath().toString())) {
			String s = outputFolder.getText().substring(project.getFullPath().toString().length());
			ed.setInitialSelection(project.findMember(s));
		}		
		ed.addFilter(new ViewerFilter() {			
			@Override
			public boolean select(Viewer arg0, Object arg1, Object resource) {
				if (resource instanceof IFile) {
					return false;
				}
				return true;
			}
		});
		ed.setEmptyListMessage(Messages.BROWSE_DIALOG_NO_CHILD_FOLDERS);
		if (ed.open() == ElementTreeSelectionDialog.OK) {
			Object[] result = ed.getResult();
			if (result.length == 1) {
				outputFolder.setText(((IContainer)result[0]).getFullPath().toString());
			} else {
				outputFolder.setText(project.getFullPath().toString());
			}
		}
	}
	
	protected void initWithDefinitionAtRoot() {
		boolean isFound = checkForDefinition("openapi.yaml");
		if (!isFound) {
			isFound = checkForDefinition("openapi.yml");
			if (!isFound) {
				isFound = checkForDefinition("openapi.json");
			}
		}
	}
	
	protected boolean checkForDefinition(String fileName) {
		IResource res = project.findMember(fileName);
		if (res != null && res.exists() && res instanceof IFile && res.isAccessible()) {
			preselectedOpenApiFile = (IFile)res;
			fileText.setText(preselectedOpenApiFile.getFullPath().toString());
			return true;
		}
		return false;
	}

	
	public boolean doFinish(IProgressMonitor monitor) {
		return false;
	}
}
