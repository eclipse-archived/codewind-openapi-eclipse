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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class OpenApiContentDetector implements IOpenApiContentDetector {

	protected boolean fOpenApiFieldDetected = false;
	protected boolean fOpenApiParsed;
	protected Reader fReader;
	
	public OpenApiContentDetector() {
		// empty
	}

	@Override
	public boolean isOpenApiDetected() {
		if (!fOpenApiParsed) {
			try {
				parseInput();
			} catch(IOException e) {
				fOpenApiFieldDetected = false;
			} finally {
				try {
					fReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			fOpenApiParsed = true;
		}
		return fOpenApiFieldDetected;
	}
	
	@Override
	public void set(InputStream inputStream) {
		resetAll();
		fReader = new InputStreamReader(inputStream);
	}

	@Override
	public void set(Reader reader) {
		resetAll();
		fReader = reader;
		if (!fReader.markSupported()) {
			fReader = new BufferedReader(fReader);
		}
	}
	
	protected void resetAll() {
		fReader = null;
		fOpenApiParsed = false;
	}

	protected abstract void parseInput() throws IOException;
	
	/**
	 * Support variations of the target string because characters are read in once.
	 *
	 * Note:
	 * OpenAPI Tools SHOULD NOT consider the service version
	 * Subsequent minor updates of the OpenAPI specification SHOULD still be usable with OpenAPI tools
	 * eg. openapi:3. 
	 *     openapi: 3.
	 *     openapi:"3.
	 * 
	 * @param target - Expected string to match
	 * @return true if it file has matched content, false if it does not
	 */
	protected boolean findString(String ...target) {
		boolean analyze = true;
		int i = 1;
		char [] c = new char[1];
		String firstNonSpaceChar = "";
		int numberOfTargets = target.length;
		try {
			boolean moreSpaces = true;
			do {
				int aChar = fReader.read(c);
				if (aChar < 1) {
					// If an empty file or end of file is reached, then stop analyzing
					analyze = false;
				}
				firstNonSpaceChar = String.valueOf(c);
				if (firstNonSpaceChar.trim().length() > 0) {
					moreSpaces = false;
				}
				i++;
			} while (analyze && moreSpaces && i < 80);  // limit this to 80 for performance reasons
			
			if (moreSpaces) { // Still haven't found a non-space char, just return
				return false;
			}
			analyze = true;
			String text = firstNonSpaceChar;
			do {
				int read = fReader.read(c);
				if (read < 1) {
					// If an empty file or end of file is reached, then stop analyzing
					analyze = false;
				}
				text += String.valueOf(c);
				boolean startsWithMatch = false;  // match char by char
				for (int j = 0; j < numberOfTargets; j++) {
					if (target[j].startsWith(text.trim())) {
						startsWithMatch = true;
					} else {
						continue;
					}
					if (target[j].equals(text.trim())) { // If any target matches then return
						return true;
					}
				}
				if (!startsWithMatch) { // If the text does not start with any of the targets, stop analyzing
					analyze = false;
				}
			} while (analyze);
		} catch (IOException ioe) {
			// do nothing, just return false
		}

		return false;
	}
	
	/*
	 * Find the target from the reader
	 */
	protected boolean findChar(char target) {
		int i = 1;
		char [] possibleLeadingSpace = new char[1];
		String firstNonSpaceChar = "";
		try {
			boolean moreSpaces = true;
			do {
				int aChar = fReader.read(possibleLeadingSpace);
				if (aChar < 1) {
					return false;
				}
				firstNonSpaceChar = String.valueOf(possibleLeadingSpace);
				if (firstNonSpaceChar.trim().length() > 0) {
					moreSpaces = false;
				}
				i++;
			} while (moreSpaces && i < 80);  // limit this to 80 for performance reasons
			if (moreSpaces) {
				return false;
			}
			if (target == firstNonSpaceChar.charAt(0)) {
			    return true;
			}
		} catch (IOException ioe) {
			// do nothing, just return false
		}
		return false;
	}
}
