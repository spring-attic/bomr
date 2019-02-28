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

package io.spring.bomr.upgrade.version;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ArtifactVersionDependencyVersion}.
 *
 * @author Andy Wilkinson
 */
public class ArtifactVersionDependencyVersionTests {

	@Test
	public void parseWhenVersionIsNotAMavenVersionShouldReturnNull() {
		assertThat(version("1.2.3.1")).isNull();
	}

	@Test
	public void parseWhenVersionIsAMavenVersionShouldReturnAVersion() {
		assertThat(version("1.2.3")).isNotNull();
	}

	@Test
	public void isNewerThanWhenInputIsOlderMajorShouldReturnTrue() {
		assertThat(version("2.1.2").isNewerThan(version("1.9.0"))).isTrue();
	}

	@Test
	public void isNewerThanWhenInputIsOlderMinorShouldReturnTrue() {
		assertThat(version("2.1.2").isNewerThan(version("2.0.2"))).isTrue();
	}

	@Test
	public void isNewerThanWhenInputIsOlderPatchShouldReturnTrue() {
		assertThat(version("2.1.2").isNewerThan(version("2.1.1"))).isTrue();
	}

	@Test
	public void isNewerThanWhenInputIsNewerMajorShouldReturnFalse() {
		assertThat(version("2.1.2").isNewerThan(version("3.2.1"))).isFalse();
	}

	@Test
	public void isSameMajorAndNewerThanWhenMinorIsOlderShouldReturnTrue() {
		assertThat(version("1.10.2").isSameMajorAndNewerThan(version("1.9.0"))).isTrue();
	}

	@Test
	public void isSameMajorAndNewerThanWhenMajorIsOlderShouldReturnFalse() {
		assertThat(version("2.0.2").isSameMajorAndNewerThan(version("1.9.0"))).isFalse();
	}

	@Test
	public void isSameMajorAndNewerThanWhenPatchIsNewerShouldReturnTrue() {
		assertThat(version("2.1.2").isSameMajorAndNewerThan(version("2.1.1"))).isTrue();
	}

	@Test
	public void isSameMajorAndNewerThanWhenMinorIsNewerShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMajorAndNewerThan(version("2.2.1"))).isFalse();
	}

	@Test
	public void isSameMajorAndNewerThanWhenMajorIsNewerShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMajorAndNewerThan(version("3.0.1"))).isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenPatchIsOlderShouldReturnTrue() {
		assertThat(version("1.10.2").isSameMinorAndNewerThan(version("1.10.1"))).isTrue();
	}

	@Test
	public void isSameMinorAndNewerThanWhenMinorIsOlderShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMinorAndNewerThan(version("2.0.1"))).isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenVersionsAreTheSameShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMinorAndNewerThan(version("2.1.2"))).isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenPatchIsNewerShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMinorAndNewerThan(version("2.1.3"))).isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenMinorIsNewerShouldReturnFalse() {
		assertThat(version("2.1.2").isSameMinorAndNewerThan(version("2.0.1"))).isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenMajorIsNewerShouldReturnFalse() {
		assertThat(version("3.1.2").isSameMinorAndNewerThan(version("2.0.1"))).isFalse();
	}

	private ArtifactVersionDependencyVersion version(String version) {
		return ArtifactVersionDependencyVersion.parse(version);
	}

}
