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

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for configuring upgrades.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties(prefix = "bomr.upgrade", ignoreUnknownFields = false)
public class UpgradeProperties {

	private final Github github = new Github();

	private final List<ProhibitedVersions> prohibited = new ArrayList<>();

	/**
	 * Policy that controls which versions are suggested as possible upgrades.
	 */
	private UpgradePolicy policy = UpgradePolicy.ANY;

	public UpgradePolicy getPolicy() {
		return this.policy;
	}

	public void setPolicy(UpgradePolicy policy) {
		this.policy = policy;
	}

	public Github getGithub() {
		return this.github;
	}

	public List<ProhibitedVersions> getProhibited() {
		return this.prohibited;
	}

	/**
	 * Properties related to GitHub.
	 */
	public static class Github {

		/**
		 * Labels to apply to issues that are opened.
		 */
		private List<String> issueLabels;

		/**
		 * Organization on GitHub the owns the repository where issues should be opened.
		 */
		private String organization;

		/**
		 * Repository on GitHub where issues should be opened.
		 */
		private String repository;

		public List<String> getIssueLabels() {
			return this.issueLabels;
		}

		public void setIssueLabels(List<String> issueLabels) {
			this.issueLabels = issueLabels;
		}

		public String getOrganization() {
			return this.organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		public String getRepository() {
			return this.repository;
		}

		public void setRepository(String repository) {
			this.repository = repository;
		}

	}

}
