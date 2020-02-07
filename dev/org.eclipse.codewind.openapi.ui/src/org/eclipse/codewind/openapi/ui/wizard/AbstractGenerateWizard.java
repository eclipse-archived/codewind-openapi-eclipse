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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.codewind.openapi.ui.Activator;
import org.eclipse.codewind.openapi.ui.Constants.PROJECT_TYPE;
import org.eclipse.codewind.openapi.ui.Messages;
import org.eclipse.codewind.openapi.ui.commands.AbstractOpenApiGeneratorCommand;
import org.eclipse.codewind.openapi.ui.commands.PostGenCommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
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
		this.page = (AbstractGenerateWizardPage) p;
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
			IFolder outputFolder = (IFolder) ResourcesPlugin.getWorkspace().getRoot()
					.getFolder(new Path(page.getOutputFolder()));
			ignoreFile = outputFolder.findMember(".openapi-generator-ignore"); //$NON-NLS-1$
		}
		
		IResource pomFile;
		if (page.getOutputFolder().equals(project.getFullPath().toString())) {
			pomFile = project.findMember("pom.xml"); //$NON-NLS-1$
		} else {
			IFolder outputFolder = (IFolder) ResourcesPlugin.getWorkspace().getRoot()
					.getFolder(new Path(page.getOutputFolder()));
			pomFile = outputFolder.findMember("pom.xml"); //$NON-NLS-1$
		}
		
		boolean ignoreFileExists = ignoreFile != null && ignoreFile.exists();
		cmd.setPomFileExists(pomFile != null && pomFile.exists());

		// Do pre-code gen checks for both generators
		boolean doContinue = page.preCodeGen(ignoreFileExists);
		if (!doContinue) {
			performCancel();
			return true; // To close the wizard, after confirmation dialogs are dismissed by the user
		}

		PROJECT_TYPE projectType = page.getProjectType();
		
		cmd.setCodewindProjectTypeId(page.getCodewindProjectTypeId());

		// Need a wrapper outside the context of the wizard page, since the wizard will be disposed of.
		// Pass in the needed parameters gathered by the wizard
	    // Originally, this was supposed to run inside the wizard's container progress, but potentially,
		// this could be a long running process if mvn dependencies are downloaded.
		PostGenCommand postGenCommand = new PostGenCommand(project, projectType, ignoreFileExists,
				page.getOutputFolder(), page.getPomFileBackupName());

		// Dismiss the wizard and run the workspace modify operations in an eclipse Job
		Job job = new Job(Messages.JOB_GENERATING) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					cmd.run(monitor); // Run the generator
					if (projectType == PROJECT_TYPE.MAVEN) {
						postGenCommand.run(monitor);
					}
				} catch (InvocationTargetException e) {
					Activator.log(IStatus.INFO, "Generator failed: " + e.getMessage()); //$NON-NLS-1$
				} catch (InterruptedException e) {
					Activator.log(IStatus.INFO, "Generator job was cancelled"); //$NON-NLS-1$
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.setRule(ResourcesPlugin.getWorkspace().getRoot());
		job.setPriority(Job.LONG);
		job.schedule(1000);
		return true;
	}
}