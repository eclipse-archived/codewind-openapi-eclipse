/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
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

import java.util.StringTokenizer;

import org.eclipse.codewind.openapi.core.IOpenApiConstants;
import org.eclipse.codewind.openapi.core.maven.Constants;
import org.eclipse.codewind.openapi.core.maven.Utils;
import org.eclipse.codewind.openapi.core.util.Util;
import org.eclipse.codewind.openapi.ui.Activator;
import org.eclipse.codewind.openapi.ui.Constants.PROJECT_TYPE;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.FileEditorInput;

public abstract class AbstractGenerateWizardPage extends WizardPage {

	protected Text projectText;
	protected Text fileText;
	protected Text outputFolder;
	private PROJECT_TYPE projectType;
	protected String tempOrigFileName;

	protected ISelection selection;
	
	protected IProject project;

	protected IFile preselectedOpenApiFile;

	protected Combo generatorTypes;
	protected Combo languages;
	
	protected boolean isCodewindProject = false;
	protected String codewindProjectLanguage = ""; //$NON-NLS-1$
	protected String codewindProjectTypeId = ""; //$NON-NLS-1$
	protected String initialOutputFolder = null;
	
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
	
	public PROJECT_TYPE getProjectType() {
		return projectType;
	}
	
	public String getPomFileBackupName() {
		return tempOrigFileName;
	}

	public String getCodewindProjectTypeId() {
		return codewindProjectTypeId;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		
		// Project type determines what we need to show
		determineProjectType();
		
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
		
		label = new Label(container, SWT.NULL);
		label.setText(Messages.WIZARD_PAGE_GENERATOR_TYPE);
		label.setToolTipText(Messages.WIZARD_PAGE_GENERATOR_TYPE_TOOLTIP);

		generatorTypes = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		generatorTypes.setToolTipText(Messages.WIZARD_PAGE_GENERATOR_TYPE_TOOLTIP);
		generatorTypes.setLayoutData(gd);

		initialize();
		dialogChanged(null);
		languages.addModifyListener(e -> dialogChanged(e));
		generatorTypes.addModifyListener(e -> dialogChanged(e));
		setControl(container);
	}
	
	protected abstract void populateGeneratorTypesCombo(String language);
	protected abstract void fillLanguagesCombo();
	
