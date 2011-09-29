package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableBeanProperty;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.Metadata;

public class MutableBeanPropertyImpl implements MutableBeanProperty<BeanProperty> {

	private String name;
	private Metadata value;
	
	public String getName() {
		return name;
	}

	public Metadata getValue() {
		return value;
	}

	public BeanProperty freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public BeanProperty copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableBeanProperty<BeanProperty> name(String name) {
		this.name = name;
		return this;
	}

	public MutableBeanProperty<BeanProperty> value(Metadata value) {
		this.value = value;
		return this;
	}

}
