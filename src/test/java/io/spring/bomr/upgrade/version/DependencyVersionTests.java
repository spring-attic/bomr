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
 * Tests for {@link DependencyVersion}.
 *
 * @author Andy Wilkinson
 */
public class DependencyVersionTests {

	@Test
	public void parseWhenValidMavenVersionShouldReturnArtifactVersionDependencyVersion() {
		assertThat(DependencyVersion.parse("1.2.3.Final"))
				.isInstanceOf(ArtifactVersionDependencyVersion.class);
	}

	@Test
	public void parseWhenReleaseTrainShouldReturnReleaseTrainDependencyVersion() {
		assertThat(DependencyVersion.parse("Ingalls-SR5"))
				.isInstanceOf(ReleaseTrainDependencyVersion.class);
	}

	@Test
	public void parseWhenMavenLikeVersionWithNumericQualifieShouldReturnNumericQualifierDependencyVersion() {
		assertThat(DependencyVersion.parse("1.2.3.4"))
				.isInstanceOf(NumericQualifierDependencyVersion.class);
	}

	@Test
	public void parseWhenVersionWithLeadingZeroesShouldReturnLeadingZeroesDependencyVersion() {
		assertThat(DependencyVersion.parse("1.4.01"))
				.isInstanceOf(LeadingZeroesDependencyVersion.class);
	}

	@Test
	public void parseWhenVersionWithCombinedPatchAndQualifierShouldReturnCombinedPatchAndQualifierDependencyVersion() {
		assertThat(DependencyVersion.parse("4.0.0M4"))
				.isInstanceOf(CombinedPatchAndQualifierDependencyVersion.class);
	}

}
