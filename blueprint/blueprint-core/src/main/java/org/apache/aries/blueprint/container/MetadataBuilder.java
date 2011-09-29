package org.apache.aries.blueprint.container;

import org.apache.aries.blueprint.metadata.Builder;
import org.apache.aries.blueprint.metadata.MutableBeanArgument;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableBeanProperty;
import org.apache.aries.blueprint.metadata.MutableCollectionMetadata;
import org.apache.aries.blueprint.metadata.MutableIdRefMetadata;
import org.apache.aries.blueprint.metadata.MutableMapEntry;
import org.apache.aries.blueprint.metadata.MutableMapMetadata;
import org.apache.aries.blueprint.metadata.MutablePropsMetadata;
import org.apache.aries.blueprint.metadata.MutableRefMetadata;
import org.apache.aries.blueprint.metadata.MutableReferenceListMetadata;
import org.apache.aries.blueprint.metadata.MutableReferenceListener;
import org.apache.aries.blueprint.metadata.MutableReferenceMetadata;
import org.apache.aries.blueprint.metadata.MutableRegistrationListener;
import org.apache.aries.blueprint.metadata.MutableServiceMetadata;
import org.apache.aries.blueprint.metadata.MutableValueMetadata;
import org.apache.aries.blueprint.metadata.impl.MutableBeanArgumentImpl;
import org.apache.aries.blueprint.metadata.impl.MutableBeanMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableBeanPropertyImpl;
import org.apache.aries.blueprint.metadata.impl.MutableCollectionMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableIdRefMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableMapEntryImpl;
import org.apache.aries.blueprint.metadata.impl.MutableMapMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutablePropsMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableRefMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableReferenceListMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableReferenceListenerImpl;
import org.apache.aries.blueprint.metadata.impl.MutableReferenceMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableRegistrationListenerImpl;
import org.apache.aries.blueprint.metadata.impl.MutableServiceMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableValueMetadataImpl;
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

public class MetadataBuilder implements Builder {

	public MutableBeanMetadata<BeanMetadata> newBean() {
		return new MutableBeanMetadataImpl();
	}

	public MutableBeanArgument<BeanArgument> newBeanArgument() {
		return new MutableBeanArgumentImpl();
	}

	public MutableBeanProperty<BeanProperty> newBeanProperty() {
		return new MutableBeanPropertyImpl();
	}

	public MutableReferenceMetadata<ReferenceMetadata> newReference() {
		return new MutableReferenceMetadataImpl();
	}

	public MutableReferenceListMetadata<ReferenceListMetadata> newRefList() {
		return new MutableReferenceListMetadataImpl();
	}

	public MutableServiceMetadata<ServiceMetadata> newService() {
		return new MutableServiceMetadataImpl();
	}

	public MutableRegistrationListener<RegistrationListener> newRegistrationListener() {
		return new MutableRegistrationListenerImpl();
	}

	public MutableReferenceListener<ReferenceListener> newReferenceListener() {
		return new MutableReferenceListenerImpl();
	}

	public MutableRefMetadata<RefMetadata> newRef() {
		return new MutableRefMetadataImpl();
	}

	public MutableIdRefMetadata<IdRefMetadata> newIdRef() {
		return new MutableIdRefMetadataImpl();
	}

	public MutableCollectionMetadata<CollectionMetadata> newCollection() {
		return new MutableCollectionMetadataImpl();
	}

	public MutableMapMetadata<MapMetadata> newMap() {
		return new MutableMapMetadataImpl();
	}

	public MutablePropsMetadata<PropsMetadata> newProps() {
		return new MutablePropsMetadataImpl();
	}

	public MutableValueMetadata<ValueMetadata> newValue() {
		return new MutableValueMetadataImpl();
	}

	public MutableMapEntry<MapEntry> newMapEntry() {
		return new MutableMapEntryImpl();
	}

}
