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

import java.util.Optional;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * A {@link DependencyVersion} backed by an {@link ArtifactVersion}.
 *
 * @author Andy Wilkinson
 */
class ArtifactVersionDependencyVersion extends AbstractDependencyVersion {

	private final ArtifactVersion artifactVersion;

	protected ArtifactVersionDependencyVersion(ArtifactVersion artifactVersion) {
		super(new ComparableVersion(artifactVersion.toString()));
		this.artifactVersion = artifactVersion;
	}

	@Override
	public boolean isNewerThan(DependencyVersion other) {
		if (other instanceof ReleaseTrainDependencyVersion) {
			return false;
		}
		return compareTo(other) > 0;
	}

	@Override
	public boolean isSameMajorAndNewerThan(DependencyVersion other) {
		if (other instanceof ReleaseTrainDependencyVersion) {
			return false;
		}
		return extractArtifactVersionDependencyVersion(other)
				.map(this::isSameMajorAndNewerThan).orElse(true);
	}

	private boolean isSameMajorAndNewerThan(ArtifactVersionDependencyVersion other) {
		return this.artifactVersion.getMajorVersion() == other.artifactVersion
				.getMajorVersion() && isNewerThan(other);
	}

	@Override
	public boolean isSameMinorAndNewerThan(DependencyVersion other) {
		if (other instanceof ReleaseTrainDependencyVersion) {
			return false;
		}
		return extractArtifactVersionDependencyVersion(other)
				.map(this::isSameMinorAndNewerThan).orElse(true);
	}

	private boolean isSameMinorAndNewerThan(ArtifactVersionDependencyVersion other) {
		return this.artifactVersion.getMajorVersion() == other.artifactVersion
				.getMajorVersion()
				&& this.artifactVersion.getMinorVersion() == other.artifactVersion
						.getMinorVersion()
				&& isNewerThan(other);
	}

	@Override
	public String toString() {
		return this.artifactVersion.toString();
	}

	private Optional<ArtifactVersionDependencyVersion> extractArtifactVersionDependencyVersion(
			DependencyVersion other) {
		ArtifactVersionDependencyVersion artifactVersion = null;
		if (other instanceof ArtifactVersionDependencyVersion) {
			artifactVersion = (ArtifactVersionDependencyVersion) other;
		}
		return Optional.ofNullable(artifactVersion);
	}

	static ArtifactVersionDependencyVersion parse(String version) {
		ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
		if (artifactVersion.getQualifier() != null
				&& artifactVersion.getQualifier().equals(version)) {
			return null;
		}
		return new ArtifactVersionDependencyVersion(artifactVersion);
	}

}
