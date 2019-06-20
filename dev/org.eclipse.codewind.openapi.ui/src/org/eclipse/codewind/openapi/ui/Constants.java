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

/*
 * None of these constants are translatable
 */
public class Constants {

	public Constants() {
		// Empty
	}
	
	public static String CODEWIND_EXPLORER_VIEW = "org.eclipse.codewind.ui.explorerView"; //$NON-NLS-1$
	public static String HTML_DOCUMENTATION_FILE = "index.html"; //$NON-NLS-1$
	
	public static String ALL_CLIENT_LANGUAGES[][]  = new String[][] {
		{ "Go", "go"}, 
		{ "Java", "java", "jaxrs-cxf-client"},
		{ "Node.js", "javascript",
			"javascript-closure-angular",
			"javascript-flowtyped",
			"typescript-angular",
			"typescript-angularjs",
			"typescript-aurelia",
			"typescript-axios",
			"typescript-fetch",
			"typescript-inversify",
			"typescript-jquery",
        	"typescript-node"},
		{"Python", "python"},
        {"Swift", "swift3", "swift4", "swift2-deprecated"},
        {"Ada", "ada"},
        {"Apex", "apex"},
        {"Bash", "bash"},
        {"C", "c"},
        {"C#", "csharp", "csharp-dotnet2", "csharp-refactor"},
        {"C++", "cpp-qt5", "cpp-restsdk", "cpp-tizen"},
        {"Dart", "dart", "dart-jaguar"},
        {"Eiffel", "eiffel"},
        {"Elixir", "elixir"},
        {"Elm", "elm"},
        {"Erlang", "erlang-client", "erlang-proper"},
        {"Haskell", "haskell-http-client"},
        {"Kotlin", "kotlin"},
        {"Lua", "lua"},
        {"Objective-C", "objc"},
        {"Perl", "perl"},
        {"PHP", "php"},
        {"PowerShell", "powershell"},
        {"R", "r"},
        { "Ruby", "ruby"},
        { "Rust", "rust"},
        { "Scala", "scala-akka","scala-gatling","scala-httpclient","scalaz"},
        { "TypeScript", "typescript-angular",
                       "typescript-angularjs",
                       "typescript-aurelia",
                       "typescript-axios",
                       "typescript-fetch",
                       "typescript-inversify",
                       "typescript-jquery",
                       "typescript-node"}
	};
	
	public static String ALL_SERVER_LANGUAGES[][]  = new String[][] {
		{"Go", "go-gin-server", "go-server"},
        {"Java", "jaxrs-spec",
            "java-inflector",
            "java-msf4j",
            "java-pkmst",
            "java-play-framework",
            "java-undertow-server",
            "java-vertx",
            "jaxrs-cxf",
            "jaxrs-cxf-cdi",
            "jaxrs-jersey",
            "jaxrs-resteasy",
            "jaxrs-resteasy-eap",
            "spring"},
        {"Node.js", "nodejs-server"},
        {"Python", "python-flask"},
        {"Swift", ""},
        {"Ada", "ada-server"},
        {"C#", "csharp-nancyfx"},
        {"C++", "cpp-pistache-server", "cpp-qt5-qhttpengine-server", "cpp-restbed-server"},
        {"Erlang", "erlang-server"},
        {"Haskell", "haskell"},
        {"Kotlin", "kotlin-server", "kotlin-spring"},
        {"PHP", "php-laravel", "php-lumen", "php-silex", "php-slim", "php-symfony", "php-ze-ph"},
        {"Python", "python-flask"},
        {"Ruby", "ruby-on-rails", "ruby-sinatra"},
        {"Rust", "rust-server"},
        {"Scala", "scala-finch","scala-lagom-server", "scalatra"}
    };
}
