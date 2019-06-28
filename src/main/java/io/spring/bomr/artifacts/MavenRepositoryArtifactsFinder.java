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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

/**
 * Finds artifacts in a group with a particular version that are available in a Maven
 * repository.
 *
 * @author Andy Wilkinson
 */
class MavenRepositoryArtifactsFinder implements ArtifactsFinder {

	private final Pattern linkPattern = Pattern.compile("<a href=\"(.*?/)\".*>.*/</a>");

	private final RestOperations rest;

	private final URI repository;

	MavenRepositoryArtifactsFinder(RestOperations rest, URI repository) {
		this.rest = rest;
		this.repository = repository;
	}

	@Override
	public Set<String> find(String group, String version) {
		URI groupUri = this.repository.resolve(group.replace('.', '/') + "/");
		List<String> artifactLinks = extractLinks(groupUri);
		Set<String> artifacts = new TreeSet<>();
		for (String artifactLink : artifactLinks) {
			List<String> versionLinks = extractLinks(groupUri.resolve(artifactLink));
			if (versionLinks.contains(version + "/")) {
				String artifact = artifactLink.substring(0, artifactLink.length() - 1);
				if (jarArtifactExists(group, artifact, version)) {
					artifacts.add(artifact);
				}
			}
		}
		return artifacts;
	}

	private List<String> extractLinks(URI uri) {
		try {
			ResponseEntity<String> response = this.rest.getForEntity(uri, String.class);
			String body = response.getBody();
			List<String> links = new ArrayList<String>();
			Matcher matcher = this.linkPattern.matcher(body);
			while (matcher.find()) {
				String candidate = matcher.group(1);
				if (!"../".equals(candidate)) {
					links.add(candidate);
				}
			}
			return links;
		}
		catch (RestClientException ex) {
			System.err.println(uri + " " + ex.getMessage());
			System.exit(-1);
			return null;
		}
	}

	private boolean jarArtifactExists(String group, String artifact, String version) {
		String jarUrl = this.repository + group.replace('.', '/') + "/" + artifact + "/" + version + "/" + artifact
				+ "-" + version + ".jar";
		try {
			this.rest.headForHeaders(jarUrl);
			return true;
		}
		catch (RestClientException ex) {
			return false;
		}
	}

}
