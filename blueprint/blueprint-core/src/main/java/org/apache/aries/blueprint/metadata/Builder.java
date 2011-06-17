package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.BeanMetadata;

public interface Builder {
	MutableBeanMetadata<BeanMetadata> newBean();
}