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

package io.spring.bomr.upgrade;

import java.io.File;
import java.util.Arrays;

import io.spring.bomr.Command;
import io.spring.bomr.github.GitHub;

/**
 * A {@link Command} for upgrading the versions of the plugins and dependencies managed by
 * a bom.
 *
 * @author Andy Wilkinson
 */
final class UpgradeCommand implements Command {

	private final GitHub gitHub;

	private final UpgradeProperties properties;

	private final File bom;

	UpgradeCommand(GitHub gitHub, UpgradeProperties upgradeProperties, File bom) {
		this.gitHub = gitHub;
		this.properties = upgradeProperties;
		this.bom = bom;
	}

	@Override
	public String getName() {
		return "upgrade";
	}

	@Override
	public String getDescription() {
		return "Upgrades the versions of the plugins and dependencies managed by a bom";
	}

	@Override
	public void invoke(String[] args) {
		UpgradeCommandArguments arguments = UpgradeCommandArguments.parse(args);
		if (this.bom == null) {
			System.err.println();
			System.err.println("Fatal: bomr.bom has not been configured");
			System.err.println();
			System.err.println("Check your configuration in .bomr/bomr.(properties|yaml)");
			System.err.println();
			System.exit(-1);
		}
		if (!this.bom.exists()) {
			System.err.println();
			System.err.println("Fatal: bom file does not exist:");
			System.err.println();
			System.err.println("  " + this.bom.getAbsolutePath());
			System.err.println();
			System.err.println("Check your configuration in .bomr/bomr.(properties|yaml)");
			System.err.println();
			System.exit(-1);
		}
		new BomUpgrader(this.gitHub, new MavenMetadataVersionResolver(Arrays.asList("https://repo1.maven.org/maven2/")),
				this.properties.getPolicy(), this.properties.getProhibited()).upgrade(this.bom,
						this.properties.getGithub().getOrganization(), this.properties.getGithub().getRepository(),
						this.properties.getGithub().getIssueLabels(), arguments.getMilestone(),
						arguments.commitsEnabled(), arguments.issuesEnabled());
	}

}
