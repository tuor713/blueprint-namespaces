/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.reflect;

import java.util.Collections;
import java.util.List;

import org.apache.aries.blueprint.PassThroughMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;

/**
 * A metadata for environment managers.
 *
 * @version $Rev: 896324 $, $Date: 2010-01-06 06:05:04 +0000 (Wed, 06 Jan 2010) $
 */
public class PassThroughMetadataImpl implements ComponentMetadata, PassThroughMetadata {

    private final Object object;
    private final String id;

    public PassThroughMetadataImpl(String id, Object object) {
        this.id = id;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

	public int getActivation() {
		return ComponentMetadata.ACTIVATION_EAGER;
	}

	public List<String> getDependsOn() {
		return Collections.emptyList();
	}

	public String getId() {
		return id;
	}
}
