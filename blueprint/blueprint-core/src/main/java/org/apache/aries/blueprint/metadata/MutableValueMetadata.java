package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.ValueMetadata;

public interface MutableValueMetadata<T extends ValueMetadata> extends ValueMetadata, MutableMetadata<T> {
	MutableValueMetadata<T> stringValue(String value);
	MutableValueMetadata<T> type(String type);
}
