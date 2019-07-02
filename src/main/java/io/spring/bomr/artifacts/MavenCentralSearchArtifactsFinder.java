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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.springframework.web.client.RestOperations;

/**
 * An {@link ArtifactsFinder} for Maven Central. Finds artifacts by querying Maven Central
 * Search.
 *
 * @author Andy Wilkinson
 */
class MavenCentralSearchArtifactsFinder implements ArtifactsFinder {

	private static final int PAGE_SIZE = 20;

	private static final String URI_TEMPLATE = "https://search.maven.org/solrsearch/select?q=g:{group}+AND+v:{version}&rows={pageSize}&start={start}";

	private final RestOperations rest;

	MavenCentralSearchArtifactsFinder(RestOperations rest) {
		this.rest = rest;
	}

	@Override
	public Set<String> find(String group, String version) {
		List<String> artifacts = new ArrayList<>();
		int total;
		int start = 0;
		do {
			String result = this.rest.getForObject(URI_TEMPLATE, String.class, group, version, PAGE_SIZE, start);
			DocumentContext json = JsonPath.parse(result);
			artifacts.addAll(json.read("$.response.docs.[?('.jar' in @.ec)].a"));
			total = json.read("$.response.numFound");
			start += PAGE_SIZE;
		}
		while (start < total);
		return new TreeSet<String>(artifacts);
	}

}
