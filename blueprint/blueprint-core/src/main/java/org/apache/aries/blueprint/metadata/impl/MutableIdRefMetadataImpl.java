package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableIdRefMetadata;
import org.osgi.service.blueprint.reflect.IdRefMetadata;

public class MutableIdRefMetadataImpl implements MutableIdRefMetadata<IdRefMetadata> {

	private String componentId;
	
	public String getComponentId() {
		return componentId;
	}

	public IdRefMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public IdRefMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableIdRefMetadata<IdRefMetadata> componentId(String id) {
		this.componentId = id;
		return this;
	}

}
