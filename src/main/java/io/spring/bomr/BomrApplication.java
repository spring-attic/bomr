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

package io.spring.bomr;

import java.util.Arrays;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main entry point for Bomr.
 *
 * @author Andy Wilkinson
 */
@SpringBootApplication
@EnableConfigurationProperties(BomrProperties.class)
public class BomrApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(
				BomrApplication.class).properties("logging.level.root=warn",
						"spring.config.location=file:.bomr/", "spring.config.name=bomr")
						.bannerMode(Mode.OFF).run(args);
		Commands commands = context.getBean(Commands.class);
		if (args.length == 0) {
			displayUsage();
			System.err.println();
			displayCommandList(commands);
			System.exit(-1);
		}
		Command command = commands.get(args[0]);
		if (command == null) {
			System.err.println("bomr: '" + args[0] + "' is not a bomr command");
			System.err.println();
			displayCommandList(commands);
			System.exit(-1);
		}
		else {
			command.invoke(Arrays.copyOfRange(args, 1, args.length));
		}
	}

	private static void displayUsage() {
		System.err.println("Usage: bomr <command> [<args>]");
	}

	private static void displayCommandList(Commands commands) {
		System.err.println("Commands:");
		System.err.println();
		System.err.println(commands.describe());
		System.err.println();
	}

}
