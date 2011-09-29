package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableRefMetadata;
import org.osgi.service.blueprint.reflect.RefMetadata;

public class MutableRefMetadataImpl implements MutableRefMetadata<RefMetadata> {

	private String componentId;
	
	public String getComponentId() {
		return componentId;
	}

	public RefMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public RefMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableRefMetadata<RefMetadata> componentId(String id) {
		componentId = id;
		return this;
	}

}
