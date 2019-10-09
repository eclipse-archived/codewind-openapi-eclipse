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
package org.eclipse.codewind.openapi.core.util;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.codewind.openapi.core.IOpenApiConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Util {

	private static String UNKNOWN_VALUE = "unknown"; //$NON-NLS-1$
	
	public Util() {
	}

	public static boolean isCodewindProject(IProject project) {
		return project.getFile(IOpenApiConstants.CODEWIND_CW_SETTINGS).exists(); //$NON-NLS-1$
	}
	
	public static String getProjectLanguage(IProject project) {
		IPath location = project.getLocation();
		IPath dotProjectFolder = location.append("../.projects"); //$NON-NLS-1$
		String projectName = project.getName();
		IPath projectInfFile = dotProjectFolder.append(projectName + ".inf"); //$NON-NLS-1$
		
		File f = projectInfFile.toFile();
		if (f.exists()) {
			try {
				JSONTokener tokenizer = new JSONTokener(new FileReader(f));
				JSONObject jobj = new JSONObject(tokenizer);
				Object object = jobj.get(IOpenApiConstants.CODEWIND_LANGUAGE);
				if (object != null) {
					String lang = object.toString();
					if (UNKNOWN_VALUE.equals(lang)) {
						lang = ""; //$NON-NLS-1$
					}
					return lang;
				}
			} catch (Exception e) {
				Activator.log(IStatus.INFO, "Cannot find the metadata file for the project"); //$NON-NLS-1$
			}
		} else {
			File dotProjectFolderFile = dotProjectFolder.toFile();
			if (dotProjectFolderFile.isDirectory()) {
				File[] listFiles = dotProjectFolderFile.listFiles();
				int length = listFiles.length;
				for (int i = 0; i < length; i++ ) {
					File aFile = listFiles[i];
					try {
						if (aFile.isFile()) {
							JSONTokener tokenizer = new JSONTokener(new FileReader(aFile));
							JSONObject jobj = new JSONObject(tokenizer);
							Object nameObject = jobj.get(IOpenApiConstants.CODEWIND_PROJECT_NAME);
							if (nameObject != null) {
								String name = nameObject.toString();
								if (projectName.contentEquals(name)) {
									Object object = jobj.get(IOpenApiConstants.CODEWIND_LANGUAGE);
									if (object != null) {
										String lang = object.toString();
										if (UNKNOWN_VALUE.equals(lang)) {
											lang = ""; //$NON-NLS-1$
										}
										return lang;
									}								
								}
							}
						}
					} catch (Exception e) {
						Activator.log(IStatus.INFO, "Cannot find language for the project"); //$NON-NLS-1$
					}
				}
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	public static boolean isImported(Object obj) {
		Class<?> clazz = obj.getClass();
		try {
			Field field = clazz.getField(IOpenApiConstants.CODEWIND_PROJECT_FULL_LOCAL_PATH);
			Field nameField = clazz.getField(IOpenApiConstants.CODEWIND_PROJECT_NAME);
			Object object = field.get(obj);
			Object nObj = nameField.get(obj);
			if (nObj instanceof String) {
				String pName = (String)nObj;
				IResource proj = ResourcesPlugin.getWorkspace().getRoot().findMember(pName);
				if (proj == null) {
					return false;
				}
				IPath location = proj.getLocation();
				if (object instanceof IPath) {
					IPath pat = (IPath) object;
					if (location.toString().equals(pat.toString())){
						return proj.isAccessible();
					}
				}
			}
		} catch (Exception e) {
			Activator.log(IStatus.INFO, e);
		}
		return false;
	}
	
	public static String getProjectLanguage(Object obj) {
		Class<?> clazz = obj.getClass();
		try {
			Field field = clazz.getField(IOpenApiConstants.CODEWIND_PROJECT_LANGUAGE);
			Object projectLanguageObject = field.get(obj);
			Class<?> projectLanguageClass = projectLanguageObject.getClass();
			Method getIdMethod = projectLanguageClass.getMethod(IOpenApiConstants.CODEWIND_GET_ID_METHOD); //$NON-NLS-1$
			Object languageObject = getIdMethod.invoke(projectLanguageObject);
			
			if (languageObject instanceof String) {
				String language = (String)languageObject;
				if (UNKNOWN_VALUE.equals(language)) { //$NON-NLS-1$
					language = ""; //$NON-NLS-1$
				}
				return language;
			}
		} catch (Exception e) {
			Activator.log(IStatus.ERROR, e);
		}
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @param obj
	 * @return a non-null string
	 */
	public static String getProjectTypeId(Object obj) {
		Class<?> clazz = obj.getClass();
		try {
			Field field = clazz.getField(IOpenApiConstants.CODEWIND_PROJECT_TYPE);
			Object projectTypeObject = field.get(obj);
			Class<?> projectTypeClass = projectTypeObject.getClass();
			Method getIdMethod = projectTypeClass.getMethod(IOpenApiConstants.CODEWIND_GET_ID_METHOD); //$NON-NLS-1$
			Object typeObject = getIdMethod.invoke(projectTypeObject);
			
			if (typeObject instanceof String) {
				String typeId = (String)typeObject;
				if (UNKNOWN_VALUE.equals(typeId)) { //$NON-NLS-1$
					typeId = ""; //$NON-NLS-1$
				}
				return typeId;
			}
		} catch (Exception e) {
			Activator.log(IStatus.ERROR, e);
		}
		return ""; //$NON-NLS-1$
	}
	
	public static IProject getProject(Object obj) {
		Class<?> clazz = obj.getClass();
		String projectName = null;
		try {
			Field field = clazz.getField(IOpenApiConstants.CODEWIND_PROJECT_NAME);
			Object object = field.get(obj);
			if (object instanceof String) {
				projectName = (String)object;
			}
		} catch (Exception e) {
			Activator.log(IStatus.ERROR, e);
		}
		if (projectName != null) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}
		return null;
	}
}
