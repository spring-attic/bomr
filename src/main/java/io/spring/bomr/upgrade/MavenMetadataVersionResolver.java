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

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import io.spring.bomr.upgrade.version.DependencyVersion;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * A {@link VersionResolver} that examines {@code maven-metadata.xml} to determine the
 * available versions.
 *
 * @author Andy Wilkinson
 */
final class MavenMetadataVersionResolver implements VersionResolver {

	private final RestTemplate rest;

	private final List<String> repositoryUrls;

	MavenMetadataVersionResolver(List<String> repositoryUrls) {
		this(new RestTemplate(), repositoryUrls);
	}

	MavenMetadataVersionResolver(RestTemplate restTemplate, List<String> repositoryUrls) {
		this.rest = restTemplate;
		this.repositoryUrls = repositoryUrls;
	}

	@Override
	public SortedSet<DependencyVersion> resolveVersions(Module module) {
		Set<String> versions = new HashSet<String>();
		for (String repositoryUrl : this.repositoryUrls) {
			versions.addAll(resolveVersions(module, repositoryUrl));
		}
		return new TreeSet<>(versions.stream().map(DependencyVersion::parse)
				.collect(Collectors.toSet()));
	}

	private Set<String> resolveVersions(Module module, String repositoryUrl) {
		Set<String> versions = new HashSet<String>();
		String url = repositoryUrl + "/" + module.getGroupId().replace('.', '/') + "/"
				+ module.getArtifactId() + "/maven-metadata.xml";
		try {
			String metadata = this.rest.getForObject(url, String.class);
			Document metadataDocument = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new StringReader(metadata)));
			NodeList versionNodes = (NodeList) XPathFactory.newInstance().newXPath()
					.evaluate("/metadata/versioning/versions/version", metadataDocument,
							XPathConstants.NODESET);
			for (int i = 0; i < versionNodes.getLength(); i++) {
				versions.add(versionNodes.item(i).getTextContent());
			}
		}
		catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
				System.err.println("Failed to download maven-metadata.xml for " + module
						+ " from " + url + ": " + ex.getMessage());
			}
		}
		catch (Exception ex) {
			System.err.println("Failed to resolve versions for module " + module
					+ " in repository " + repositoryUrl + ": " + ex.getMessage());
		}
		return versions;
	}

}
