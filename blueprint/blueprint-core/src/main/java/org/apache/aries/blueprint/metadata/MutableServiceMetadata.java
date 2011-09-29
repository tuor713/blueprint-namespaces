package org.apache.aries.blueprint.metadata;

import java.util.Collection;
import java.util.List;

import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.Target;

public interface MutableServiceMetadata<T extends ServiceMetadata> 
	extends MutableComponentMetadata<T, MutableServiceMetadata<T>>, ServiceMetadata {

	MutableServiceMetadata<T> serviceComponent(Target component);
	MutableServiceMetadata<T> interfaces(String ... interfaces);
	MutableServiceMetadata<T> interfaces(List<String> interfaces);
	MutableServiceMetadata<T> autoExport(int autoExport);
	MutableServiceMetadata<T> serviceProperties(MapEntry ... properties);
	MutableServiceMetadata<T> serviceProperties(List<MapEntry> properties);
	MutableServiceMetadata<T> ranking(int rank);
	
	MutableServiceMetadata<T> addRegistrationListener(RegistrationListener listener);
	MutableServiceMetadata<T> registrationListeners(RegistrationListener ... listeners);
	MutableServiceMetadata<T> registrationListeners(Collection<RegistrationListener> listeners);	
	
}
