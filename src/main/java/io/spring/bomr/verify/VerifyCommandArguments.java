/*
 * Copyright 2017-2018 the original author or authors.
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

package io.spring.bomr.verify;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Command line arguments for the {@link VerifyCommand}.
 *
 * @author Andy Wilkinson
 */
final class VerifyCommandArguments {

	private final Set<String> ignores;

	private final Set<URI> repositoryUris;

	private VerifyCommandArguments(Set<String> ignores, Set<URI> repositoryUris) {
		this.ignores = ignores;
		this.repositoryUris = repositoryUris;
	}

	static VerifyCommandArguments parse(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.formatHelpWith(new BuiltinHelpFormatter(120, 2));
		ArgumentAcceptingOptionSpec<String> ignoreSpec = optionParser
				.accepts("ignore", "groupId:artifactId of a managed dependency to ignore")
				.withRequiredArg().ofType(String.class);
		ArgumentAcceptingOptionSpec<URI> repositorySpec = optionParser
				.accepts("repository",
						"Additional repository to use for dependency resolution")
				.withRequiredArg().ofType(URI.class);
		try {
			OptionSet parsed = optionParser.parse(args);
			if (parsed.nonOptionArguments().size() != 0) {
				showUsageAndExit(optionParser);
			}
			return new VerifyCommandArguments(new HashSet<>(parsed.valuesOf(ignoreSpec)),
					new HashSet<>(parsed.valuesOf(repositorySpec)));
		}
		catch (Exception ex) {
			showUsageAndExit(optionParser);
		}
		return null;
	}

	private static void showUsageAndExit(OptionParser optionParser) {
		System.err.println("Usage: bomr verify [<options>]");
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

	Set<String> getIgnores() {
		return this.ignores;
	}

	Set<URI> getRepositoryUris() {
		return this.repositoryUris;
	}

}
