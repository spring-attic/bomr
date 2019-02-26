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

package io.spring.bomr.artifacts;

import java.io.File;
import java.net.URI;
import java.util.Set;

import org.junit.Test;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for {@link ArtifactsFinder}.
 *
 * @author Andy Wilkinson
 */
public class ArtifactsFinderTests {

	private final RestTemplate rest = new RestTemplate();

	private final MockRestServiceServer server = MockRestServiceServer.bindTo(this.rest)
			.ignoreExpectOrder(true).build();

	@Test
	public void findReturnsNamesOfArtifactsWithMatchingVersionAndJarArtifact() {
		configureExpectations(
				new File("src/test/resources/artifacts/org/quartz-scheduler/"));
		Set<String> artifacts = new ArtifactsFinder(this.rest).find(
				URI.create("https://repo1.maven.org/maven2/"), "org.quartz-scheduler",
				"2.3.0");
		assertThat(artifacts).containsExactly("quartz", "quartz-jobs");
	}

	private void configureExpectations(File root) {
		configureExpectations(root,
				"https://repo1.maven.org/maven2/org/quartz-scheduler/");
		this.server.expect(requestTo(endsWith(".jar")))
				.andRespond(withStatus(HttpStatus.NOT_FOUND));
	}

	private void configureExpectations(File source, String base) {
		for (File candidate : source.listFiles()) {
			if (candidate.isFile()) {
				if ("index.html".equals(candidate.getName())) {
					this.server.expect(requestTo(base)).andExpect(method(HttpMethod.GET))
							.andRespond(withSuccess(new FileSystemResource(candidate),
									MediaType.TEXT_HTML));
				}
				else if (candidate.getName().endsWith(".jar")) {
					this.server.expect(requestTo(base + candidate.getName()))
							.andExpect(method(HttpMethod.HEAD)).andRespond(withSuccess());
				}
			}
			else {
				configureExpectations(candidate, base + candidate.getName() + "/");
			}
		}
	}

}
