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

import io.spring.bomr.upgrade.version.DependencyVersion;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link UpgradePolicy}.
 *
 * @author Andy Wilkinson
 */
public class UpgradePolicyTests {

	@Test
	public void anyWhenCandidateIsNewerMajorThanCurrentShouldReturnTrue() throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.ANY.test(version("2.0.0"), version("1.0.0"))).isTrue();
	}

	@Test
	public void anyWhenCandidateIsNewerMinorThanCurrentShouldReturnTrue() throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.ANY.test(version("1.1.0"), version("1.0.0"))).isTrue();
	}

	@Test
	public void anyWhenCandidateIsNewerPatchThanCurrentShouldReturnTrue() throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.ANY.test(version("1.0.1"), version("1.0.0"))).isTrue();
	}

	@Test
	public void anyWhenCandidateIsSameAsCurrentShouldReturnFalse() throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.ANY.test(version("1.0.0"), version("1.0.0"))).isFalse();
	}

	@Test
	public void anyWhenCandidateIsOlderThanCurrentShouldReturnFalse() throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.ANY.test(version("2.0.0"), version("2.0.1"))).isFalse();
	}

	@Test
	public void sameMajorVersionWhenCandidateIsNewerMajorThanCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MAJOR_VERSION.test(version("2.0.0"), version("1.0.0"))).isFalse();
	}

	@Test
	public void sameMajorVersionWhenCandidateIsNewerMinorThanCurrentShouldReturnTrue()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MAJOR_VERSION.test(version("1.1.0"), version("1.0.0"))).isTrue();
	}

	@Test
	public void sameMajorVersionWhenCandidateIsNewerPatchThanCurrentShouldReturnTrue()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MAJOR_VERSION.test(version("1.0.1"), version("1.0.0"))).isTrue();
	}

	@Test
	public void sameMajorVersionWhenCandidateIsSameAsCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MAJOR_VERSION.test(version("1.0.0"), version("1.0.0"))).isFalse();
	}

	@Test
	public void sameMajorVersionWhenCandidateIsOlderThanCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MAJOR_VERSION.test(version("2.0.0"), version("2.1.0"))).isFalse();
	}

	@Test
	public void sameMinorVersionWhenCandidateIsNewerMajorThanCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MINOR_VERSION.test(version("2.0.0"), version("1.0.0"))).isFalse();
	}

	@Test
	public void sameMinorVersionWhenCandidateIsNewerMinorThanCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MINOR_VERSION.test(version("10.13.1"), version("10.14.1"))).isFalse();
	}

	@Test
	public void sameMinorVersionWhenCandidateIsNewerPatchThanCurrentShouldReturnTrue()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MINOR_VERSION.test(version("1.0.1"), version("1.0.0"))).isTrue();
	}

	@Test
	public void sameMinorVersionWhenCandidateIsSameAsCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MINOR_VERSION.test(version("1.0.0"), version("1.0.0"))).isFalse();
	}

	@Test
	public void sameMinorVersionWhenCandidateIsOlderThanCurrentShouldReturnFalse()
			throws InvalidVersionSpecificationException {
		assertThat(UpgradePolicy.SAME_MINOR_VERSION.test(version("2.1.0"), version("2.1.1"))).isFalse();
	}

	private DependencyVersion version(String version) {
		return DependencyVersion.parse(version);
	}

}
