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

import java.util.SortedSet;

import io.spring.bomr.upgrade.version.DependencyVersion;

/**
 * Resolves the available versions for a {@link Module}.
 *
 * @author Andy Wilkinson
 */
interface VersionResolver {

	/**
	 * Resolves the available versions for the given {@code module}.
	 * @param module the module
	 * @return the available versions
	 */
	SortedSet<DependencyVersion> resolveVersions(Module module);

}
