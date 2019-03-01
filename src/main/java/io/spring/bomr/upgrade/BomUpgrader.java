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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.spring.bomr.github.GitHub;
import io.spring.bomr.github.GitHubRepository;
import io.spring.bomr.github.Milestone;

/**
 * Handles the process of upgrading the versions of the dependencies managed by a bom.
 *
 * @author Andy Wilkinson
 */
final class BomUpgrader {

	private final GitHub gitHub;

	private final VersionResolver versionResolver;

	private final UpgradePolicy upgradePolicy;

	private final List<ProhibitedVersions> prohibitedVersions;

	BomUpgrader(GitHub gitHub, VersionResolver versionResolver,
			UpgradePolicy upgradePolicy, List<ProhibitedVersions> prohibitedVersions) {
		this.gitHub = gitHub;
		this.versionResolver = versionResolver;
		this.upgradePolicy = upgradePolicy;
		this.prohibitedVersions = prohibitedVersions;
	}

	void upgrade(File bomFile, String organization, String repositoryName,
			List<String> labels, String milestoneName) {
		GitHubRepository repository = this.gitHub.getRepository(organization,
				repositoryName);
		List<String> availableLabels = repository.getLabels();
		if (!availableLabels.containsAll(labels)) {
			List<String> unknownLabels = new ArrayList<>(labels);
			unknownLabels.removeAll(availableLabels);
			unknownLabels.forEach(
					(label) -> System.err.println("Unknown label '" + label + "'"));
			System.exit(1);
		}
		Milestone milestone = determineMilestone(repository, milestoneName);
		Bom bom = new Bom(bomFile);
		List<Upgrade> upgrades = new InteractiveUpgradeResolver(this.versionResolver,
				this.upgradePolicy, this.prohibitedVersions)
						.resolveUpgrades(bom.getManagedProjects().values());
		upgrades.forEach(
				(upgrade) -> applyUpgrade(upgrade, bom, repository, labels, milestone));
	}

	private Milestone determineMilestone(GitHubRepository repository,
			String milestoneName) {
		if (milestoneName == null) {
			return null;
		}
		List<Milestone> milestones = repository.getMilestones();
		Optional<Milestone> matchingMilestone = milestones.stream()
				.filter((milestone) -> milestone.getName().equals(milestoneName))
				.findFirst();
		if (!matchingMilestone.isPresent()) {
			System.err.println("Unknown milestone '" + milestoneName + "'");
			System.exit(1);
		}
		return matchingMilestone.get();
	}

	private void applyUpgrade(Upgrade upgrade, Bom bom, GitHubRepository repository,
			List<String> labels, Milestone milestone) {
		String description = "Upgrade to " + upgrade.getProject().getName() + " "
				+ upgrade.getVersion();
		int issueNumber = repository.openIssue(description, labels, milestone);
		bom.applyUpgrade(upgrade);
		bom.commit(description + "\n\nCloses gh-" + issueNumber);
	}

}
