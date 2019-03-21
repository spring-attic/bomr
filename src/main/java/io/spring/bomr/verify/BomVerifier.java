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

package io.spring.bomr.verify;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.TemplateLoader;
import com.samskivert.mustache.Template;
import io.spring.bomr.verify.MavenInvoker.MavenInvocationFailedException;

import org.springframework.util.StringUtils;

/**
 * Verifies a Maven bom by checking that all of its managed dependencies can be resolved.
 *
 * @author Andy Wilkinson
 */
class BomVerifier {

	private final MavenInvoker mavenInvoker;

	private final Compiler compiler;

	private final TemplateLoader templateLoader;

	BomVerifier(MavenInvoker mavenInvoker, Compiler compiler,
			TemplateLoader templateLoader) {
		this.mavenInvoker = mavenInvoker;
		this.compiler = compiler;
		this.templateLoader = templateLoader;
	}

	void verify(File bom, Set<String> ignoredDependencies, Set<URI> repositoryUris) {
		VerifiableBom verifiableBom = new VerifiableBom(this.mavenInvoker, bom);
		List<ManagedDependency> dependenciesToVerify = verifiableBom
				.getManagedDependencies().stream()
				.filter((dependency) -> !ignoredDependencies.contains(
						dependency.getGroupId() + ":" + dependency.getArtifactId()))
				.collect(Collectors.toList());
		File dependenciesPom = createDependenciesPom(bom, verifiableBom,
				dependenciesToVerify, repositoryUris.stream().map(Repository::new)
						.collect(Collectors.toList()));
		System.out.print("Verifying " + dependenciesToVerify.size() + " dependencies...");
		try {
			this.mavenInvoker.invoke(dependenciesPom, new Properties(),
					"dependency:list");
			System.out.println(" Done.");
		}
		catch (MavenInvocationFailedException ex) {
			System.err.println();
			System.err.println("Dependency resolution failed:");
			System.err.println();
			ex.getOutputLines().forEach(System.err::println);
		}
	}

	private File createDependenciesPom(File bomFile, VerifiableBom bom,
			List<ManagedDependency> dependenciesToVerify,
			List<Repository> additionalRepositories) {
		try {
			Template template = this.compiler
					.compile(this.templateLoader.getTemplate("bom-dependencies"));
			Map<String, Object> context = new HashMap<>();
			context.put("dependencies", dependenciesToVerify);
			List<Repository> repositories = new ArrayList<>(bom.getRepositories());
			repositories.addAll(additionalRepositories);
			context.put("repositories", repositories);
			context.put("parentGroupId", bom.getGroupId());
			context.put("parentArtifactId", bom.getArtifactId());
			context.put("parentVersion", bom.getVersion());
			context.put("parentRelativePath", bomFile.getAbsolutePath());
			context.put("classifierIfAvailable", (Mustache.Lambda) (frag, out) -> {
				String classifier = frag.execute();
				if (StringUtils.hasText(classifier)) {
					out.append("<classifier>");
					out.append(classifier);
					out.append("</classifier>\n");
				}
			});
			File dependenciesPom = File.createTempFile("dependencies-", ".pom");
			try (FileWriter writer = new FileWriter(dependenciesPom)) {
				template.execute(context, writer);
			}
			return dependenciesPom;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
