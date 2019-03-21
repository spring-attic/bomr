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

package io.spring.bomr.upgrade;

import io.spring.bomr.BomrProperties;
import io.spring.bomr.github.GitHub;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Bomr's upgrade support.
 *
 * @author Andy Wilkinson
 */
@Configuration
@EnableConfigurationProperties(UpgradeProperties.class)
class UpgradeConfiguration {

	@Bean
	public UpgradeCommand upgradeCommand(GitHub gitHub,
			UpgradeProperties upgradeProperties, BomrProperties bomrProperties) {
		return new UpgradeCommand(gitHub, upgradeProperties, bomrProperties.getBom());
	}

	@Bean
	@ConfigurationPropertiesBinding
	public StringToVersionRangeConverter stringToVersionRangeConverter() {
		return new StringToVersionRangeConverter();
	}

}
