/*
 * Copyright 2017-2019 the original author or authors.
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

import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.TemplateLoader;
import io.spring.bomr.BomrProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for bom verification.
 *
 * @author Andy Wilkinson
 */
@Configuration
@EnableConfigurationProperties(BomrProperties.class)
class VerificationConfiguration {

	@Bean
	public VerifyCommand verifyCommand(BomrProperties properties, Compiler compiler,
			TemplateLoader templateLoader) {
		return new VerifyCommand(
				new BomVerifier(new MavenInvoker(properties.getMaven().getHome()),
						compiler, templateLoader));
	}

}
