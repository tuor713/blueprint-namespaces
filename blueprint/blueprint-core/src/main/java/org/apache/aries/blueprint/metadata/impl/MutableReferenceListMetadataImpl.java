package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableReferenceListMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListMetadata;

public class MutableReferenceListMetadataImpl 
	extends MutableServiceReferenceMetadataImpl<ReferenceListMetadata, MutableReferenceListMetadata<ReferenceListMetadata>> 
	implements MutableReferenceListMetadata<ReferenceListMetadata>{

	private int memberType;
	
	public int getMemberType() {
		return memberType;
	}

	public MutableReferenceListMetadata<ReferenceListMetadata> memberType(int type) {
		this.memberType = type;
		return this;
	}

}
