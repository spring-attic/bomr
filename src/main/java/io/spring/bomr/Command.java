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

package io.spring.bomr;

/**
 * A Bomr command.
 *
 * @author Andy Wilkinson
 */
public interface Command extends Comparable<Command> {

	/**
	 * Returns the name of the command that is used to invoke it from the command line.
	 *
	 * @return the name of the command
	 */
	String getName();

	/**
	 * Returns the description of the command, shown in CLI usage messages.
	 *
	 * @return the description of the command
	 */
	String getDescription();

	/**
	 * Invokes the command using the given {@code args}.
	 *
	 * @param args the args
	 */
	void invoke(String[] args);

	@Override
	default int compareTo(Command command) {
		return getName().compareTo(command.getName());
	}

}
