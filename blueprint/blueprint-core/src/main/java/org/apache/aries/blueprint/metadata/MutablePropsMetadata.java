package org.apache.aries.blueprint.metadata;

import java.util.List;

import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.PropsMetadata;

public interface MutablePropsMetadata<T extends PropsMetadata> extends PropsMetadata, MutableMetadata<T> {
	MutablePropsMetadata<T> entries(MapEntry ... entries);
	MutablePropsMetadata<T> entries(List<MapEntry> entries);	
}
