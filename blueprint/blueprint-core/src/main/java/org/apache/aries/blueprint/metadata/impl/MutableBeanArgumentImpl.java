package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableBeanArgument;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.Metadata;

public class MutableBeanArgumentImpl implements MutableBeanArgument<BeanArgument> {

	private int index = 0;
	private Metadata value;
	private String type;
	
	public int getIndex() {
		return index;
	}

	public Metadata getValue() {
		return value;
	}

	public String getValueType() {
		return type;
	}

	public BeanArgument freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public BeanArgument copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableBeanArgument<BeanArgument> value(Metadata value) {
		this.value = value;
		return this;
	}

	public MutableBeanArgument<BeanArgument> valueType(String type) {
		this.type = type;
		return this;
	}

	public MutableBeanArgument<BeanArgument> index(int index) {
		this.index = index;
		return this;
	}

}
