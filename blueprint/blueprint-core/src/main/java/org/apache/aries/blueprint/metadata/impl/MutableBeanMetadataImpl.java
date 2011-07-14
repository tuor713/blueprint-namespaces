package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.metadata.ExtensibleBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.Target;

public class MutableBeanMetadataImpl implements MutableBeanMetadata<ExtensibleBeanMetadata> {

	private String id;
	private int activation;
	private List<String> dependencies = new ArrayList<String>();
	private List<BeanArgument> arguments = new ArrayList<BeanArgument>();
	private final Map<NamespaceHandler, Object> customData = new HashMap<NamespaceHandler, Object>();
	private String className;
	private String initMethod;
	private String destroyMethod;
	private List<BeanProperty> properties = new ArrayList<BeanProperty>();
	private String scope;
	private Target factoryComponent;
	private String factoryMethod;
	
	
	
	public MutableBeanMetadata<ExtensibleBeanMetadata> id(String id) {
		this.id = id;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> activation(int activation) {
		this.activation = activation;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> dependsOn(String... dependencies) {
		this.dependencies = new ArrayList<String>(Arrays.asList(dependencies));
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> dependsOn(List<String> dependencies) {
		this.dependencies = new ArrayList<String>(dependencies);
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> addDependsOn(String dependency) {
		this.dependencies.add(dependency);
		return this;
	}

	public int getActivation() {
		return activation;
	}

	public List<String> getDependsOn() {
		return dependencies;
	}

	public String getId() {
		return id;
	}

	public ExtensibleBeanMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public ExtensibleBeanMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public Object retrieveCustomData(NamespaceHandler handler) {
		return customData.get(handler);
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

	public MutableBeanMetadata<ExtensibleBeanMetadata> className(String name) {
		this.className = name;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> initMethod(String method) {
		this.initMethod = method;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> destroyMethod(String method) {
		this.destroyMethod = method;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> arguments(List<BeanArgument> arguments) {
		this.arguments = new ArrayList<BeanArgument>(arguments);
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> arguments(BeanArgument... arguments) {
		this.arguments = new ArrayList<BeanArgument>(Arrays.asList(arguments));
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> addArgument(BeanArgument argument) {
		this.arguments.add(argument);
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> properties(List<BeanProperty> properties) {
		this.properties = new ArrayList<BeanProperty>(properties);
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> properties(BeanProperty... properties) {
		this.properties = new ArrayList<BeanProperty>(Arrays.asList(properties));
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> addProperty(BeanProperty property) {
		this.properties.add(property);
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> factoryMethod(String method) {
		this.factoryMethod = method;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> factoryComponent(Target component) {
		this.factoryComponent = component;
		return this;
	}

	public MutableBeanMetadata<ExtensibleBeanMetadata> scope(String scope) {
		this.scope = scope;
		return this;
	}

	public void addCustomData(NamespaceHandler handler, Object data) {
		customData.put(handler, data);
	}

}
