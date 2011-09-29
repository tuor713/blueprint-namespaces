package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableReferenceMetadata;
import org.osgi.service.blueprint.reflect.ReferenceMetadata;

public class MutableReferenceMetadataImpl 
	extends MutableServiceReferenceMetadataImpl<ReferenceMetadata, MutableReferenceMetadata<ReferenceMetadata>>
	implements MutableReferenceMetadata<ReferenceMetadata>{

	private long timeout;
	
	public long getTimeout() {
		return timeout;
	}

	public MutableReferenceMetadata<ReferenceMetadata> timeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

}
