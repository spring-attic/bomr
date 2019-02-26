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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import io.spring.bomr.upgrade.BomVersions.BomVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.util.FileCopyUtils;

/**
 * A Maven bom.
 *
 * @author Andy Wilkinson
 */
final class Bom {

	private final File bomFile;

	private final Map<ProjectName, Project> managedProjects;

	Bom(File bomFile) {
		this.bomFile = bomFile;
		Document bom = parseBom(bomFile);
		this.managedProjects = extractManagedProjects(bom, new BomVersions(bom));
	}

	private Document parseBom(File bomFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(bomFile);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private Map<ProjectName, Project> extractManagedProjects(Document bom,
			BomVersions versions) {
		Map<ProjectName, Project> projects = new LinkedHashMap<>();
		try {
			collectProjects("/project/dependencyManagement/dependencies/dependency",
					projects, bom, versions);
			collectProjects("/project/build/pluginManagement/plugins/plugin", projects,
					bom, versions);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return projects;
	}

	private void collectProjects(String managedExpression,
			Map<ProjectName, Project> projects, Document bom, BomVersions versions)
			throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String projectGroupId = xpath.evaluate("/project/groupId/text()", bom);
		NodeList managedDependencies = (NodeList) xpath.evaluate(managedExpression, bom,
				XPathConstants.NODESET);
		for (int i = 0; i < managedDependencies.getLength(); i++) {
			Node managedDependency = managedDependencies.item(i);
			String groupId = xpath.evaluate("groupId/text()", managedDependency);
			if (!groupId.equals(projectGroupId)) {
				BomVersion version = versions
						.resolve(xpath.evaluate("version/text()", managedDependency));
				if (version != null) {
					Project project = projects.computeIfAbsent(new ProjectName(version),
							(projectName) -> new Project(projectName, version));
					String artifactId = xpath.evaluate("artifactId/text()",
							managedDependency);
					project.getModules().add(new Module(groupId, artifactId));
				}
			}
		}

	}

	Map<ProjectName, Project> getManagedProjects() {
		return Collections.unmodifiableMap(this.managedProjects);
	}

	void applyUpgrade(Upgrade upgrade) {
		String versionProperty = upgrade.getProject().getVersion().getProperty();
		try {
			String updatedBom = FileCopyUtils.copyToString(new FileReader(this.bomFile))
					.replaceAll("<" + versionProperty + ">.*</" + versionProperty + ">",
							"<" + versionProperty + ">" + upgrade.getVersion() + "</"
									+ versionProperty + ">");
			FileCopyUtils.copy(updatedBom, new FileWriter(this.bomFile));
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	void commit(String message) {
		try {
			if (new ProcessBuilder().command("git", "add", this.bomFile.getAbsolutePath())
					.start().waitFor() != 0) {
				throw new IllegalStateException("git add failed");
			}
			if (new ProcessBuilder().command("git", "commit", "-m", message).start()
					.waitFor() != 0) {
				throw new IllegalStateException("git commit failed");
			}
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
