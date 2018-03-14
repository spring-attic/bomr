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

import io.spring.bomr.Command;

/**
 * A {@link Command} to verify a bom. Verification is performed by checking that every
 * dependency that is managed by the bom can be resolved.
 *
 * @author Andy Wilkinson
 */
class VerifyCommand implements Command {

	private final BomVerifier verifier;

	VerifyCommand(BomVerifier verifier) {
		this.verifier = verifier;
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
		VerifyCommandArguments arguments = VerifyCommandArguments.parse(args);
		File pomFile = new File(arguments.getBom());
		if (!pomFile.exists()) {
			System.err.println("Fatal: pom file does not exist:");
			System.err.println();
			System.err.println("  " + pomFile.getAbsolutePath());
			System.err.println();
			System.exit(-1);
		}
		this.verifier.verify(pomFile, arguments.getIgnores());
	}

}
