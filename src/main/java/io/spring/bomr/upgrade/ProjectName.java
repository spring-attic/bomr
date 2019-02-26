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

package io.spring.bomr.upgrade;

import io.spring.bomr.upgrade.BomVersions.BomVersion;

/**
 * The name of a {@link Project}.
 *
 * @author Andy Wilkinson
 */
final class ProjectName {

	private final String name;

	ProjectName(BomVersion version) {
		StringBuilder nameBuilder = new StringBuilder();
		String name = version.getProperty();
		if (name.endsWith(".version")) {
			name = name.substring(0, name.length() - ".version".length());
		}
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i == 0 || name.charAt(i - 1) == '-') {
				c = Character.toUpperCase(c);
			}
			if (c == '-') {
				c = ' ';
			}
			nameBuilder.append(c);
		}
		this.name = nameBuilder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProjectName other = (ProjectName) obj;
		return this.name.equals(other.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return this.name;
	}

}
