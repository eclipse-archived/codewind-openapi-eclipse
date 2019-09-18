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
package org.eclipse.codewind.openapi.test;

import org.eclipse.codewind.openapi.core.IOpenApiConstants;

public class Constants {
	
	public static String CONTENTTYPE_YAML = IOpenApiConstants.CONTENTTYPE_YAML;
	public static String CONTENTTYPE_JSON = IOpenApiConstants.CONTENTTYPE_JSON;
	
	public static String PETSTORE_30 = "/resources/3.0/petstore.yaml";
	public static String PETSTORE_OAS_30 = "/resources/3.0/petstore-oas-example.yaml";
	public static String PETSTORE_JSON_30 = "/resources/3.0/petstore.json";
	public static String GEN_YAML_30 = "/resources/3.0/gen.yaml";
	public static String GEN_YML_30 = "/resources/3.0/gen.yml";
	public static String GEN_JSON_30 = "/resources/3.0/gen.json";
	public static String SWAGGER_JSON_30 = "/resources/3.0/swagger.json";
	
	public static String SWAGGER_YAML_20 = "/resources/2.0/swagger.yaml";
	public static String SWAGGER_JSON_20 = "/resources/2.0/swagger.json";
	public static String SWAGGER001_YAML_20 = "/resources/2.0/swagger001.yaml";
	public static String SWAGGER002_YAML_20 = "/resources/2.0/swagger002.yaml";
	
	public static String ANY_JSON_FILE = "/resources/nonOpenApi/AnyJsonFile.json";
	public static String ANY_YAML_FILE = "/resources/nonOpenApi/AnyYamlFile.yml";
	public static String REGULAR_JSON_FILE = "/resources/nonOpenApi/RegularJson.json";

	public Constants() {
		// Empty
	}
}
