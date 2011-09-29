package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.IdRefMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.MapMetadata;
import org.osgi.service.blueprint.reflect.PropsMetadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListener;
import org.osgi.service.blueprint.reflect.ReferenceMetadata;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;

public interface Builder {
	MutableBeanMetadata<BeanMetadata> newBean();
	MutableBeanArgument<BeanArgument> newBeanArgument();
	MutableBeanProperty<BeanProperty> newBeanProperty();
	
	MutableReferenceMetadata<ReferenceMetadata> newReference();
	MutableReferenceListMetadata<ReferenceListMetadata> newRefList();
	MutableServiceMetadata<ServiceMetadata> newService();
	
	MutableRegistrationListener<RegistrationListener> newRegistrationListener();
	MutableReferenceListener<ReferenceListener> newReferenceListener();
	
	MutableRefMetadata<RefMetadata> newRef();
	MutableIdRefMetadata<IdRefMetadata> newIdRef();
	
	MutableCollectionMetadata<CollectionMetadata> newCollection();
	MutableMapMetadata<MapMetadata> newMap();
	MutablePropsMetadata<PropsMetadata> newProps();
	MutableValueMetadata<ValueMetadata> newValue();
	
	MutableMapEntry<MapEntry> newMapEntry();
}

