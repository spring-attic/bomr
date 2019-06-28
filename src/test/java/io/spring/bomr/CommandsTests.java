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

package io.spring.bomr;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Commands}.
 *
 * @author Andy Wilkinson
 */
public class CommandsTests {

	@Test
	public void commandsAreAvailableByName() {
		Command alpha = mockCommand("alpha");
		Command bravo = mockCommand("bravo");
		Command charlie = mockCommand("charlie");
		Commands commands = new Commands(Arrays.asList(charlie, bravo, alpha));
		assertThat(commands.get("bravo")).isEqualTo(bravo);
	}

	@Test
	public void unknownCommandReturnsNull() {
		Commands commands = new Commands(Collections.emptyList());
		assertThat(commands.get("foo")).isNull();
	}

	@Test
	public void commandsAreDescribedAlphabetically() {
		Command alpha = mockCommand("alpha", "Description of alpha");
		Command bravo = mockCommand("bravo", "Description of bravo");
		Command charlie = mockCommand("charlie", "Description of charlie");
		Commands commands = new Commands(Arrays.asList(charlie, bravo, alpha));
		String description = commands.describe();
		assertThat(description).isEqualTo(String.format("  alpha      Description of alpha%n"
				+ "  bravo      Description of bravo%n" + "  charlie    Description of charlie%n"));
	}

	private Command mockCommand(String name) {
		return mockCommand(name, null);
	}

	private Command mockCommand(String name, String description) {
		return new MockCommand(name, description);
	}

	private static final class MockCommand implements Command {

		private final String name;

		private final String description;

		MockCommand(String name, String description) {
			this.name = name;
			this.description = description;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public void invoke(String[] args) {

		}

	}

}
