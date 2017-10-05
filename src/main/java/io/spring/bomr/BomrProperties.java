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

package io.spring.bomr;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties Configuration properties} for Bomr.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties(prefix = "bomr")
public class BomrProperties {

	private final GithubProperties github = new GithubProperties();

	/**
	 * Returns the GitHub-specific properties.
	 * @return GitHub properties
	 */
	public GithubProperties getGithub() {
		return this.github;
	}

	/**
	 * Properties for configuring access to GitHub.
	 */
	public static class GithubProperties {

		/**
		 * Username for authentication with GitHub.
		 */
		private String username;

		/**
		 * Password for authentication with GitHub.
		 */
		private String password;

		/**
		 * Returns the username used to authenticate with GitHub.
		 * @return the username
		 */
		public String getUsername() {
			return this.username;
		}

		/**
		 * Sets the username used to authenticate with GitHub.
		 * @param username the username
		 */
		public void setUsername(String username) {
			this.username = username;
		}

		/**
		 * Returns the password used to authenticate with GitHub.
		 * @return the passwrd
		 */
		public String getPassword() {
			return this.password;
		}

		/**
		 * Sets the password used to authenticate with GitHub.
		 * @param password the password
		 */
		public void setPassword(String password) {
			this.password = password;
		}

	}

}
