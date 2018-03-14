/*
 * Copyright 2017-2018 the original author or authors.
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

package io.spring.bomr.verify;

import org.springframework.util.StringUtils;

/**
 * Details of a dependency managed by a bom.
 *
 * @author Andy Wilkinson
 */
class ManagedDependency {

	private final String groupId;

	private final String artifactId;

	private final String classifier;

	private final String type;

	ManagedDependency(String groupId, String artifactId, String classifier, String type) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.classifier = classifier;
		this.type = StringUtils.hasText(type) ? type : "jar";
	}

	String getGroupId() {
		return this.groupId;
	}

	String getArtifactId() {
		return this.artifactId;
	}

	String getClassifier() {
		return this.classifier;
	}

	String getType() {
		return this.type;
	}

}
