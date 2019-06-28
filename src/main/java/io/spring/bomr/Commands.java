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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * The available {@link Command Commands}.
 *
 * @author Andy Wilkinson
 */
@Component
class Commands {

	private final Map<String, Command> commands = new HashMap<>();

	Commands(List<Command> commands) {
		commands.forEach((command) -> this.commands.put(command.getName(), command));
	}

	Command get(String name) {
		return this.commands.get(name);
	}

	String describe() {
		int maxLength = this.commands.keySet().stream().map(String::length).max((i1, i2) -> i1.compareTo(i2)).get();
		StringWriter description = new StringWriter();
		PrintWriter printer = new PrintWriter(description);
		this.commands.values().stream().sorted()
				.forEach((command) -> printer.println(describeCommand(command, maxLength + 4)));
		return description.toString();
	}

	private String describeCommand(Command command, int length) {
		return "  " + rightPad(command.getName(), length) + command.getDescription();
	}

	private static String rightPad(String input, int length) {
		StringBuffer buffer = new StringBuffer(input);
		while (buffer.length() < length) {
			buffer.append(" ");
		}
		return buffer.toString();
	}

}
