package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.Target;

public interface MutableReferenceListener<T extends ReferenceListener> extends ReferenceListener, MutableMetadata<T> 
{
	MutableReferenceListener<T> listenerComponent(Target component);
	MutableReferenceListener<T> bindMethod(String method);
	MutableReferenceListener<T> unbindMethod(String method);
}
