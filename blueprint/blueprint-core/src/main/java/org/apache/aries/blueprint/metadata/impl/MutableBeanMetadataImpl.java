package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.Target;

public class MutableBeanMetadataImpl extends MutableComponentMetadataImpl<BeanMetadata, MutableBeanMetadata<BeanMetadata>> implements MutableBeanMetadata<BeanMetadata> {

	private List<BeanArgument> arguments = new ArrayList<BeanArgument>();
	private String className;
	private String initMethod;
	private String destroyMethod;
	private List<BeanProperty> properties = new ArrayList<BeanProperty>();
	private String scope;
	private Target factoryComponent;
	private String factoryMethod;
	
	
	
	public BeanMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public BeanMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public List<BeanArgument> getArguments() {
		return arguments;
	}

	public String getClassName() {
		return className;
	}

	public String getDestroyMethod() {
		return destroyMethod;
	}

	public Target getFactoryComponent() {
		return factoryComponent;
	}

	public String getFactoryMethod() {
		return factoryMethod;
	}

	public String getInitMethod() {
		return initMethod;
	}

	public List<BeanProperty> getProperties() {
		return properties;
	}

	public String getScope() {
		return scope;
	}

	public MutableBeanMetadata<BeanMetadata> className(String name) {
		this.className = name;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> initMethod(String method) {
		this.initMethod = method;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> destroyMethod(String method) {
		this.destroyMethod = method;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> arguments(List<BeanArgument> arguments) {
		this.arguments = new ArrayList<BeanArgument>(arguments);
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> arguments(BeanArgument... arguments) {
		this.arguments = new ArrayList<BeanArgument>(Arrays.asList(arguments));
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> addArgument(BeanArgument argument) {
		this.arguments.add(argument);
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> properties(List<BeanProperty> properties) {
		this.properties = new ArrayList<BeanProperty>(properties);
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> properties(BeanProperty... properties) {
		this.properties = new ArrayList<BeanProperty>(Arrays.asList(properties));
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> addProperty(BeanProperty property) {
		this.properties.add(property);
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> factoryMethod(String method) {
		this.factoryMethod = method;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> factoryComponent(Target component) {
		this.factoryComponent = component;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> scope(String scope) {
		this.scope = scope;
		return this;
	}

	public MutableBeanMetadata<BeanMetadata> addProperty(String name, Metadata value) {
		this.properties.add(new MutableBeanPropertyImpl().name(name).value(value));
		return this;
	}
}
