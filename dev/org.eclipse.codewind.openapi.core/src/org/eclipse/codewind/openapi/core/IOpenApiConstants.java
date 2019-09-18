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
package org.eclipse.codewind.openapi.core;

public interface IOpenApiConstants {
	
	public static final String OPENAPI_CLI_OVERRIDE_PROPERTY = "org.eclipse.codewind.openapi.cli.jar.path"; //$NON-NLS-1$
	public static final String CODEGEN_JAR_401 = "openapi-generator-cli-4.0.1.jar";	//$NON-NLS-1$

	public static final String CODEWIND_LANGUAGE = "language"; //$NON-NLS-1$
	public static final String CODEWIND_PROJECT_NAME = "name"; //$NON-NLS-1$
	public static final String CODEWIND_PROJECT_FULL_LOCAL_PATH = "fullLocalPath"; //$NON-NLS-1$
	public static final String CODEWIND_PROJECT_LANGUAGE = "projectLanguage"; //$NON-NLS-1$
	public static final String CODEWIND_GET_ID_METHOD = "getId"; //$NON-NLS-1$
	public static final String CODEWIND_CW_SETTINGS = ".cw-settings"; //$NON-NLS-1$
	
	public static String CONTENTTYPE_YAML = "org.eclipse.codewind.openapi.core.openApiDocumentYaml"; //$NON-NLS-1$
	public static String CONTENTTYPE_JSON = "org.eclipse.codewind.openapi.core.openApiDocumentJson"; //$NON-NLS-1$
}
