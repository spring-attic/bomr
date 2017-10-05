/*
 * Copyright 2017 the original author or authors.
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

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import io.spring.bomr.upgrade.BomVersions.BomVersion;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BomVersions}.
 *
 * @author Andy Wilkinson
 */
public class BomVersionsTests {

	@Test
	public void versionIsResolvedFromProperties() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(
						new StringReader("<project><properties><foo>1.2.3</foo>"
								+ "<bar.version>2.3.4</bar.version>"
								+ "</properties></project>")));
		BomVersions versions = new BomVersions(document);
		BomVersion fooVersion = versions.resolve("${foo}");
		assertThat(fooVersion.getProperty()).isEqualTo("foo");
		assertThat(fooVersion.getVersion().toString()).isEqualTo("1.2.3");
		BomVersion barVersion = versions.resolve("${bar.version}");
		assertThat(barVersion.getProperty()).isEqualTo("bar.version");
		assertThat(barVersion.getVersion().toString()).isEqualTo("2.3.4");
	}

	@Test
	public void unknownVersionResolvesToNull() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(
						new StringReader("<project><properties><foo>1.2.3</foo>"
								+ "<bar.version>2.3.4</bar.version>"
								+ "</properties></project>")));
		BomVersions versions = new BomVersions(document);
		assertThat(versions.resolve("${baz.version}")).isNull();
	}

	@Test
	public void indirectVersionsAreResolved() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(
						new StringReader("<project><properties><foo>1.2.3</foo>"
								+ "<bar.version>${foo}</bar.version>"
								+ "</properties></project>")));
		BomVersions versions = new BomVersions(document);
		BomVersion barVersion = versions.resolve("${bar.version}");
		assertThat(barVersion.getProperty()).isEqualTo("foo");
		assertThat(barVersion.getVersion().toString()).isEqualTo("1.2.3");
	}

}
