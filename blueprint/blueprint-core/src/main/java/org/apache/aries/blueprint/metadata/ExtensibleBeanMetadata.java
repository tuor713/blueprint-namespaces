package org.apache.aries.blueprint.metadata;

import org.apache.aries.blueprint.NamespaceHandler;
import org.osgi.service.blueprint.reflect.BeanMetadata;

public interface ExtensibleBeanMetadata extends BeanMetadata {
	
	/** 
	 * Retrieve custom data installed by the given namespace handler
	 */
	Object retrieveCustomData(NamespaceHandler handler);
}
