/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.bomr.artifacts;

import java.io.IOException;
import java.net.URI;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Command line arguments for the {@link ArtifactsCommand}.
 *
 * @author Andy Wilkinson
 */
final class ArtifactsCommandArguments {

	private final String group;

	private final String version;

	private final String versionProperty;

	private final URI repository;

	private ArtifactsCommandArguments(String group, String version,
			String versionProperty, URI repository) {
		this.group = group;
		this.version = version;
		this.versionProperty = versionProperty;
		this.repository = (repository != null) ? repository
				: URI.create("https://repo1.maven.org/maven2/");
	}

	static ArtifactsCommandArguments parse(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.formatHelpWith(new BuiltinHelpFormatter(120, 2));
		ArgumentAcceptingOptionSpec<String> versionPropertySpec = optionParser
				.accepts("version-property",
						"Version property to use in generated dependency management")
				.withRequiredArg().ofType(String.class);
		ArgumentAcceptingOptionSpec<URI> repositoryPropertySpec = optionParser
				.accepts("repository", "Repository to query").withRequiredArg()
				.ofType(URI.class);
		try {
			OptionSet parsed = optionParser.parse(args);
			if (parsed.nonOptionArguments().size() != 2) {
				showUsageAndExit(optionParser);
			}
			return new ArtifactsCommandArguments(
					(String) parsed.nonOptionArguments().get(0),
					(String) parsed.nonOptionArguments().get(1),
					parsed.valueOf(versionPropertySpec),
					parsed.valueOf(repositoryPropertySpec));
		}
		catch (Exception ex) {
			showUsageAndExit(optionParser);
		}
		return null;
	}

	private static void showUsageAndExit(OptionParser optionParser) {
		System.err.println("Usage: bomr artifacts <group> <version> [<options>]");
		System.err.println();
		try {
			optionParser.printHelpOn(System.err);
		}
		catch (IOException ex) {
			// Continue
		}
		System.err.println();
		System.exit(-1);
	}

	String getGroup() {
		return this.group;
	}

	String getVersion() {
		return this.version;
	}

	String getVersionProperty() {
		return this.versionProperty;
	}

	URI getRepository() {
		return this.repository;
	}

}
