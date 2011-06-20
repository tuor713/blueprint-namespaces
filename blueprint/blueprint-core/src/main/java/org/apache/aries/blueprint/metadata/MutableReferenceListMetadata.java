package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.ReferenceListMetadata;

public interface MutableReferenceListMetadata<T extends ReferenceListMetadata> 
	extends ReferenceListMetadata, MutableServiceReferenceMetadata<T, MutableReferenceListMetadata<T>>
{
	MutableReferenceListMetadata<T> memberType(int type);
}
