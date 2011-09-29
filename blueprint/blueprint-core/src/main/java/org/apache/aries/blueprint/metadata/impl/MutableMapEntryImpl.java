package org.apache.aries.blueprint.metadata.impl;

import org.apache.aries.blueprint.metadata.MutableMapEntry;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.NonNullMetadata;

public class MutableMapEntryImpl implements MutableMapEntry<MapEntry> {

	private NonNullMetadata key;
	private Metadata value;
	
	public NonNullMetadata getKey() {
		return key;
	}

	public Metadata getValue() {
		return value;
	}

	public MapEntry freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MapEntry copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableMapEntry<MapEntry> key(NonNullMetadata key) {
		this.key = key;
		return this;
	}

	public MutableMapEntry<MapEntry> value(Metadata value) {
		this.value = value;
		return this;
	}

}
