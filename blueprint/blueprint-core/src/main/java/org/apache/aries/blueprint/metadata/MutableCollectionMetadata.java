package org.apache.aries.blueprint.metadata;

import java.util.List;

import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.Metadata;

public interface MutableCollectionMetadata<T extends CollectionMetadata> extends CollectionMetadata, MutableMetadata<T> {
	MutableCollectionMetadata<T> collectionClass(Class<?> clazz);
	MutableCollectionMetadata<T> valueType(String type);
	
	MutableCollectionMetadata<T> values(Metadata ... values);
	MutableCollectionMetadata<T> values(List<Metadata> values);
}
