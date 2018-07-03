/*
 * Copyright 2017 the original author or authors.
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

package io.spring.bomr.upgrade;

import java.io.IOException;
import java.util.List;

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

	private final String bom;

	private final String org;

	private final String repository;

	private final List<String> labels;

	private final String milestone;

	private UpgradeCommandArguments(String bom, String org, String repository,
			List<String> labels, String milestone) {
		this.bom = bom;
		this.org = org;
		this.repository = repository;
		this.labels = labels;
		this.milestone = milestone;
	}

	static UpgradeCommandArguments parse(String[] args) {
		OptionParser optionParser = new OptionParser();
		optionParser.formatHelpWith(new BuiltinHelpFormatter(120, 2));
		ArgumentAcceptingOptionSpec<String> labelSpec = optionParser
				.accepts("label", "Label to apply to upgrade issues").withRequiredArg()
				.ofType(String.class);
		ArgumentAcceptingOptionSpec<String> milestoneSpec = optionParser
				.accepts("milestone", "Milestone to which upgrade issues are assigned")
				.withRequiredArg().ofType(String.class);
		try {
			OptionSet parsed = optionParser.parse(args);
			if (parsed.nonOptionArguments().size() != 3) {
				showUsageAndExit(optionParser);
			}
			return new UpgradeCommandArguments(
					(String) parsed.nonOptionArguments().get(0),
					(String) parsed.nonOptionArguments().get(1),
					(String) parsed.nonOptionArguments().get(2),
					parsed.valuesOf(labelSpec), parsed.valueOf(milestoneSpec));
		}
		catch (Exception ex) {
			showUsageAndExit(optionParser);
		}
		return null;
	}

	private static void showUsageAndExit(OptionParser optionParser) {
		System.err.println("Usage: bomr upgrade <pom> <org> <repository> [<options>]");
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

	String getBom() {
		return this.bom;
	}

	String getOrg() {
		return this.org;
	}

	String getRepository() {
		return this.repository;
	}

	List<String> getLabels() {
		return this.labels;
	}

	String getMilestone() {
		return this.milestone;
	}

}
