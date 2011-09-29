package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableRegistrationListener;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.Target;

public class MutableRegistrationListenerImpl implements MutableRegistrationListener<RegistrationListener> {

	private Target listenerComponent;
	private String registrationMethod;
	private String unregistrationMethod;
	
	public Target getListenerComponent() {
		return listenerComponent;
	}

	public String getRegistrationMethod() {
		return registrationMethod;
	}

	public String getUnregistrationMethod() {
		return unregistrationMethod;
	}

	public RegistrationListener freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public RegistrationListener copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableRegistrationListener<RegistrationListener> listenerComponent(Target component) {
		this.listenerComponent = component;
		return this;
	}

	public MutableRegistrationListener<RegistrationListener> registrationMethod(String method) {
		this.registrationMethod = method;
		return this;
	}

	public MutableRegistrationListener<RegistrationListener> unregistrationMethod(String method) {
		this.unregistrationMethod = method;
		return this;
	}

}
