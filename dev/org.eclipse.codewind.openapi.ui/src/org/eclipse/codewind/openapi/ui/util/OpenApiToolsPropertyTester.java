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
package org.eclipse.codewind.openapi.ui.util;

import org.eclipse.codewind.openapi.core.util.Util;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;

public class OpenApiToolsPropertyTester extends PropertyTester {

	public OpenApiToolsPropertyTester() {
		// Empty
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if ("isImported".equals(property)) {  //$NON-NLS-1$ // For enablement of menus.  See plugin.xml
			if (receiver != null) {
				if (receiver instanceof IProject) {
					return ((IProject)receiver).isOpen();
				}
				return Util.isImported(receiver);
			}
		} else if ("validSelection".equals(property)) { //$NON-NLS-1$  // For visibility of menus.  See plugin.xml
			if (receiver instanceof IProject) {
				return true;
			}
		}
		return false;
	}
}
