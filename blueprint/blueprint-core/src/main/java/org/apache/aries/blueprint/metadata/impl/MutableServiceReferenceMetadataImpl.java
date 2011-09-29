package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.aries.blueprint.metadata.MutableServiceReferenceMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.ServiceReferenceMetadata;

public class MutableServiceReferenceMetadataImpl<T extends ServiceReferenceMetadata, U extends MutableServiceReferenceMetadata<T,?>>
	extends MutableComponentMetadataImpl<T, U>
	implements MutableServiceReferenceMetadata<T,U> {

	private int availability;
	private String componentName;
	private String filter;
	private String interfaceName;
	private Collection<ReferenceListener> listeners = new ArrayList<ReferenceListener>();
	
	public int getAvailability() {
		return availability;
	}

	public String getComponentName() {
		return componentName;
	}

	public String getFilter() {
		return filter;
	}

	public String getInterface() {
		return interfaceName;
	}

	public Collection<ReferenceListener> getReferenceListeners() {
		return Collections.unmodifiableCollection(listeners);
	}

	public T freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public T copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public U availability(int availability) {
		this.availability = availability;
		return (U) this;
	}

	public U serviceInterface(String interfaceClass) {
		this.interfaceName = interfaceClass;
		return (U) this;
	}

	public U componentName(String name) {
		this.componentName = name;
		return (U) this;
	}

	public U filter(String filter) {
		this.filter = filter;
		return (U) this;
	}

	public U addReferenceListener(ReferenceListener listener) {
		this.listeners.add(listener);
		return (U) this;
	}

	public U referenceListeners(ReferenceListener... listeners) {
		this.listeners = new ArrayList<ReferenceListener>(Arrays.asList(listeners));
		return (U) this;
	}

	public U referenceListeners(Collection<ReferenceListener> listeners) {
		this.listeners = new ArrayList<ReferenceListener>(listeners);
		return (U) this;
	}





}
