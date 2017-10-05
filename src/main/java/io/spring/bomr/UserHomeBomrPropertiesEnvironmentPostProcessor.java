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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * {@link EnvironmentPostProcessor} that loads a {@code .bomr.properties} file from the
 * user's home directory.
 *
 * @author Andy Wilkinson
 */
final class UserHomeBomrPropertiesEnvironmentPostProcessor
		implements EnvironmentPostProcessor {

	private Log logger = LogFactory
			.getLog(UserHomeBomrPropertiesEnvironmentPostProcessor.class);

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		File bomrPropertiesFile = new File(System.getProperty("user.home"),
				".bomr.properties");
		if (bomrPropertiesFile.exists()) {
			Properties bomrProperties = new Properties();
			try (FileReader reader = new FileReader(bomrPropertiesFile)) {
				bomrProperties.load(reader);
			}
			catch (IOException ex) {
				this.logger.warn(
						"Failed to load '" + bomrPropertiesFile.getAbsolutePath() + "'",
						ex);
			}
			environment.getPropertySources()
					.addLast(new PropertiesPropertySource("bomr", bomrProperties));
		}
	}

}
