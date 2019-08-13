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

import java.io.File;
import java.util.Set;

import org.junit.Test;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link MavenCentralSearchArtifactsFinder}.
 *
 * @author Andy Wilkinson
 */
public class MavenCentralSearchArtifactsFinderTests {

	private final RestTemplate rest = new RestTemplate();

	private final MockRestServiceServer server = MockRestServiceServer.bindTo(this.rest).ignoreExpectOrder(true)
			.build();

	@Test
	public void findReturnsNamesOfArtifactsWithMatchingVersionAndJarArtifact() {
		configureExpectations(new File("src/test/resources/artifacts/org/quartz-scheduler/"));
		Set<String> artifacts = new MavenCentralSearchArtifactsFinder(this.rest).find("org.infinispan", "9.4.15.Final");
		assertThat(artifacts).hasSize(82);
	}

	private void configureExpectations(File root) {
		for (int i = 0; i < 7; i++) {
			this.server.expect(requestTo(
					"https://search.maven.org/solrsearch/select?q=g:org.infinispan+AND+v:9.4.15.Final&rows=20&start="
							+ (i * 20)))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(
							new FileSystemResource(
									new File("src/test/resources/artifacts/org/infinispan/page" + (i + 1) + ".json")),
							MediaType.APPLICATION_JSON));
		}
	}

}
