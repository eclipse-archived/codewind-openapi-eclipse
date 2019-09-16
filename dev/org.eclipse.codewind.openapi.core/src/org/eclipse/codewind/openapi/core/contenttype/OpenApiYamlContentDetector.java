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

public class OpenApiYamlContentDetector extends OpenApiContentDetector {

	public OpenApiYamlContentDetector() {
		// Empty
	}

	protected void parseInput() throws IOException {
		/*
	     * Find in yaml:
	     *   openapi: 3.
	     *   while ignoring spaces
	     *   
		 */
		if (findString("openapi") && findChar(':') && 
				findString("3.", "\"3.")) {
			fOpenApiFieldDetected = true;
		}
		fOpenApiParsed = true;
	}

}
