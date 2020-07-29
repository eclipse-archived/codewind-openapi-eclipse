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

import java.io.IOException;

import org.eclipse.codewind.openapi.core.IOpenApiConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class CustomLabelDecorator implements ILabelDecorator {

	public CustomLabelDecorator() {
		// Empty
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// Empty

	}

	@Override
	public void dispose() {
		// Empty

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// Empty
	}

	@Override
	public Image decorateImage(Image arg0, Object resource) {
		if (resource instanceof IFile) {
			IFile iFile = (IFile) resource;
			try {
				IContentTypeManager contentTypeManager = Platform.getContentTypeManager();
				IContentType contentType = contentTypeManager.findContentTypeFor(iFile.getContents(), iFile.getName());
				if (contentType != null) {
					if (IOpenApiConstants.CONTENTTYPE_JSON.equals(contentType.getId())
						|| IOpenApiConstants.CONTENTTYPE_YAML.contentEquals(contentType.getId())) {
						Image image = Activator.getImage(Constants.IMG_OPENAPI);
						return image;
					}
				}
			} catch (IOException e) {
				// ignore
			} catch (CoreException e) {
				// ignore
			}
		}
		return arg0;
	}

	@Override
	public String decorateText(String arg0, Object arg1) {
		return null;
	}

}
