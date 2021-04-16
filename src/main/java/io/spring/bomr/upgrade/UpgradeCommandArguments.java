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
import joptsimple.OptionSpec;

/**
 * Command line arguments for the {@link UpgradeCommand}.
 *
 * @author Andy Wilkinson
 */
final class UpgradeCommandArguments {

	private final String milestone;

	private final boolean commitsEnabled;

	private final boolean issuesEnabled;

	private UpgradeCommandArguments(String milestone, boolean commitsEnabled, boolean issuesEnabled) {
		this.milestone = milestone;
		this.commitsEnabled = commitsEnabled;
		this.issuesEnabled = issuesEnabled;
	}

	static UpgradeCommandArguments parse(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.formatHelpWith(new BuiltinHelpFormatter(120, 2));
		ArgumentAcceptingOptionSpec<String> milestoneSpec = optionParser
				.accepts("milestone", "Milestone to which upgrade issues are assigned").withRequiredArg()
				.ofType(String.class);
		OptionSpec<Void> noCommitsSpec = optionParser.accepts("no-commits",
				"Suppress the creation of commits during the upgrade");
		OptionSpec<Void> noIssuesSpec = optionParser.accepts("no-issues",
				"Suppress the creation of issues during the upgrade");
		try {
			OptionSet parsed = optionParser.parse(args);
			if (parsed.nonOptionArguments().size() != 0) {
				showUsageAndExit(optionParser);
			}
			return new UpgradeCommandArguments(parsed.valueOf(milestoneSpec), !parsed.has(noCommitsSpec),
					!parsed.has(noIssuesSpec));
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

	boolean commitsEnabled() {
		return this.commitsEnabled;
	}

	boolean issuesEnabled() {
		return this.issuesEnabled;
	}

}
