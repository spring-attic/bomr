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

package io.spring.bomr.upgrade.version;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ReleaseTrainDependencyVersion}.
 *
 * @author Andy Wilkinson
 */
public class ReleaseTrainDependencyVersionTests {

	@Test
	public void parsingOfANonReleaseTrainVersionReturnsNull() {
		assertThat(version("5.1.4.RELEASE")).isNull();
	}

	@Test
	public void parsingOfAReleaseTrainVersionReturnsVersion() {
		assertThat(version("Lovelace-SR3")).isNotNull();
	}

	@Test
	public void isNewerThanWhenReleaseTrainIsNewerShouldReturnTrue() {
		assertThat(version("Lovelace-RELEASE").isNewerThan(version("Kay-SR5"))).isTrue();
	}

	@Test
	public void isNewerThanWhenVersionIsNewerShouldReturnTrue() {
		assertThat(version("Kay-SR10").isNewerThan(version("Kay-SR5"))).isTrue();
	}

	@Test
	public void isNewerThanWhenVersionIsOlderShouldReturnFalse() {
		assertThat(version("Kay-RELEASE").isNewerThan(version("Kay-SR5"))).isFalse();
	}

	@Test
	public void isNewerThanWhenReleaseTrainIsOlderShouldReturnFalse() {
		assertThat(version("Ingalls-RELEASE").isNewerThan(version("Kay-SR5"))).isFalse();
	}

	@Test
	public void isSameMajorAndNewerWhenWhenReleaseTrainIsNewerShouldReturnTrue() {
		assertThat(
				version("Lovelace-RELEASE").isSameMajorAndNewerThan(version("Kay-SR5")))
						.isTrue();
	}

	@Test
	public void isSameMajorAndNewerThanWhenReleaseTrainIsOlderShouldReturnFalse() {
		assertThat(version("Ingalls-RELEASE").isSameMajorAndNewerThan(version("Kay-SR5")))
				.isFalse();
	}

	@Test
	public void isSameMajorAndNewerThanWhenVersionIsNewerShouldReturnTrue() {
		assertThat(version("Kay-SR6").isSameMajorAndNewerThan(version("Kay-SR5")))
				.isTrue();
	}

	@Test
	public void isSameMinorAndNewerThanWhenReleaseTrainIsNewerShouldReturnFalse() {
		assertThat(
				version("Lovelace-RELEASE").isSameMinorAndNewerThan(version("Kay-SR5")))
						.isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenReleaseTrainIsTheSameAndVersionIsNewerShouldReturnTrue() {
		assertThat(version("Kay-SR6").isSameMinorAndNewerThan(version("Kay-SR5")))
				.isTrue();
	}

	@Test
	public void isSameMinorAndNewerThanWhenReleaseTrainAndVersionAreTheSameShouldReturnFalse() {
		assertThat(version("Kay-SR6").isSameMinorAndNewerThan(version("Kay-SR6")))
				.isFalse();
	}

	@Test
	public void isSameMinorAndNewerThanWhenReleaseTrainIsTheSameAndVersionIsOlderShouldReturnFalse() {
		assertThat(version("Kay-SR6").isSameMinorAndNewerThan(version("Kay-SR7")))
				.isFalse();
	}

	private static ReleaseTrainDependencyVersion version(String input) {
		return ReleaseTrainDependencyVersion.parse(input);
	}

}
