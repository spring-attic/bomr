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
 * Tests for {@link NumericQualifierDependencyVersion}.
 *
 * @author Andy Wilkinson
 */
public class NumericQualifierDependencyVersionTests {

	@Test
	public void isNewerThanOnVersionWithNumericQualifierWhenInputHasNoQualifierShouldReturnTrue() {
		assertThat(version("2.9.9.20190806").isNewerThan(DependencyVersion.parse("2.9.9"))).isTrue();
	}

	@Test
	public void isNewerThanOnVersionWithNumericQualifierWhenInputHasOlderQualifierShouldReturnTrue() {
		assertThat(version("2.9.9.20190806").isNewerThan(version("2.9.9.20190805"))).isTrue();
	}

	@Test
	public void isNewerThanOnVersionWithNumericQualifierWhenInputHasNewerQualifierShouldReturnFalse() {
		assertThat(version("2.9.9.20190806").isNewerThan(version("2.9.9.20190807"))).isFalse();
	}

	@Test
	public void isNewerThanOnVersionWithNumericQualifierWhenInputHasSameQualifierShouldReturnFalse() {
		assertThat(version("2.9.9.20190806").isNewerThan(version("2.9.9.20190806"))).isFalse();
	}

	private NumericQualifierDependencyVersion version(String version) {
		return NumericQualifierDependencyVersion.parse(version);
	}

}
