package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableReferenceListener;
import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.Target;

public class MutableReferenceListenerImpl implements MutableReferenceListener<ReferenceListener> {

	private String bindMethod;
	private Target listenerComponent;
	private String unbindMethod;
	
	public String getBindMethod() {
		return bindMethod;
	}

	public Target getListenerComponent() {
		return listenerComponent;
	}

	public String getUnbindMethod() {
		return unbindMethod;
	}

	public ReferenceListener freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public ReferenceListener copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableReferenceListener<ReferenceListener> listenerComponent(Target component) {
		this.listenerComponent = component;
		return this;
	}

	public MutableReferenceListener<ReferenceListener> bindMethod(String method) {
		this.bindMethod = method;
		return this;
	}

	public MutableReferenceListener<ReferenceListener> unbindMethod(String method) {
		this.unbindMethod = method;
		return this;
	}

}
