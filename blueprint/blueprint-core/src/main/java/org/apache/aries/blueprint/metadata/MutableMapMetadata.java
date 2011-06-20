package org.apache.aries.blueprint.metadata;

import java.util.List;

import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.MapMetadata;

public interface MutableMapMetadata<T extends MapMetadata> extends MapMetadata, MutableMetadata<T> {

	MutableMapMetadata<T> keyType(String type);
	MutableMapMetadata<T> valueType(String type);
	MutableMapMetadata<T> entries(MapEntry ... entries);
	MutableMapMetadata<T> entries(List<MapEntry> entries);
}
