package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.aries.blueprint.metadata.MutableCollectionMetadata;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.Metadata;

public class MutableCollectionMetadataImpl implements MutableCollectionMetadata<CollectionMetadata> {

	private Class<?> collectionClass;
	private String valueType;
	private List<Metadata> values = new ArrayList<Metadata>();
	
	public Class<?> getCollectionClass() {
		return collectionClass;
	}

	public String getValueType() {
		return valueType;
	}

	public List<Metadata> getValues() {
		return values;
	}

	public CollectionMetadata freeze() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public CollectionMetadata copy() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableCollectionMetadata<CollectionMetadata> collectionClass(Class<?> clazz) {
		this.collectionClass = clazz;
		return this;
	}

	public MutableCollectionMetadata<CollectionMetadata> valueType(String type) {
		this.valueType = type;
		return this;
	}

	public MutableCollectionMetadata<CollectionMetadata> values(Metadata... values) {
		this.values = new ArrayList<Metadata>(Arrays.asList(values));
		return this;
	}

	public MutableCollectionMetadata<CollectionMetadata> values(List<? extends Metadata> values) {
		this.values = new ArrayList<Metadata>(values);
		return this;
	}

}
