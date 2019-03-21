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

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import org.springframework.core.convert.converter.Converter;

/**
 * {@link Converter} to create a {@link VersionRange} from a {@link String}.
 *
 * @author Andy Wilkinson
 */
class StringToVersionRangeConverter implements Converter<String, VersionRange> {

	@Override
	public VersionRange convert(String source) {
		try {
			return VersionRange.createFromVersionSpec(source);
		}
		catch (InvalidVersionSpecificationException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

}
