package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.IdRefMetadata;

public interface MutableIdRefMetadata<T extends IdRefMetadata> extends IdRefMetadata, MutableMetadata<T>{
	MutableIdRefMetadata<T> componentId(String id);
}
