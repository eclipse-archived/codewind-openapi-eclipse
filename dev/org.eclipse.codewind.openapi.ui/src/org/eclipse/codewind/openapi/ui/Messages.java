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
package org.eclipse.codewind.openapi.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.codewind.openapi.ui.Messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	
	public static String CLIENT_WIZARD_PAGE_TITLE;
	public static String CLIENT_WIZARD_PAGE_DESCRIPTION;
	
	public static String SERVER_WIZARD_PAGE_TITLE;
	public static String SERVER_WIZARD_PAGE_DESCRIPTION;
	
	public static String HTML_WIZARD_PAGE_TITLE;
	public static String HTML_WIZARD_PAGE_DESCRIPTION;
	
	public static String INFO_FILES_EXIST_TITLE;
	public static String INFO_FILES_EXIST_DESCRIPTION;
	
	public static String INFO_OPENAPI_TITLE;
	public static String INFO_PROJECT_NOT_IMPORTED;
	public static String INFO_INVALID_SELECTION;
	
	public static String WIZARD_PAGE_PROJECT;
	public static String WIZARD_PAGE_DEFINITION;
	public static String WIZARD_PAGE_OUTPUT_FOLDER;
	public static String WIZARD_PAGE_LANGUAGE;
	public static String WIZARD_PAGE_GENERATOR_TYPE;
	public static String WIZARD_PAGE_BROWSE_FILE;
	public static String WIZARD_PAGE_BROWSE_FOLDER;

	public static String WIZARD_PAGE_OUTPUT_FOLDER_TOOLTIP;
	public static String WIZARD_PAGE_GENERATOR_TYPE_TOOLTIP;

	public static String WIZARD_PAGE_PROJECT_NOT_PROVIDED;
	public static String WIZARD_PAGE_PROJECT_NOT_IMPORTED;
	public static String WIZARD_PAGE_PROJECT_NOT_ACCESSIBLE;
	public static String WIZARD_PAGE_SELECT_DEFINITION;
	public static String WIZARD_PAGE_SELECT_OUTPUT_FOLDER;
	public static String WIZARD_PAGE_SELECT_LANGUAGE;
	public static String WIZARD_PAGE_SELECT_GENERATOR_TYPE;

	public static String BROWSE_DIALOG_MESSAGE_SELECT_DEFINITION;
	public static String BROWSE_DIALOG_TITLE_SELECT_DEFINITION;
	
	public static String BROWSE_DIALOG_MESSAGE_SELECT_FOLDER;
	public static String BROWSE_DIALOG_TITLE_SELECT_FOLDER;
	public static String BROWSE_DIALOG_NO_CHILD_FOLDERS;
}
