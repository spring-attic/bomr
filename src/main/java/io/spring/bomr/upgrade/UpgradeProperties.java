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

package io.spring.bomr.upgrade;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for configuring upgrades.
 *
 * @author Andy Wilkinson
 */
@ConfigurationProperties(prefix = "bomr.upgrade", ignoreUnknownFields = false)
public class UpgradeProperties {

	/**
	 * Policy that controls which versions are suggested as possible upgrades.
	 */
	private UpgradePolicy policy = UpgradePolicy.ANY;

	public UpgradePolicy getPolicy() {
		return this.policy;
	}

	public void setPolicy(UpgradePolicy policy) {
		this.policy = policy;
	}

}
