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
 * @author Christoph Dreis
 */
final class BomUpgrader {

	private final GitHub gitHub;

	private final VersionResolver versionResolver;

	private final UpgradePolicy upgradePolicy;

	private final List<ProhibitedVersions> prohibitedVersions;

	BomUpgrader(GitHub gitHub, VersionResolver versionResolver, UpgradePolicy upgradePolicy,
			List<ProhibitedVersions> prohibitedVersions) {
		this.gitHub = gitHub;
		this.versionResolver = versionResolver;
		this.upgradePolicy = upgradePolicy;
		this.prohibitedVersions = prohibitedVersions;
	}

	void upgrade(File bomFile, String organization, String repositoryName, List<String> issueLabels,
			String milestoneName, boolean commitsEnabled, boolean issuesEnabled) {
		GitHubRepository repository = getRepository(organization, repositoryName, issuesEnabled);
		validateLabels(repository, issueLabels);
		Milestone milestone = determineMilestone(repository, milestoneName);
		Bom bom = new Bom(bomFile);
		List<Upgrade> upgrades = new InteractiveUpgradeResolver(this.versionResolver, this.upgradePolicy,
				this.prohibitedVersions).resolveUpgrades(bom.getManagedProjects().values());
		upgrades.forEach((upgrade) -> applyUpgrade(upgrade, bom, repository, issueLabels, milestone, commitsEnabled,
				issuesEnabled));
	}

	private GitHubRepository getRepository(String organization, String repositoryName, boolean issuesEnabled) {
		if (!issuesEnabled) {
			return null;
		}
		return this.gitHub.getRepository(organization, repositoryName);
	}

	private void validateLabels(GitHubRepository repository, List<String> labels) {
		if (repository == null) {
			return;
		}
		List<String> availableLabels = repository.getLabels();
		if (!availableLabels.containsAll(labels)) {
			List<String> unknownLabels = new ArrayList<>(labels);
			unknownLabels.removeAll(availableLabels);
			unknownLabels.forEach((label) -> System.err.println("Unknown label '" + label + "'"));
			System.exit(1);
		}
	}

	private Milestone determineMilestone(GitHubRepository repository, String milestoneName) {
		if (milestoneName == null || repository == null) {
			return null;
		}
		List<Milestone> milestones = repository.getMilestones();
		Optional<Milestone> matchingMilestone = milestones.stream()
				.filter((milestone) -> milestone.getName().equals(milestoneName)).findFirst();
		if (!matchingMilestone.isPresent()) {
			System.err.println("Unknown milestone '" + milestoneName + "'");
			System.exit(1);
		}
		return matchingMilestone.get();
	}

	private void applyUpgrade(Upgrade upgrade, Bom bom, GitHubRepository repository, List<String> labels,
			Milestone milestone, boolean commitsEnabled, boolean issuesEnabled) {
		bom.applyUpgrade(upgrade);
		if (!commitsEnabled && !issuesEnabled) {
			return;
		}
		String description = createUpgradeDescription(upgrade);
		if (issuesEnabled) {
			int issueNumber = repository.openIssue(description, labels, milestone);
			description = description + "\n\nCloses gh-" + issueNumber;
		}
		if (commitsEnabled) {
			bom.commit(description);
		}
	}

	private String createUpgradeDescription(Upgrade upgrade) {
		return "Upgrade to " + upgrade.getProject().getName() + " " + upgrade.getVersion();
	}

}
