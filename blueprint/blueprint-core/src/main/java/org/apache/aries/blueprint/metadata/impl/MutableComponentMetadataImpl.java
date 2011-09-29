package org.apache.aries.blueprint.metadata.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.metadata.MutableComponentMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;

public abstract class MutableComponentMetadataImpl<T extends ComponentMetadata, U extends MutableComponentMetadata<T,?>> implements MutableComponentMetadata<T, U> {

	private String id;
	private int activation;
	private List<String> dependencies = new ArrayList<String>();
	private final Map<Key,Object> customData = new ConcurrentHashMap<Key, Object>();
	
	private static class Key {
		private final Object nsHandler;
		private final Object key;
		
		public Key(Object nsHandler, Object key) {
			this.nsHandler = nsHandler;
			this.key = key;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result
					+ ((nsHandler == null) ? 0 : nsHandler.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (nsHandler == null) {
				if (other.nsHandler != null)
					return false;
			} else if (!nsHandler.equals(other.nsHandler))
				return false;
			return true;
		}
	}
	
	public U id(String id) {
		this.id = id;
		return (U) this;
	}

	public U activation(int activation) {
		this.activation = activation;
		return (U) this;
	}

	public U dependsOn(String... dependencies) {
		this.dependencies = new ArrayList<String>(Arrays.asList(dependencies));
		return (U) this;
	}

	public U dependsOn(List<String> dependencies) {
		this.dependencies = new ArrayList<String>(dependencies);
		return (U) this;
	}

	public U addDependsOn(String dependency) {
		this.dependencies.add(dependency);
		return (U) this;
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
	
	public Object retrieveCustomData(NamespaceHandler handler, Object key) {
		return customData.get(new Key(handler, key));
	}

	public Object retrieveCustomData(Class<? extends NamespaceHandler> clazz, Object key) {
		return customData.get(new Key(clazz, key));
	}

	public void addCustomData(NamespaceHandler handler, Object key, Object data) {
		customData.put(new Key(handler, key), data);
	}

	public void addCustomData(Class<? extends NamespaceHandler> handler, Object key, Object data) {
		customData.put(new Key(handler, key), data);
	}

	
}
