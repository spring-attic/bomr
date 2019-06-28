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
import java.net.URI;
import java.util.Set;

import io.spring.bomr.Command;

/**
 * A {@link Command} to verify a bom. Verification is performed by checking that every
 * dependency that is managed by the bom can be resolved.
 *
 * @author Andy Wilkinson
 */
class VerifyCommand implements Command {

	private final BomVerifier verifier;

	private final File bom;

	private final Set<String> ignoredDependencies;

	private final Set<URI> repositories;

	VerifyCommand(BomVerifier verifier, File bom, Set<String> ignoredDependencies, Set<URI> repositories) {
		this.verifier = verifier;
		this.bom = bom;
		this.ignoredDependencies = ignoredDependencies;
		this.repositories = repositories;
	}

	@Override
	public String getName() {
		return "verify";
	}

	@Override
	public String getDescription() {
		return "Verifies the dependencies managed by a bom";
	}

	@Override
	public void invoke(String[] args) {
		if (this.bom == null) {
			System.err.println();
			System.err.println("Fatal: bomr.bom has not been configured");
			System.err.println();
			System.err.println("Check your onfiguration in .bomr/bomr.(properties|yaml)");
			System.err.println();
			System.exit(-1);
		}
		if (!this.bom.exists()) {
			System.err.println();
			System.err.println("Fatal: bom file does not exist:");
			System.err.println();
			System.err.println("  " + this.bom.getAbsolutePath());
			System.err.println();
			System.err.println("Check your onfiguration in .bomr/bomr.(properties|yaml)");
			System.err.println();
			System.exit(-1);
		}
		this.verifier.verify(this.bom, this.ignoredDependencies, this.repositories);
	}

}
