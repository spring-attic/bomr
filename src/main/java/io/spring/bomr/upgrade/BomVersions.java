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

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import io.spring.bomr.upgrade.version.DependencyVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The versions declared in the <code>&lt;properties&gt;</code> section of a bom.
 *
 * @author Andy Wilkinson
 */
final class BomVersions {

	private Map<String, String> versions = new HashMap<>();

	BomVersions(Document bom) {
		try {
			NodeList propertyNodes = (NodeList) XPathFactory.newInstance().newXPath()
					.evaluate("/project/properties/*", bom, XPathConstants.NODESET);
			for (int i = 0; i < propertyNodes.getLength(); i++) {
				Node propertyNode = propertyNodes.item(i);
				this.versions.put(propertyNode.getNodeName(),
						propertyNode.getTextContent());
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	BomVersion resolve(String requested) {
		String property = requested;
		String value = requested;
		while (value != null && value.startsWith("${") && value.endsWith("}")) {
			property = value.substring(2, value.length() - 1);
			value = this.versions.get(property);
		}
		if (value == null) {
			return null;
		}
		return new BomVersion(property, DependencyVersion.parse(value));
	}

	/**
	 * An individual version extracted from the <code>&lt;properties&gt;</code> section of
	 * a bom.
	 */
	static class BomVersion {

		private final String property;

		private final DependencyVersion version;

		BomVersion(String property, DependencyVersion version) {
			this.property = property;
			this.version = version;
		}

		String getProperty() {
			return this.property;
		}

		DependencyVersion getVersion() {
			return this.version;
		}

		@Override
		public String toString() {
			return this.property + ":" + this.version;
		}

	}

}
