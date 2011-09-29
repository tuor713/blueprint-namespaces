package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.aries.blueprint.metadata.MutableMapMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.MapMetadata;

public class MutableMapMetadataImpl implements MutableMapMetadata<MapMetadata> {

	private List<MapEntry> entries;
	private String keyType;
	private String valueType;
	
	public List<MapEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public String getKeyType() {
		return keyType;
	}

	public String getValueType() {
		return valueType;
	}

	public MapMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MapMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableMapMetadata<MapMetadata> keyType(String type) {
		this.keyType = keyType;
		return this;
	}

	public MutableMapMetadata<MapMetadata> valueType(String type) {
		this.valueType = valueType;
		return this;
	}

	public MutableMapMetadata<MapMetadata> entries(MapEntry... entries) {
		return entries(Arrays.asList(entries));
	}

	public MutableMapMetadata<MapMetadata> entries(List<MapEntry> entries) {
		this.entries = new ArrayList<MapEntry>(entries);
		return this;
	}

}
