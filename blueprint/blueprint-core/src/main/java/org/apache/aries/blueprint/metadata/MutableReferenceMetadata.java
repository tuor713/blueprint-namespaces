package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.ReferenceMetadata;

public interface MutableReferenceMetadata<T extends ReferenceMetadata> 
	extends ReferenceMetadata, MutableServiceReferenceMetadata<T, MutableReferenceMetadata<T>>
{
	MutableReferenceMetadata<T> timeout(long timeout);
}
