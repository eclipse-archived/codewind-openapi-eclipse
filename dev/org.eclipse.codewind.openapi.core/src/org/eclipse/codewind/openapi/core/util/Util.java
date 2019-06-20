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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Util {

	public Util() {
	}

	public static boolean isCodewindProject(IProject project) {
		return project.getFile(".cw-settings").exists();
	}
	
	public static String getProjectLanguage(IProject project) {
		IPath location = project.getLocation();
		IPath dotProjectFolder = location.append("../.projects");
		String projectName = project.getName();
		IPath projectInfFile = dotProjectFolder.append(projectName + ".inf");
		
		File f = projectInfFile.toFile();
		if (f.exists()) {
			try {
				JSONTokener tokenizer = new JSONTokener(new FileReader(f));
				JSONObject jobj = new JSONObject(tokenizer);
				Object object = jobj.get("language");
				if (object != null) {
					String lang = object.toString();
					if ("unknown".equals(lang)) {
						lang = "";
					}
					return lang;
				}
			} catch (Exception e) {
				Activator.log(IStatus.INFO, "Cannot find the metadata file for the project");
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
							Object nameObject = jobj.get("name");
							if (nameObject != null) {
								String name = nameObject.toString();
								if (projectName.contentEquals(name)) {
									Object object = jobj.get("language");
									if (object != null) {
										String lang = object.toString();
										if ("unknown".equals(lang)) {
											lang = "";
										}
										return lang;
									}								
								}
							}
						}
					} catch (Exception e) {
						Activator.log(IStatus.INFO, "Cannot find language for the project");
					}
				}
			}
		}
		return "";
	}
	
	public static boolean isImported(Object obj) {
		Class<?> clazz = obj.getClass();
		try {
			Field field = clazz.getField("fullLocalPath");
			Field nameField = clazz.getField("name");
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
						return true;
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
			Field field = clazz.getField("projectLanguage");
			Object projectLanguageObject = field.get(obj);
			Class<?> projectLanguageClass = projectLanguageObject.getClass();
			Method getIdMethod = projectLanguageClass.getMethod("getId");
			Object languageObject = getIdMethod.invoke(projectLanguageObject);
			
			if (languageObject instanceof String) {
				String language = (String)languageObject;
				if ("unknown".equals(language)) {
					language = "";
				}
				return language;
			}
		} catch (Exception e) {
			Activator.log(IStatus.ERROR, e);
		}
		return "";
	}
	
	public static IProject getProject(Object obj) {
		Class<?> clazz = obj.getClass();
		String projectName = null;
		try {
			Field field = clazz.getField("name");
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
