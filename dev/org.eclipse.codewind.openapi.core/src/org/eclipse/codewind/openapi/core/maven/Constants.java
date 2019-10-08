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

public class Constants {

	public Constants() {
		// Empty
	}

    public static final String MAVEN_PROJECT_NATURE = "org.eclipse.m2e.core.maven2Nature";
	public static final String SRC_GEN_PATH = "/src/gen/java"; // Extra source path needed to be added to the classpath

	public static final String POM_FILE_NAME = "pom.xml";
	public static final String POM_FILE_BACKUP_XML = "pom-backup.xml";
	public static final String POM_FILE_BACKUP = "pom-backup";
	public static final String POM_FILE_EXTENSION = ".xml";
	// Currently, these are not enabled
	public static final String POM_FILE_MERGED = "pom-merged.xml";
	public static final String POM_FILE_OPENAPI_GENERATED = "pom-generated.xml";	
}
