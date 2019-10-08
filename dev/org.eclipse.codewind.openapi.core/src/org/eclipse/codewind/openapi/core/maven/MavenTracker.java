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
package org.eclipse.codewind.openapi.core.maven;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.model.merge.MavenModelMerger;
import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

/*
 * For wrapping and tracking the POM models so it is created once
 * 
 */
public class MavenTracker {

	private IFile targetPomFile;
	private IFile sourcePomFile;
	private Model targetPomFileModel;
	private Model sourcePomFileModel;
	private IProject project;

	private String targetGroupId;
	
	public MavenTracker(IFile target, IFile source, IProject project) {
		this.targetPomFile = target;
		this.sourcePomFile = source;
		this.project = project;
		this.targetPomFileModel = getModelObject(target);
		this.sourcePomFileModel = getModelObject(source);
		Parent parent = targetPomFileModel.getParent();
		if (parent != null) {
			this.targetGroupId = parent.getGroupId();
		}
	}
	
	/*
	 * Merge the models
	 * 
	 */
	public void mergeModels() {
        try {
        	Parent parent = targetPomFileModel.getParent();
        	if (parent != null) {	        		
	            try {
		            if (this.targetGroupId != null && this.targetGroupId.toLowerCase().contains("org.springframework.boot")) { //$NON-NLS-1$
		            	Build sourceBuild = sourcePomFileModel.getBuild();
		            	if (sourceBuild != null) {
		            		String sourceDirectory = sourceBuild.getSourceDirectory();
		            		if (sourceDirectory != null) {
		            			sourceBuild.setSourceDirectory(null);
		            		}
		            	}
		            }
	            } catch (Exception e) {
	            	Activator.log(IStatus.INFO, "Error:", e); //$NON-NLS-1$
	            }
        	}
        	MavenModelMerger merger = new MavenModelMerger();
            merger.merge(targetPomFileModel, sourcePomFileModel, false, null);
            MavenXpp3Writer xpp3writer = new MavenXpp3Writer();            
            FileOutputStream fos = new FileOutputStream(sourcePomFile.getLocation().toString());  
            xpp3writer.write(fos, targetPomFileModel);            
            project.refreshLocal(IProject.DEPTH_ONE, new NullProgressMonitor());
        } catch (Exception e) {
        	Activator.log(IStatus.INFO, "Possible issue when merging the two Maven Models:", e); //$NON-NLS-1$
        }
	}
	
    private Model getModelObject(IFile pomFile) {
        InputStreamReader streamReader = null;
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            streamReader = new InputStreamReader(new FileInputStream(pomFile.getLocation().toFile()));
            Model model = reader.read(streamReader);
            return model;
        } catch (Exception e) {
        	Activator.log(IStatus.ERROR, e);
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                	// ignore
                }
            }
        }
        return null;
    }

	public IFile getOriginalPomFile() {
		return this.targetPomFile;
	}

	public IFile getTargetPomFile() {
		return this.sourcePomFile;
	}

	public Model getOriginalPomFileModel() {
		return targetPomFileModel;
	}

	public Model getTargetPomFileModel() {
		return sourcePomFileModel;
	}

	public String getTargetGroupId() {
		return targetGroupId;
	}
}
