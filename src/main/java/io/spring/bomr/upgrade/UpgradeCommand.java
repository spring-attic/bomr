/*
 * Copyright 2017-2019 the original author or authors.
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

import java.io.File;
import java.util.Arrays;

import io.spring.bomr.Command;
import io.spring.bomr.github.GitHub;

import org.springframework.stereotype.Component;

/**
 * A {@link Command} for upgrading the versions of the plugins and dependencies managed by
 * a bom.
 *
 * @author Andy Wilkinson
 */
@Component
final class UpgradeCommand implements Command {

	private final GitHub gitHub;

	UpgradeCommand(GitHub gitHub) {
		this.gitHub = gitHub;
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
		File pomFile = new File(arguments.getBom());
		if (!pomFile.exists()) {
			System.err.println("Fatal: pom file does not exist:");
			System.err.println();
			System.err.println("  " + pomFile.getAbsolutePath());
			System.err.println();
			System.exit(-1);
		}
		new BomUpgrader(this.gitHub,
				new MavenMetadataVersionResolver(
						Arrays.asList("http://central.maven.org/maven2"))).upgrade(
								pomFile, arguments.getOrg(), arguments.getRepository(),
								arguments.getLabels(), arguments.getMilestone());
	}

}
