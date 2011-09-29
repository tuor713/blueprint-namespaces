package org.apache.aries.blueprint.metadata;

import java.util.Collection;

import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.ServiceReferenceMetadata;

public interface MutableServiceReferenceMetadata<T extends ServiceReferenceMetadata, U extends MutableServiceReferenceMetadata<T,?>>
	extends ServiceReferenceMetadata, MutableComponentMetadata<T, U> {

	U availability(int availability);
	U serviceInterface(String interfaceClass);
	U componentName(String name);
	U filter(String filter);
	
	U addReferenceListener(ReferenceListener listener);
	U referenceListeners(ReferenceListener ... listener);
	U referenceListeners(Collection<ReferenceListener> listener);
}
