package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.RefMetadata;

public interface MutableRefMetadata<T extends RefMetadata> extends RefMetadata, MutableMetadata<T> {
	MutableRefMetadata<T> componentId(String id);
}
