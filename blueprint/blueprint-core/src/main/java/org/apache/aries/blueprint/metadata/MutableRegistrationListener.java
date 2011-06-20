package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.Target;

public interface MutableRegistrationListener<T extends RegistrationListener>
	extends RegistrationListener, MutableMetadata<T> {

	MutableRegistrationListener<T> listenerComponent(Target component);
	MutableRegistrationListener<T> registrationMethod(String method);
	MutableRegistrationListener<T> unregistrationMethod(String method);	
	
}
