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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for configuring Maven.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties("bomr.maven")
public class MavenProperties {

	/**
	 * Maven's home directory.
	 */
	private File home;

	/**
	 * Returns the location of Maven's home directory.
	 * @return the home directory
	 */
	public File getHome() {
		return this.home;
	}

	/**
	 * Sets the location of Maven's home directory.
	 * @param home the home directory
	 */
	public void setHome(File home) {
		this.home = home;
	}

}
