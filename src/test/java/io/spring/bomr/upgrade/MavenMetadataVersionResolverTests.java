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

package io.spring.bomr.upgrade;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.aether.version.Version;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MavenMetadataVersionResolver}.
 *
 * @author Andy Wilkinson
 */
public class MavenMetadataVersionResolverTests {

	@Test
	public void versionsAreResolvedFromMavenMetadata() {
		RestTemplate rest = new RestTemplate();
		MockRestServiceServer server = MockRestServiceServer.bindTo(rest).build();
		server.expect(MockRestRequestMatchers.requestTo(
				"http://central.maven.org/maven2/org/springframework/spring-core/maven-metadata.xml"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						new FileSystemResource(new File(
								"src/test/resources/spring-core-maven-metadata.xml")),
						MediaType.TEXT_XML));
		Set<Version> versions = new MavenMetadataVersionResolver(rest,
				Arrays.asList("http://central.maven.org/maven2")).resolveVersions(
						new Module("org.springframework", "spring-core"));
		assertThat(versions.stream().map(Version::toString).collect(Collectors.toList()))
				.containsExactly("4.3.0.RELEASE", "4.3.1.RELEASE", "4.3.2.RELEASE",
						"4.3.3.RELEASE", "4.3.4.RELEASE", "4.3.5.RELEASE",
						"4.3.6.RELEASE", "4.3.7.RELEASE", "4.3.8.RELEASE",
						"4.3.9.RELEASE", "4.3.10.RELEASE", "4.3.11.RELEASE",
						"5.0.0.RELEASE");
	}

	@Test
	public void versionsFromMultipleRepositoriesAreCombined() {
		RestTemplate rest = new RestTemplate();
		MockRestServiceServer server = MockRestServiceServer.bindTo(rest).build();
		server.expect(MockRestRequestMatchers.requestTo(
				"http://repo1.example.com/com/example/core/maven-metadata.xml"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						new FileSystemResource(
								new File("src/test/resources/repo1-maven-metadata.xml")),
								MediaType.TEXT_XML));
		server.expect(MockRestRequestMatchers.requestTo(
				"http://repo2.example.com/com/example/core/maven-metadata.xml"))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
				.andRespond(MockRestResponseCreators.withSuccess(
						new FileSystemResource(
								new File("src/test/resources/repo2-maven-metadata.xml")),
								MediaType.TEXT_XML));
		Set<Version> versions = new MavenMetadataVersionResolver(rest,
				Arrays.asList("http://repo1.example.com", "http://repo2.example.com"))
						.resolveVersions(new Module("com.example", "core"));
		assertThat(versions.stream().map(Version::toString).collect(Collectors.toList()))
				.containsExactly("1.0.0.RELEASE", "1.1.0.RELEASE");
	}

}
