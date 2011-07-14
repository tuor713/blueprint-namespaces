package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableValueMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;

public class MutableValueMetadataImpl implements MutableValueMetadata<ValueMetadata>{

	private String value;
	private String type;
	
	public String getStringValue() {
		return value;
	}

	public String getType() {
		return type;
	}

	public ValueMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public ValueMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableValueMetadata<ValueMetadata> stringValue(String value) {
		this.value = value;
		return this;
	}

	public MutableValueMetadata<ValueMetadata> type(String type) {
		this.type = type;
		return this;
	}

}
