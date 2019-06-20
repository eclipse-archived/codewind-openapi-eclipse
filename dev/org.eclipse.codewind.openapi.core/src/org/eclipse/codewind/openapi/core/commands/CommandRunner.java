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
package org.eclipse.codewind.openapi.core.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.codewind.openapi.core.Activator;
import org.eclipse.codewind.openapi.core.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

public class CommandRunner {

	private List<String> args_;
	protected static String lineSeparator_;
	private String commandName;

	public CommandRunner(List<String> args, String commandName) {
		args_ = new ArrayList<String>();
		args_.addAll(args);
		lineSeparator_ = System.getProperty("line.separator");
		this.commandName = commandName;
	}

	public IStatus run() {
		ProcessBuilder pb = new ProcessBuilder(args_);
		pb.redirectErrorStream(true);
		StringBuffer outputBuffer = new StringBuffer();
		try {
			File javaHomePath = getJavaHome();
			boolean argsAdded = false;
			
			if (javaHomePath != null) {
				Map<String, String> environment = pb.environment();
				environment.put("JAVA_HOME", javaHomePath.getPath());				
				File javaExecutablePath = getJavaBinaryPath(javaHomePath);
				if(javaExecutablePath != null && javaExecutablePath.exists()) {
					File javaBinDirectory = javaExecutablePath.getParentFile();
					Set<String> keySet = environment.keySet();
					if (keySet.contains("PATH")) {
						environment.put("PATH", javaBinDirectory.getPath() + File.pathSeparator + environment.get("PATH"));
					} else if (keySet.contains("Path")) {
						environment.put("Path", javaBinDirectory.getPath() + File.pathSeparator + environment.get("Path") );
					} else {
						environment.put("PATH", javaBinDirectory.getPath());
					}
					// Add the fully qualified path to the Java executable
					args_.add(0, javaExecutablePath.getPath());
					argsAdded = true;
				}
			}
			
			if(!argsAdded) {
				args_.add(0, "java");  //$NON-NLS-1$
			}

			Process process = pb.start();
			CommandOutputReader reader = new CommandOutputReader(process.getInputStream(), outputBuffer);
			reader.start();
			int rc = process.waitFor();
			Activator.log(IStatus.INFO,"Codegen result:");
			Activator.log(IStatus.INFO,"RC = " + rc + "\n" + outputBuffer.toString());
			if (rc != 0) {
				outputBuffer.insert(0, lineSeparator_);
				String outputBufferText = outputBuffer.toString();

				final String pluginId = Activator.getDefault().getBundle().getSymbolicName();
				MultiStatus multiStatus = new MultiStatus(pluginId, IStatus.ERROR, MessageFormat.format(Messages.COMMAND_RUNNER_ERROR, commandName), null);
				String consoleOutputNew = outputBufferText.replace("\r", "");
				String[] lines = consoleOutputNew.split("\n");
				for (String line : lines) {
					multiStatus.add(new Status(IStatus.ERROR, pluginId, line));
				}
				return multiStatus;
			}
		} catch (InterruptedException e) {
			return createErrorStatus(outputBuffer, e);
		} catch (IOException e) {
			return createErrorStatus(outputBuffer, e);
		}
		return Status.OK_STATUS;
	}

	private final IStatus createErrorStatus(StringBuffer outputBuffer, Throwable throwable) {
		outputBuffer.insert(0, lineSeparator_);
		outputBuffer.insert(0, MessageFormat.format(Messages.COMMAND_RUNNER_ERROR, commandName));
		if (throwable == null) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, outputBuffer.toString());
		} else {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, outputBuffer.toString(), throwable);
		}
	}

	private static File getJavaHome() {

		File result = null;
		IVMInstall install = JavaRuntime.getDefaultVMInstall();
		if (install != null && install.getInstallLocation() != null && install.getInstallLocation().exists()) {
			result = install.getInstallLocation();
		}
		return result;
	}
	
	private static File getJavaBinaryPath(final File homeDir) {
		File result = null;
		
		if (homeDir != null && homeDir.exists()) {
			String osName = System.getProperty("os.name").toLowerCase();
			boolean isWindows = osName.indexOf("win") >= 0;

			if (isWindows) {
				File java = new File(homeDir, "bin\\java.exe"); //$NON-NLS-1$
				if (java.exists()) {
					result = java;
				} else {
					java = new File(homeDir, "jre\\bin\\java.exe"); //$NON-NLS-1$
					if (java.exists()) {
						result = java;
					}
				}
			} else {

				File java = new File(homeDir, "bin/java"); //$NON-NLS-1$
				if (java.exists()) {
					result = java;
				} else {
					java = new File(homeDir, "jre/bin/java"); //$NON-NLS-1$

					if (java.exists()) {
						result = java;
					}
				}
			}
		}
		return result;
	}
	
	final class CommandOutputReader extends Thread {
		private InputStream is;
		private StringBuffer sb;

		public CommandOutputReader(InputStream is, StringBuffer sb) {
			CommandOutputReader.this.is = is;
			CommandOutputReader.this.sb = sb;
		}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while (true) {
				try {
					String s = br.readLine();
					if (s == null) {
						break;
					}
					sb.append(s).append(CommandRunner.lineSeparator_);
				} catch (IOException e) {
					return;
				}
			}
		}
	}
}

