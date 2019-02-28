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

package io.spring.bomr.verify;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties Configuration properties} for bom verification.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties(prefix = "bomr.verify", ignoreUnknownFields = false)
public class VerifyProperties {

	/**
	 * Dependencies (groupId:artifactId) to be ignored when verifying the bom.
	 */
	private final Set<String> ignoredDependencies = new HashSet<>();

	/**
	 * Additional repositories to use for dependency resolution when verifying the bom.
	 */
	private final Set<URI> repositories = new HashSet<>();

	public Set<String> getIgnoredDependencies() {
		return this.ignoredDependencies;
	}

	public Set<URI> getRepositories() {
		return this.repositories;
	}

}
