package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.aries.blueprint.metadata.MutablePropsMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.PropsMetadata;

public class MutablePropsMetadataImpl implements MutablePropsMetadata<PropsMetadata> {

	private List<MapEntry> entries;
	
	public List<MapEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public PropsMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public PropsMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutablePropsMetadata<PropsMetadata> entries(MapEntry... entries) {
		return entries(Arrays.asList(entries));
	}

	public MutablePropsMetadata<PropsMetadata> entries(List<MapEntry> entries) {
		this.entries = new ArrayList<MapEntry>(entries);
		return this;
	}

}
