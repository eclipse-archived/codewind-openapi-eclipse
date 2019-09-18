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
package org.eclipse.codewind.openapi.test.core.contenttype;

import java.io.IOException;

import org.eclipse.codewind.openapi.test.BaseTestCase;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;

public abstract class AbstractContentTypeTest extends BaseTestCase {

	public AbstractContentTypeTest() {
		// Empty
	}

	public AbstractContentTypeTest(String name) {
		super(name);
	}

	protected void doTest() {
		createGeneralProject(this.newProjectName);
		copyDefinitionToProject(this.sourceDefinition, this.targetDefinitionInProject);
		verify();
	}

	protected abstract void verify();

	protected void verifyContentType(String testFile, String expectedId) {
		IResource aFile = this.testProject.findMember(testFile);
		IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
		if (aFile instanceof IFile) {
			IFile iFile = (IFile) aFile;
			try {
				IContentType contentType = contentTypeManager.findContentTypeFor(iFile.getContents(), aFile.getName());
				System.out.println("CONTENT TYPE IS " + contentType);
				if (expectedId != null) {
					System.out.println(iFile.getLocation() + ": " + contentType.getId());
					assertTrue("Verify content type id", expectedId.equals(contentType.getId()));
				} else {
					assertTrue("Verify content type id", contentType == null);
				}
			} catch (IOException e) {
				e.printStackTrace();
				fail(e.getMessage());
			} catch (CoreException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
	}
}
