package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.Metadata;

public interface MutableBeanProperty<T extends BeanProperty> extends BeanProperty, MutableMetadata<T> {
	MutableBeanProperty<T> name(String name);
	MutableBeanProperty<T> value(Metadata value);
}
