/*
 * Copyright 2017 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import io.spring.bomr.upgrade.BomVersions.BomVersion;

/**
 * A project made up of one or more {@link Module Modules}.
 *
 * @author Andy Wilkinson
 */
final class Project {

	private final ProjectName name;

	private final BomVersion version;

	private final List<Module> modules = new ArrayList<>();

	Project(ProjectName name, BomVersion version) {
		this.name = name;
		this.version = version;
	}

	ProjectName getName() {
		return this.name;
	}

	BomVersion getVersion() {
		return this.version;
	}

	List<Module> getModules() {
		return this.modules;
	}

}
