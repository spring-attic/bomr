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

package io.spring.bomr.artifacts;

import java.util.Set;

import io.spring.bomr.Command;

import org.springframework.util.StringUtils;

/**
 * A command to list all of the artifacts that are available with a particular group id
 * and version.
 *
 * @author Andy Wilkinson
 */
class ArtifactsCommand implements Command {

	private final ArtifactsFinder artifactsFinder;

	ArtifactsCommand(ArtifactsFinder artifactsFinder) {
		this.artifactsFinder = artifactsFinder;
	}

	@Override
	public String getName() {
		return "artifacts";
	}

	@Override
	public String getDescription() {
		return "Lists the artifacts in a group with a matching version";
	}

	@Override
	public void invoke(String[] args) {
		ArtifactsCommandArguments arguments = ArtifactsCommandArguments.parse(args);
		Set<String> artifacts = this.artifactsFinder.find(arguments.getRepository(),
				arguments.getGroup(), arguments.getVersion());
		artifacts.forEach((artifact) -> {
			System.out.println("<dependency>");
			System.out.println("\t<groupId>" + arguments.getGroup() + "</groupId>");
			System.out.println("\t<artifactId>" + artifact + "</artifactId>");
			System.out
					.println("\t<version>" + determineVersion(arguments) + "</version>");
			System.out.println("</dependency>");
		});
	}

	private String determineVersion(ArtifactsCommandArguments arguments) {
		if (StringUtils.hasText(arguments.getVersionProperty())) {
			return "${" + arguments.getVersionProperty() + "}";
		}
		return arguments.getVersion();
	}

}
