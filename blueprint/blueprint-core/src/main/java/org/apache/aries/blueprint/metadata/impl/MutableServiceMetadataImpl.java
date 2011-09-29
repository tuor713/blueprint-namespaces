package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.aries.blueprint.metadata.MutableServiceMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.Target;

public class MutableServiceMetadataImpl 
	extends MutableComponentMetadataImpl<ServiceMetadata, MutableServiceMetadata<ServiceMetadata>>
	implements MutableServiceMetadata<ServiceMetadata> {

	private int autoExport;
	private int ranking;
	private Target serviceComponent;
	private List<MapEntry> serviceProperties = new ArrayList<MapEntry>();
	private List<String> interfaces = new ArrayList<String>();
	private List<RegistrationListener> listeners = new ArrayList<RegistrationListener>();
	
	public ServiceMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public ServiceMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public int getAutoExport() {
		return autoExport;
	}

	public List<String> getInterfaces() {
		return Collections.unmodifiableList(interfaces);
	}

	public int getRanking() {
		return ranking;
	}

	public Collection<RegistrationListener> getRegistrationListeners() {
		return Collections.unmodifiableCollection(listeners);
	}

	public Target getServiceComponent() {
		return serviceComponent;
	}

	public List<MapEntry> getServiceProperties() {
		return Collections.unmodifiableList(serviceProperties);
	}

	public MutableServiceMetadata<ServiceMetadata> serviceComponent(
			Target component) {
		this.serviceComponent = component;
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> interfaces(
			String... interfaces) {
		return interfaces(Arrays.asList(interfaces));
	}

	public MutableServiceMetadata<ServiceMetadata> interfaces(
			List<String> interfaces) {
		this.interfaces = new ArrayList<String>(interfaces);
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> autoExport(int autoExport) {
		this.autoExport = autoExport;
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> serviceProperties(
			MapEntry... properties) {
		return serviceProperties(Arrays.asList(properties));
	}

	public MutableServiceMetadata<ServiceMetadata> serviceProperties(
			List<MapEntry> properties) {
		this.serviceProperties = new ArrayList<MapEntry>(properties);
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> ranking(int rank) {
		this.ranking = rank;
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> addRegistrationListener(
			RegistrationListener listener) {
		this.listeners.add(listener);
		return this;
	}

	public MutableServiceMetadata<ServiceMetadata> registrationListeners(
			RegistrationListener... listeners) {
		return registrationListeners(Arrays.asList(listeners));
	}

	public MutableServiceMetadata<ServiceMetadata> registrationListeners(
			Collection<RegistrationListener> listeners) {
		this.listeners = new ArrayList<RegistrationListener>(listeners);
		return this;
	}

}
