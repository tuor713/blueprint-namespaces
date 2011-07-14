package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.Metadata;

public interface MutableBeanArgument<T extends BeanArgument> extends BeanArgument, MutableMetadata<T> {
	MutableBeanArgument<T> value(Metadata value);
	MutableBeanArgument<T> valueType(String type);
	MutableBeanArgument<T> index(int index);
}
