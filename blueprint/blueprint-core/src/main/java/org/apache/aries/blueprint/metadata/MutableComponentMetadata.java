package org.apache.aries.blueprint.metadata;

import java.util.List;

import org.osgi.service.blueprint.reflect.ComponentMetadata;

/**
 * @param <T> The product that methods like freeze() and copy() will produce. This can be specialized beyond ComponentMetadat
 *   for subinterfaces
 * @param <U> The result of flow methods. This is needed so that MutableBeanMetadata flow methods can be freely intermixed
 *   with those defined on MutableComponentMetadata. Needs to be at least MutableComponentMetadata.
 */
public interface MutableComponentMetadata<T extends ComponentMetadata, U extends MutableComponentMetadata<T,?>> 
extends ComponentMetadata, MutableMetadata<T> {

	U id(String id);
	U activation(int activation);
	
	U dependsOn(String ... dependencies);
	U dependsOn(List<String> dependencies);
	U addDependsOn(String dependency);
	
}
