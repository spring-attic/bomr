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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import io.spring.bomr.verify.MavenInvoker.MavenInvocationFailedException;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A representation of a Maven bom intended for verification.
 *
 * @author Andy Wilkinson
 */
class VerifiableBom {

	private final List<ManagedDependency> managedDependencies;

	private final String groupId;

	private final String artifactId;

	private final String version;

	VerifiableBom(MavenInvoker mavenInvoker, File bomFile) {
		try {
			File effectiveBomFile = createEffectiveBomFile(mavenInvoker, bomFile);
			Document effectiveBom = parseBom(effectiveBomFile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList managedDependencies = (NodeList) xpath.evaluate(
					"/project/dependencyManagement/dependencies/dependency", effectiveBom,
					XPathConstants.NODESET);
			this.managedDependencies = IntStream.range(0, managedDependencies.getLength())
					.mapToObj(managedDependencies::item)
					.map((node) -> createManagedDependency(xpath, node))
					.collect(Collectors.toList());
			this.groupId = xpath.evaluate("/project/groupId/text()", effectiveBom);
			this.artifactId = xpath.evaluate("/project/artifactId/text()", effectiveBom);
			this.version = xpath.evaluate("/project/version/text()", effectiveBom);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public List<ManagedDependency> getManagedDependencies() {
		return this.managedDependencies;
	}

	String getGroupId() {
		return this.groupId;
	}

	String getArtifactId() {
		return this.artifactId;
	}

	String getVersion() {
		return this.version;
	}

	private ManagedDependency createManagedDependency(XPath xpath, Node node) {
		try {
			String groupId = xpath.evaluate("groupId/text()", node);
			String artifactId = xpath.evaluate("artifactId/text()", node);
			String classifier = xpath.evaluate("classifier/text()", node);
			String type = xpath.evaluate("type/text()", node);
			return new ManagedDependency(groupId, artifactId, classifier, type);
		}
		catch (XPathExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}

	private File createEffectiveBomFile(MavenInvoker mavenInvoker, File bomFile)
			throws IOException, MavenInvocationException {
		File effectiveBomFile = File.createTempFile("effective-", ".pom");
		Properties properties = new Properties();
		properties.setProperty("output", effectiveBomFile.getAbsolutePath());
		try {
			mavenInvoker.invoke(bomFile, properties, "help:effective-pom");
			return effectiveBomFile;
		}
		catch (MavenInvocationFailedException ex) {
			System.err.println("Failed to create effective bom from '"
					+ bomFile.getAbsolutePath() + "':");
			ex.getOutputLines().forEach(System.err::println);
			System.exit(-1);
			return null;
		}
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

}
