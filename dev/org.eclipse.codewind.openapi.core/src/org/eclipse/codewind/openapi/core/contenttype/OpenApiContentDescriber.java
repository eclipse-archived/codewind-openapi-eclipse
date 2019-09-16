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
package org.eclipse.codewind.openapi.core.contenttype;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

public abstract class OpenApiContentDescriber implements ITextContentDescriber {
	private final static QualifiedName[] SUPPORTED_OPTIONS = {IContentDescription.CHARSET, IContentDescription.BYTE_ORDER_MARK};

	public OpenApiContentDescriber() {
		// Empty
	}
	
	public abstract IOpenApiContentDetector getDetector();

	@Override
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		int result = IContentDescriber.INVALID;
		result = determineValidity(result, contents);
		return result;
	}

	@Override
	public QualifiedName[] getSupportedOptions() {
		return SUPPORTED_OPTIONS;
	}

	@Override
	public int describe(Reader contents, IContentDescription description) throws IOException {
		int result = IContentDescriber.INVALID;
	    result = determineValidity(result, contents);
		return result;
	}

	private int determineValidity(int result, InputStream contents) throws IOException {
		int returnResult = result;
		IOpenApiContentDetector detector = getDetector();
		contents.reset();
		detector.set(contents);
		returnResult = determineValidity(detector, returnResult);
		return returnResult;
	}
	
	private int determineValidity(int result, Reader contents) throws IOException {
		int returnResult = result;
		IOpenApiContentDetector detector = getDetector();
		contents.reset();
		detector.set(contents);
		returnResult = determineValidity(detector, returnResult);
		return returnResult;
	}
	
	private int determineValidity(IOpenApiContentDetector detector, int returnResult) {
		if (detector instanceof OpenApiContentDetector) {
			OpenApiContentDetector openApiDetector = (OpenApiContentDetector) detector;
			if (openApiDetector.isOpenApiDetected()) {
				returnResult = IContentDescriber.VALID;
			} else {
				returnResult = IContentDescriber.INVALID;
			}
		}
		return returnResult;
	}
}
