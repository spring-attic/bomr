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

import io.spring.bomr.upgrade.version.DependencyVersion;
import org.eclipse.aether.version.Version;

/**
 * An upgrade to change a {@link Project} to use a new {@link Version}.
 *
 * @author Andy Wilkinson
 */
final class Upgrade {

	private final Project project;

	private final DependencyVersion version;

	Upgrade(Project project, DependencyVersion version) {
		this.project = project;
		this.version = version;
	}

	Project getProject() {
		return this.project;
	}

	DependencyVersion getVersion() {
		return this.version;
	}

}
