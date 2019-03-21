/*
 * Copyright 2017 the original author or authors.
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

package io.spring.bomr.upgrade;

import java.io.IOException;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Command line arguments for the {@link UpgradeCommand}.
 *
 * @author Andy Wilkinson
 */
final class UpgradeCommandArguments {

	private final String milestone;

	private UpgradeCommandArguments(String milestone) {
		this.milestone = milestone;
	}

	static UpgradeCommandArguments parse(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.formatHelpWith(new BuiltinHelpFormatter(120, 2));
		ArgumentAcceptingOptionSpec<String> milestoneSpec = optionParser
				.accepts("milestone", "Milestone to which upgrade issues are assigned")
				.withRequiredArg().ofType(String.class);
		try {
			OptionSet parsed = optionParser.parse(args);
			if (parsed.nonOptionArguments().size() != 0) {
				showUsageAndExit(optionParser);
			}
			return new UpgradeCommandArguments(parsed.valueOf(milestoneSpec));
		}
		catch (Exception ex) {
			showUsageAndExit(optionParser);
		}
		return null;
	}

	private static void showUsageAndExit(OptionParser optionParser) {
		System.err.println("Usage: bomr upgrade [<options>]");
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

	String getMilestone() {
		return this.milestone;
	}

}
