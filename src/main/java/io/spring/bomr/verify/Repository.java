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

package io.spring.bomr.verify;

import java.net.URI;

/**
 * Representation of a repository that can be declared in a Maven pom.
 *
 * @author Andy Wilkinson
 */
final class Repository {

	private final String id;

	private final URI url;

	private final boolean snapshotsEnabled;

	Repository(String id, URI url, boolean snapshotsEnabled) {
		this.id = id;
		this.url = url;
		this.snapshotsEnabled = snapshotsEnabled;
	}

	/**
	 * Returns the id of the repository.
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Returns the URL of the repository.
	 * @return the URL
	 */
	public URI getUrl() {
		return this.url;
	}

	/**
	 * Whether snapshots are enabled for the repository.
	 * @return {@code true} if snapshots are enabled, otherwise {@code false}
	 */
	public boolean isSnapshotsEnabled() {
		return this.snapshotsEnabled;
	}

}
