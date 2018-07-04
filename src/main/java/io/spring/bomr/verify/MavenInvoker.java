/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.bomr.verify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.InvokerLogger;
import org.apache.maven.shared.invoker.PrintStreamLogger;

/**
 * A {@code MavenInvoker} can be used to programmatically invoke Maven.
 *
 * @author Andy Wilkinson
 */
class MavenInvoker {

	private final File mavenHome;

	MavenInvoker(File mavenHome) {
		this.mavenHome = mavenHome;
	}

	void invoke(File pom, Properties properties, String... goals)
			throws MavenInvocationFailedException {
		DefaultInvocationRequest invocation = new DefaultInvocationRequest();
		invocation.setPomFile(pom);
		invocation.setGoals(Arrays.asList(goals));
		invocation.setProperties(properties);
		invocation.setUpdateSnapshots(true);
		CapturingInvocationOutputHandler outputHandler = new CapturingInvocationOutputHandler();
		invocation.setOutputHandler(outputHandler);
		DefaultInvoker invoker = new DefaultInvoker();
		invoker.setLogger(new PrintStreamLogger(
				new PrintStream(new ByteArrayOutputStream(), true), InvokerLogger.FATAL));
		invoker.setMavenHome(this.mavenHome);
		InvocationResult result;
		try {
			result = invoker.execute(invocation);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		if (result.getExitCode() != 0) {
			throw new MavenInvocationFailedException(outputHandler.lines);
		}
	}

	/**
	 * Exception thrown when an invocation of Maven fails.
	 */
	static final class MavenInvocationFailedException extends Exception {

		private final List<String> outputLines;

		MavenInvocationFailedException(List<String> outputLines) {
			this.outputLines = outputLines;
		}

		List<String> getOutputLines() {
			return this.outputLines;
		}

	}

	private final class CapturingInvocationOutputHandler
			implements InvocationOutputHandler {

		private final List<String> lines = new ArrayList<>();

		@Override
		public void consumeLine(String line) {
			this.lines.add(line);
		}

	}

}
