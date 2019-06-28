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

package io.spring.bomr.artifacts;

import java.net.URI;
import java.util.Set;

import org.springframework.web.client.RestTemplate;

/**
 * An {@code ArtifactsFinder} is used to find artifacts in a repository.
 *
 * @author Andy Wilkinson
 */
interface ArtifactsFinder {

	URI MAVEN_CENTRAL = URI.create("https://repo1.maven.org/maven2/");

	static ArtifactsFinder forRepository(URI repository) {
		return repository.equals(MAVEN_CENTRAL) ? new MavenCentralSearchArtifactsFinder(new RestTemplate())
				: new MavenRepositoryArtifactsFinder(new RestTemplate(), repository);
	}

	/**
	 * Finds the artifacts with the given {@code group} and {@code version} in the
	 * repository.
	 * @param group the group
	 * @param version the version
	 * @return the artifacts in the group with the required version
	 */
	Set<String> find(String group, String version);

}
