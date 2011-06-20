package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.NonNullMetadata;

public interface MutableMapEntry<T extends MapEntry> extends MapEntry, MutableMetadata<T> {
	MutableMapEntry<T> key(NonNullMetadata key);
	MutableMapEntry<T> value(Metadata value);
}
