package org.apache.aries.blueprint.container;

import org.apache.aries.blueprint.metadata.Builder;
import org.apache.aries.blueprint.metadata.ExtensibleBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableBeanArgument;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableBeanProperty;
import org.apache.aries.blueprint.metadata.MutableCollectionMetadata;
import org.apache.aries.blueprint.metadata.MutableIdRefMetadata;
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
import org.apache.aries.blueprint.metadata.impl.MutableCollectionMetadataImpl;
import org.apache.aries.blueprint.metadata.impl.MutableValueMetadataImpl;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.IdRefMetadata;
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

	public MutableBeanMetadata<ExtensibleBeanMetadata> newBean() {
		return new MutableBeanMetadataImpl();
	}

	public MutableBeanArgument<BeanArgument> newBeanArgument() {
		return new MutableBeanArgumentImpl();
	}

	public MutableBeanProperty<BeanProperty> newBeanProperty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableReferenceMetadata<ReferenceMetadata> newReference() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableReferenceListMetadata<ReferenceListMetadata> newRefList() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableServiceMetadata<ServiceMetadata> newService() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableRegistrationListener<RegistrationListener> newRegistrationListener() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableReferenceListener<ReferenceListener> newReferenceListener() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableRefMetadata<RefMetadata> newRef() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableIdRefMetadata<IdRefMetadata> newIdRef() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableCollectionMetadata<CollectionMetadata> newCollection() {
		return new MutableCollectionMetadataImpl();
	}

	public MutableMapMetadata<MapMetadata> newMap() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutablePropsMetadata<PropsMetadata> newProps() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public MutableValueMetadata<ValueMetadata> newValue() {
		return new MutableValueMetadataImpl();
	}

}
