package org.apache.aries.blueprint.metadata;

import org.apache.aries.blueprint.NamespaceHandler;

public interface ExtensibleMetadata {
	
	/** 
	 * Retrieve custom data installed by the given namespace handler instance or class
	 */
	Object retrieveCustomData(NamespaceHandler handler, Object key);
	Object retrieveCustomData(Class<? extends NamespaceHandler> clazz, Object key);
}
