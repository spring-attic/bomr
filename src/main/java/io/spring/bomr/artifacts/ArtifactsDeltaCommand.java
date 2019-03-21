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

package io.spring.bomr.artifacts;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import io.spring.bomr.Command;

import org.springframework.util.StringUtils;

/**
 * {@link Command} to show the delta in artifacts of a particular group across two
 * different versions.
 *
 * @author Andy Wilkinson
 */
public class ArtifactsDeltaCommand implements Command {

	private final ArtifactsFinder artifactsFinder;

	ArtifactsDeltaCommand(ArtifactsFinder artifactsFinder) {
		this.artifactsFinder = artifactsFinder;
	}

	@Override
	public String getName() {
		return "artifacts-delta";

	}

	@Override
	public String getDescription() {
		return "Lists the artifacts that have been added and removed from a "
				+ "group across two versions";
	}

	@Override
	public void invoke(String[] args) {
		ArtifactsDeltaCommandArguments arguments = ArtifactsDeltaCommandArguments
				.parse(args);
		Set<String> oldArtifacts = this.artifactsFinder.find(arguments.getRepository(),
				arguments.getGroup(), arguments.getOldVersion());
		Set<String> newArtifacts = this.artifactsFinder.find(arguments.getRepository(),
				arguments.getGroup(), arguments.getNewVersion());
		Set<String> removedArtifacts = difference(oldArtifacts, newArtifacts);
		System.out.println("Removed:");
		System.out.println();
		if (!removedArtifacts.isEmpty()) {
			removedArtifacts.forEach(System.out::println);
		}
		else {
			System.out.println("None");
		}
		System.out.println();
		Set<String> addedArtifacts = difference(newArtifacts, oldArtifacts);
		System.out.println("Added:");
		System.out.println();
		if (!addedArtifacts.isEmpty()) {
			addedArtifacts.forEach((artifact) -> {
				System.out.println("<dependency>");
				System.out.println("\t<groupId>" + arguments.getGroup() + "</groupId>");
				System.out.println("\t<artifactId>" + artifact + "</artifactId>");
				System.out.println(
						"\t<version>" + determineVersion(arguments) + "</version>");
				System.out.println("</dependency>");
			});
		}
		else {
			System.out.println("None");
		}
	}

	private String determineVersion(ArtifactsDeltaCommandArguments arguments) {
		if (StringUtils.hasText(arguments.getVersionProperty())) {
			return "${" + arguments.getVersionProperty() + "}";
		}
		return arguments.getNewVersion();
	}

	private SortedSet<String> difference(Set<String> one, Set<String> two) {
		SortedSet<String> result = new TreeSet<>(one);
		result.removeAll(two);
		return result;
	}

}