	protected void determineProjectType() {
		
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IFile) {
				preselectedOpenApiFile = (IFile) obj;
				project = preselectedOpenApiFile.getProject();
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
				
				String possibleProjectTypeId = Util.getProjectTypeId(obj);
				if (possibleProjectTypeId.length() > 0) {
					this.codewindProjectTypeId = possibleProjectTypeId;
				}
			}
		}
		
		try {
			boolean isMavenProject = Utils.isSupportedMavenProject(project);
	    	if (isMavenProject) {
	    		this.projectType = PROJECT_TYPE.MAVEN;		
	    	}
		} catch (Exception e) {
			// ignore
		}
		if (this.projectType == null) {
			this.projectType = PROJECT_TYPE.NOT_ASSESSED;  // The enhancement to improve the usability of other project types, languages, and generators is 'ongoing'
		}
	}

	protected void initialize() {
		fileText.setText(""); //$NON-NLS-1$
		
		if (project != null) {
			if (preselectedOpenApiFile != null) {
				fileText.setText(preselectedOpenApiFile.getFullPath().toString());				
			} else {
				initWithDefinitionAtRoot(); //$NON-NLS-1$
			}
			projectText.setText(project.getName());
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
			
			if (this.projectType == PROJECT_TYPE.MAVEN) {
				initialOutputFolder = project.getFullPath().toString();
				try {
					boolean hasJavaNature = Utils.hasJavaNature(project);
					if (hasJavaNature) {
						languages.setText("Java");
					}
				} catch (CoreException e) {
					// ignore.  Language combobox will not be pre-selected
				}
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
		setMessage(null);
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
			addInfoStatus(Messages.WIZARD_PAGE_SELECT_DEFINITION);
			return;
		}
		if (e != null && e.getSource() == outputFolder) {
			IResource findMember = project.getWorkspace().getRoot().findMember(outputFolder.getText());
			if (findMember != null && findMember.exists() && validateOutputFolder(findMember.getLocation().toOSString())) {
				String workspacePathOfFolder = findMember.getFullPath().toString();
				StringTokenizer stringTokenizer = new StringTokenizer(workspacePathOfFolder, "/"); //$NON-NLS-1$
				if (stringTokenizer.countTokens() == 1) {
					setMessage(null);
				} else {
					setMessage(Messages.MAVEN_PROJECT_DIFFERENT_OUTPUT_FOLDER_DESCRIPTION, IStatus.INFO);
				}
				updateStatus(null);
			} else {
				if (outputFolder.getText().equals(".")) { //$NON-NLS-1$
					updateStatus(null);
				} else if (outputFolder.getText().length() == 0) {
					updateStatus(Messages.WIZARD_PAGE_SELECT_OUTPUT_FOLDER);
				} else {
					updateStatus(Messages.BROWSE_DIALOG_OUTPUT_FOLDER_INVALID);					
				}
			}
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
		setMessage(message, IStatus.INFO);
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
						&& !fileName.toLowerCase().equals("package.json") //$NON-NLS-1$
						&& !fileName.toLowerCase().equals("package-lock.json") //$NON-NLS-1$
						&& !fileName.toLowerCase().equals("chart.yaml") //$NON-NLS-1$
						&& !fileName.toLowerCase().equals("nodemon.json") //$NON-NLS-1$
						&& !fileName.toLowerCase().equals("manifest.yml") //$NON-NLS-1$
						&& !fileName.toLowerCase().equals("devfile.yaml")) { //$NON-NLS-1$
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
		DirectoryDialog dd = new DirectoryDialog(getShell());
		String currentOutputFolderText = outputFolder.getText();
		if (currentOutputFolderText.startsWith(project.getFullPath().toString())) {
			String s = outputFolder.getText().substring(project.getFullPath().toString().length());
			IResource findMember = project.findMember(s);
			dd.setFilterPath(findMember.getLocation().toString());
		} else {
			dd.setFilterPath(project.getLocation().toString());
		}
		// Usability Enhancement/Issue reported: Use a dialog that supports creating a new folder while the browse dialog is up
		dd.setMessage(Messages.BROWSE_DIALOG_MESSAGE_SELECT_FOLDER);
		String selectedFolder = dd.open(); // null if cancelled
		if (selectedFolder != null) {
			if (validateOutputFolder(selectedFolder)) {
				String workspacePathOfFolder = selectedFolder.substring(project.getWorkspace().getRoot().getLocation().toOSString().length());
				outputFolder.setText(workspacePathOfFolder);				
				outputFolder.setFocus();
				StringTokenizer stringTokenizer = new StringTokenizer(workspacePathOfFolder, "/"); //$NON-NLS-1$
				if (stringTokenizer.countTokens() == 1) {
					setMessage(null);
				} else {
					setMessage(Messages.MAVEN_PROJECT_DIFFERENT_OUTPUT_FOLDER_DESCRIPTION, IStatus.INFO);
				}
				return;
			} else {
				MessageDialog.openError(getShell(), Messages.BROWSE_DIALOG_OUTPUT_FOLDER_INVALID, Messages.BROWSE_DIALOG_OUTPUT_FOLDER_INVALID_MESSAGE);
			}
			updateStatus(null);
		}
	}
	
	protected void initWithDefinitionAtRoot() {
		// Do fast check for recommended spec names:
		boolean isFound = checkForDefinition("openapi.yaml"); //$NON-NLS-1$
		if (!isFound) {
			isFound = checkForDefinition("openapi.yml"); //$NON-NLS-1$
			if (!isFound) {
				isFound = checkForDefinition("openapi.json"); //$NON-NLS-1$
			}
		}
		// Usability enhancement.  Use content type support to pre-populate the file text field with the first known Open API file
		if (!isFound) { // If still not found, then use content type
			try {
				IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
				IResource[] members = project.members();
				int numOfMembers = members.length;
				for (int i = 0; i < numOfMembers; i++) {
					if (members[i].getType() == IResource.FILE && members[i].exists() && members[i].isAccessible()) {
						boolean isOpenApiFile = verifyContentType(((IFile)members[i]), contentTypeManager);
						if (isOpenApiFile) {
							preselectedOpenApiFile = (IFile)members[i];
							fileText.setText(preselectedOpenApiFile.getFullPath().toString());
							break;
						}
					}
				}
			} catch (CoreException e) {
				// Ignore.  Will not pre-populate combo with a file name
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
	
	public boolean preCodeGen(boolean ignoreFileExists) {
		// If the .openapi-generator-ignore, prompt to override files regardless of project type
		if (ignoreFileExists) {
        	boolean goAhead = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), Messages.INFO_FILES_EXIST_TITLE, Messages.INFO_FILES_EXIST_DESCRIPTION);
        	if (!goAhead) {
        		return false;
        	}
		}
		boolean isOutputFolderProjectRoot = getOutputFolder().equals(project.getFullPath().toString());
		if (this.projectType == PROJECT_TYPE.MAVEN && isOutputFolderProjectRoot && !ignoreFileExists) {
			boolean goAhead = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), Messages.MAVEN_PROJECT_DETECTED_TITLE, Messages.MAVEN_PROJECT_DETECTED_DESCRIPTION);
        	if (!goAhead) {
        		return false;
        	}
		}
		
		if (this.projectType == PROJECT_TYPE.MAVEN && isOutputFolderProjectRoot && !ignoreFileExists) {
			try {
				IFile pomXmlFile = (IFile)project.findMember(Constants.POM_FILE_NAME);
				
			    IEditorPart editorForPom = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(new FileEditorInput(pomXmlFile));
			    if (editorForPom != null) {
			    	PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editorForPom,  true);
			    }				
				int i = 0;
				IFile tempOrigFile = (IFile)this.project.findMember(Constants.POM_FILE_BACKUP_XML);
				this.tempOrigFileName = Constants.POM_FILE_BACKUP_XML;
				if (tempOrigFile != null) {
					do {
						if (tempOrigFile != null && !tempOrigFile.exists() ) {
							break;
						}
						i++;
						this.tempOrigFileName = Constants.POM_FILE_BACKUP + "-" + i + Constants.POM_FILE_EXTENSION; //$NON-NLS-1$
						tempOrigFile = (IFile)this.project.findMember(tempOrigFileName);

					} while (tempOrigFile != null && tempOrigFile.exists());					
				}
			    pomXmlFile.move(project.getFullPath().append(this.tempOrigFileName), true, new NullProgressMonitor());				
			} catch (CoreException e) {
				Activator.log(IStatus.INFO, "Unexpected exception at pre-CodeGen:", e); //$NON-NLS-1$
			}
		}
		return true;
	}

	public boolean doFinish(IProgressMonitor monitor) {
		return false;
	}
	
	/* 
	 * selectedFolder is the fully qualified path name to the folder
	 * It is valid if it starts with the fully qualified path name of the project
	 */
	private boolean validateOutputFolder(String selectedFolder) {
		if (selectedFolder.startsWith(project.getLocation().toOSString())) { // Tested on Windows
			return true;
		}
		return false;
	}

	private boolean verifyContentType(IFile iFile, IContentTypeManager contentTypeManager) {
		try {
			IContentType contentType = contentTypeManager.findContentTypeFor(iFile.getContents(), iFile.getName());
			if (contentType != null) {
				String id = contentType.getId();
				if (IOpenApiConstants.CONTENTTYPE_JSON.equals(id) || IOpenApiConstants.CONTENTTYPE_YAML.contentEquals(id)) {
					return true;
				}
			}
		} catch (Exception e) {
			// ignore
		}
		return false;
	}
}