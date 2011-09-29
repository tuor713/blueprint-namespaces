/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.compendium.cm;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.container.Parser;
import org.apache.aries.blueprint.container.ParserContextImpl;
import org.apache.aries.blueprint.container.ServiceListener;
import org.apache.aries.blueprint.ext.ExtNamespaceHandler;
import org.apache.aries.blueprint.ext.PlaceholdersUtils;
import org.apache.aries.blueprint.metadata.Builder;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableComponentMetadata;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.IdRefMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.MapMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.RegistrationListener;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Namespace handler for the Config Admin service.
 * This handler will parse the various elements defined and populate / modify the registry
 * accordingly.
 *
 * @see CmManagedProperties
 * @see CmManagedServiceFactory
 * @see CmProperties
 * @see CmPropertyPlaceholder
 *
 * @version $Rev: 1071628 $, $Date: 2011-02-17 14:44:49 +0000 (Thu, 17 Feb 2011) $
 */
public class CmNamespaceHandler implements NamespaceHandler {

    public static final String BLUEPRINT_NAMESPACE = "http://www.osgi.org/xmlns/blueprint/v1.0.0";
    public static final String BLUEPRINT_CM_NAMESPACE_1_0 = "http://aries.apache.org/blueprint/xmlns/blueprint-cm/v1.0.0";
    public static final String BLUEPRINT_CM_NAMESPACE_1_1 = "http://aries.apache.org/blueprint/xmlns/blueprint-cm/v1.1.0";

    public static final String PROPERTY_PLACEHOLDER_ELEMENT = "property-placeholder";
    public static final String MANAGED_PROPERTIES_ELEMENT = "managed-properties";
    public static final String MANAGED_SERVICE_FACTORY_ELEMENT = "managed-service-factory";
    public static final String CM_PROPERTIES_ELEMENT = "cm-properties";
    public static final String DEFAULT_PROPERTIES_ELEMENT = "default-properties";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String INTERFACES_ELEMENT = "interfaces";
    public static final String VALUE_ELEMENT = "value";
    public static final String MANAGED_COMPONENT_ELEMENT = "managed-component";

    public static final String ID_ATTRIBUTE = "id";
    public static final String PERSISTENT_ID_ATTRIBUTE = "persistent-id";
    public static final String PLACEHOLDER_PREFIX_ATTRIBUTE = "placeholder-prefix";
    public static final String PLACEHOLDER_SUFFIX_ATTRIBUTE = "placeholder-suffix";
    public static final String DEFAULTS_REF_ATTRIBUTE = "defaults-ref";
    public static final String UPDATE_STRATEGY_ATTRIBUTE = "update-strategy";
    public static final String UPDATE_METHOD_ATTRIBUTE = "update-method";
    public static final String FACTORY_PID_ATTRIBUTE = "factory-pid";
    public static final String AUTO_EXPORT_ATTRIBUTE = "auto-export";
    public static final String RANKING_ATTRIBUTE = "ranking";
    public static final String INTERFACE_ATTRIBUTE = "interface";
    public static final String UPDATE_ATTRIBUTE = "update";

    public static final String AUTO_EXPORT_DISABLED = "disabled";
    public static final String AUTO_EXPORT_INTERFACES = "interfaces";
    public static final String AUTO_EXPORT_CLASS_HIERARCHY = "class-hierarchy";
    public static final String AUTO_EXPORT_ALL = "all-classes";
    public static final String AUTO_EXPORT_DEFAULT = AUTO_EXPORT_DISABLED;
    public static final String RANKING_DEFAULT = "0";

    private static final String MANAGED_OBJECT_MANAGER_NAME = "org.apache.aries.managedObjectManager";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CmNamespaceHandler.class);

    // This property is static but it should be ok since there will be only a single instance
    // of this class for the bundle
    private static ConfigurationAdmin configAdmin;

    private int idCounter;

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public static ConfigurationAdmin getConfigAdmin() {
        return configAdmin;
    }

    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    public URL getSchemaLocation(String namespace) {
        if (BLUEPRINT_CM_NAMESPACE_1_1.equals(namespace)) {
            return getClass().getResource("blueprint-cm-1.1.0.xsd");
        } else if (BLUEPRINT_CM_NAMESPACE_1_0.equals(namespace)) {
            return getClass().getResource("blueprint-cm-1.0.0.xsd");
        } else {
            return null;
        }
    }

    public Set<Class> getManagedClasses() {
        return new HashSet<Class>(Arrays.asList(
                CmPropertyPlaceholder.class,
                CmManagedServiceFactory.class,
                CmManagedProperties.class,
                CmProperties.class
        ));
    }

    public Metadata parse(Element element, ParserContext context) {
        LOGGER.debug("Parsing element {{}}{}", element.getNamespaceURI(), element.getLocalName());
        ComponentDefinitionRegistry registry = context.getComponentDefinitionRegistry();
        registerManagedObjectManager(context, registry);
        if (nodeNameEquals(element, PROPERTY_PLACEHOLDER_ELEMENT)) {
            return parsePropertyPlaceholder(context, element);
        } else if (nodeNameEquals(element, MANAGED_SERVICE_FACTORY_ELEMENT)) {
            return parseManagedServiceFactory(context, element);
        } else {
            throw new ComponentDefinitionException("Unsupported element: " + element.getNodeName());
        }
    }

    public ComponentMetadata decorate(Node node, ComponentMetadata component, ParserContext context) {
        LOGGER.debug("Decorating node {{}}{}", node.getNamespaceURI(), node.getLocalName());
        ComponentDefinitionRegistry registry = context.getComponentDefinitionRegistry();
        registerManagedObjectManager(context, registry);
        if (node instanceof Element) {
            if (nodeNameEquals(node, MANAGED_PROPERTIES_ELEMENT)) {
                return decorateManagedProperties(context, (Element) node, component);
            } else if (nodeNameEquals(node, CM_PROPERTIES_ELEMENT)) {
                return decorateCmProperties(context, (Element) node, component);
            } else {
                throw new ComponentDefinitionException("Unsupported element: " + node.getNodeName());
            }
        } else {
            throw new ComponentDefinitionException("Illegal use of blueprint cm namespace");
        }
    }

    private ComponentMetadata parsePropertyPlaceholder(ParserContext context, Element element) {
    	Builder builder = context.getMetadataBuilder();
    	MutableBeanMetadata<BeanMetadata> metadata = builder.newBean()
    		.id(getId(context, element))
    		.scope(BeanMetadata.SCOPE_SINGLETON)
    		.initMethod("init")
    		.destroyMethod("destroy")
    		.addProperty(builder.newBeanProperty().name("blueprintContainer").value(createRef(context, "blueprintContainer")))
    		.addProperty(builder.newBeanProperty().name("configAdmin").value(createConfigAdminProxy(context)))
    		.addProperty(builder.newBeanProperty().name("persistentId").value(createValue(context, element.getAttribute(PERSISTENT_ID_ATTRIBUTE))));
    	
    	metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.PROCESSOR_KEY, true);
        metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, CmPropertyPlaceholder.class);        
        
        String prefix = element.hasAttribute(PLACEHOLDER_PREFIX_ATTRIBUTE)
                                    ? element.getAttribute(PLACEHOLDER_PREFIX_ATTRIBUTE)
                                    : "${";
        metadata.addProperty(builder.newBeanProperty().name("placeholderPrefix").value(createValue(context, prefix)));
        
        String suffix = element.hasAttribute(PLACEHOLDER_SUFFIX_ATTRIBUTE)
                                    ? element.getAttribute(PLACEHOLDER_SUFFIX_ATTRIBUTE)
                                    : "}";
        metadata.addProperty(builder.newBeanProperty().name("placeholderSuffix").value(createValue(context, suffix)));
        
        String defaultsRef = element.hasAttribute(DEFAULTS_REF_ATTRIBUTE) ? element.getAttribute(DEFAULTS_REF_ATTRIBUTE) : null;
        if (defaultsRef != null) {
            metadata.addProperty(builder.newBeanProperty().name("defaultProperties").value(createRef(context, defaultsRef)));
        }
        String ignoreMissingLocations = extractIgnoreMissingLocations(element);
        if (ignoreMissingLocations != null) {
            metadata.addProperty(builder.newBeanProperty().name("ignoreMissingLocations").value(createValue(context, ignoreMissingLocations)));
        }
        
        String systemProperties = extractSystemPropertiesAttribute(element);
        if (systemProperties == null) {
            systemProperties = ExtNamespaceHandler.SYSTEM_PROPERTIES_NEVER;
        }
        metadata.addProperty(builder.newBeanProperty().name("systemProperties").value(createValue(context, systemProperties)));
        
        String updateStrategy = element.getAttribute(UPDATE_STRATEGY_ATTRIBUTE);
        if (updateStrategy != null) {
            metadata.addProperty(builder.newBeanProperty().name("updateStrategy").value(createValue(context, updateStrategy)));
        }
        metadata.addProperty(builder.newBeanProperty().name("managedObjectManager").value(createRef(context, MANAGED_OBJECT_MANAGER_NAME)));
        
        // Parse elements
        List<String> locations = new ArrayList<String>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (BLUEPRINT_CM_NAMESPACE_1_0.equals(e.getNamespaceURI())
                        || BLUEPRINT_CM_NAMESPACE_1_1.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, DEFAULT_PROPERTIES_ELEMENT)) {
                        if (defaultsRef != null) {
                            throw new ComponentDefinitionException("Only one of " + DEFAULTS_REF_ATTRIBUTE + " attribute or " + DEFAULT_PROPERTIES_ELEMENT + " element is allowed");
                        }
                        Metadata props = parseDefaultProperties(context, metadata, e);
                        metadata.addProperty(builder.newBeanProperty().name("defaultProperties").value(props));
                    }
                } else if (ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_0.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, ExtNamespaceHandler.LOCATION_ELEMENT)) {
                        locations.add(getTextValue(e));
                    }
                } else if (ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_1.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, ExtNamespaceHandler.LOCATION_ELEMENT)) {
                        locations.add(getTextValue(e));
                    }
                }
            }
        }
        if (!locations.isEmpty()) {
            metadata.addProperty(builder.newBeanProperty().name("locations").value(createList(context, locations)));
        }

        PlaceholdersUtils.validatePlaceholder(metadata, context.getComponentDefinitionRegistry());
        
        return metadata;
    }

    private String extractSystemPropertiesAttribute(Element element) {
      String systemProperties = null;
      
      if (element.hasAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_0, ExtNamespaceHandler.SYSTEM_PROPERTIES_ATTRIBUTE)) {
        systemProperties =  element.getAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_0, ExtNamespaceHandler.SYSTEM_PROPERTIES_ATTRIBUTE);
      } else if (element.hasAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_1, ExtNamespaceHandler.SYSTEM_PROPERTIES_ATTRIBUTE)) {
        systemProperties =  element.getAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_1, ExtNamespaceHandler.SYSTEM_PROPERTIES_ATTRIBUTE);
      }
      return systemProperties;
    }

    private String extractIgnoreMissingLocations(Element element) {
      String ignoreMissingLocations = null;
      if (element.hasAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_0, ExtNamespaceHandler.IGNORE_MISSING_LOCATIONS_ATTRIBUTE)) {
        ignoreMissingLocations = element.getAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_0, ExtNamespaceHandler.IGNORE_MISSING_LOCATIONS_ATTRIBUTE);
      } else if (element.hasAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_1, ExtNamespaceHandler.IGNORE_MISSING_LOCATIONS_ATTRIBUTE)) {
        ignoreMissingLocations = element.getAttributeNS(ExtNamespaceHandler.BLUEPRINT_EXT_NAMESPACE_V1_1, ExtNamespaceHandler.IGNORE_MISSING_LOCATIONS_ATTRIBUTE);
      }
      return ignoreMissingLocations;
    }

    private Metadata parseDefaultProperties(ParserContext context, MutableBeanMetadata<?> enclosingComponent, Element element) {
    	Builder builder = context.getMetadataBuilder();
        
        List<MapEntry> entries = new ArrayList<MapEntry>();
        
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (BLUEPRINT_CM_NAMESPACE_1_0.equals(e.getNamespaceURI())
                        || BLUEPRINT_CM_NAMESPACE_1_1.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, PROPERTY_ELEMENT)) {
                        BeanProperty prop = context.parseElement(BeanProperty.class, enclosingComponent, e);
                        entries.add(builder.newMapEntry().key(createValue(context, prop.getName(), String.class.getName())).value(prop.getValue()));
                    }
                }
            }
        }
        
        return builder.newMap().entries(entries);
    }

    private ComponentMetadata parseManagedServiceFactory(ParserContext context, Element element) {
        String id = getId(context, element);

        Builder builder = context.getMetadataBuilder();
        MutableBeanMetadata<BeanMetadata> factoryMetadata = builder.newBean();
        
        generateIdIfNeeded(context, factoryMetadata);
        
        factoryMetadata
        	.scope(BeanMetadata.SCOPE_SINGLETON)
        	.initMethod("init")
        	.destroyMethod("destroy")
        	.addProperty("id", createValue(context, factoryMetadata.getId()))
        	.addProperty("configAdmin", createConfigAdminProxy(context))
        	.addProperty("blueprintContainer", createRef(context, "blueprintContainer"))
        	.addProperty("factoryPid", createValue(context, element.getAttribute(FACTORY_PID_ATTRIBUTE)));
        
        factoryMetadata.addCustomData(ExtNamespaceHandler.class, 
        		ExtNamespaceHandler.RUNTIME_CLASS_KEY, 
        		CmManagedServiceFactory.class);
        
        
        String autoExport = element.hasAttribute(AUTO_EXPORT_ATTRIBUTE) ? element.getAttribute(AUTO_EXPORT_ATTRIBUTE) : AUTO_EXPORT_DEFAULT;
        if (AUTO_EXPORT_DISABLED.equals(autoExport)) {
            autoExport = Integer.toString(ServiceMetadata.AUTO_EXPORT_DISABLED);
        } else if (AUTO_EXPORT_INTERFACES.equals(autoExport)) {
            autoExport = Integer.toString(ServiceMetadata.AUTO_EXPORT_INTERFACES);
        } else if (AUTO_EXPORT_CLASS_HIERARCHY.equals(autoExport)) {
            autoExport = Integer.toString(ServiceMetadata.AUTO_EXPORT_CLASS_HIERARCHY);
        } else if (AUTO_EXPORT_ALL.equals(autoExport)) {
            autoExport = Integer.toString(ServiceMetadata.AUTO_EXPORT_ALL_CLASSES);
        } else {
            throw new ComponentDefinitionException("Illegal value (" + autoExport + ") for " + AUTO_EXPORT_ATTRIBUTE + " attribute");
        }
        factoryMetadata.addProperty("autoExport", createValue(context, autoExport));
        String ranking = element.hasAttribute(RANKING_ATTRIBUTE) ? element.getAttribute(RANKING_ATTRIBUTE) : RANKING_DEFAULT;
        factoryMetadata.addProperty("ranking", createValue(context, ranking));

        List<String> interfaces = null;
        if (element.hasAttribute(INTERFACE_ATTRIBUTE)) {
            interfaces = Collections.singletonList(element.getAttribute(INTERFACE_ATTRIBUTE));
            factoryMetadata.addProperty("interfaces", createList(context, interfaces));
        }

        Parser parser = getParser(context);
        
        // Parse elements
        List<RegistrationListener> listeners = new ArrayList<RegistrationListener>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (isBlueprintNamespace(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, INTERFACES_ELEMENT)) {
                        if (interfaces != null) {
                            throw new ComponentDefinitionException("Only one of " + Parser.INTERFACE_ATTRIBUTE + " attribute or " + INTERFACES_ELEMENT + " element must be used");
                        }
                        interfaces = parseInterfaceNames(e);
                        factoryMetadata.addProperty("interfaces", createList(context, interfaces));                    
                    } else if (nodeNameEquals(e, Parser.SERVICE_PROPERTIES_ELEMENT)) { 
                        MapMetadata map = parser.parseServiceProperties(e, factoryMetadata);
                        factoryMetadata.addProperty("serviceProperties", map);
                    } else if (nodeNameEquals(e, Parser.REGISTRATION_LISTENER_ELEMENT)) {
                        listeners.add(parser.parseRegistrationListener(e, factoryMetadata));
                    }
                } else if (BLUEPRINT_CM_NAMESPACE_1_0.equals(e.getNamespaceURI())
                        || BLUEPRINT_CM_NAMESPACE_1_1.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, MANAGED_COMPONENT_ELEMENT)) {
                        MutableBeanMetadata<BeanMetadata> managedComponent = context.parseElement(MutableBeanMetadata.class, null, e);
                        generateIdIfNeeded(context, managedComponent);
                        managedComponent.scope(BeanMetadata.SCOPE_PROTOTYPE);
                        
                        // destroy-method on managed-component has different signature than on regular beans
                        // so we'll handle it differently
                        String destroyMethod = managedComponent.getDestroyMethod();
                        if (destroyMethod != null) {
                            factoryMetadata.addProperty("componentDestroyMethod", createValue(context, destroyMethod));
                            managedComponent.destroyMethod(null);
                        }
                        
                        context.getComponentDefinitionRegistry().registerComponentDefinition(managedComponent);
                        factoryMetadata.addProperty("managedComponentName", createIdRef(context, managedComponent.getId()));
                    }
                }
            }
        }

        List<BeanMetadata> values = new ArrayList<BeanMetadata>();
        for (RegistrationListener listener : listeners) {
            MutableBeanMetadata<BeanMetadata> bean = builder.newBean()
            	.addProperty("listener", listener.getListenerComponent())
            	.addProperty("registerMethod", createValue(context, listener.getRegistrationMethod()))
            	.addProperty("unregisterMethod", createValue(context, listener.getUnregistrationMethod()));
            
            bean.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, ServiceListener.class);
            values.add(bean);
        }
        
        factoryMetadata.addProperty("listeners", builder.newCollection().collectionClass(List.class).values(values));
        
        context.getComponentDefinitionRegistry().registerComponentDefinition(factoryMetadata);
        
        return builder.newBean().scope(BeanMetadata.SCOPE_SINGLETON).id(id).factoryComponent(createRef(context, factoryMetadata.getId())).factoryMethod("getServiceMap");
    }

    private ComponentMetadata decorateCmProperties(ParserContext context, Element element, ComponentMetadata component) {
        generateIdIfNeeded(context, ((MutableComponentMetadata<?,?>) component));
        
        MutableBeanMetadata<BeanMetadata> metadata = context.getMetadataBuilder().newBean()
        	.id(getId(context, element));
        
        metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.PROCESSOR_KEY, true);
        metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, CmProperties.class);
        
        String persistentId = element.getAttribute(PERSISTENT_ID_ATTRIBUTE);
        // if persistentId is "" the cm-properties element in nested in managed-service-factory
        // and the configuration object will come from the factory. So we only really need to register
        // ManagedService if the persistentId is not an empty string.
        if (persistentId.length() > 0) {
            metadata.initMethod("init").destroyMethod("destroy");
        }
        
        metadata.addProperty("blueprintContainer", createRef(context, "blueprintContainer"))
        	.addProperty("configAdmin", createConfigAdminProxy(context))
        	.addProperty("managedObjectManager", createRef(context, MANAGED_OBJECT_MANAGER_NAME))
        	.addProperty("persistentId", createValue(context, persistentId))
        	.addProperty("serviceId", createIdRef(context, component.getId()));
        
        if (element.hasAttribute(UPDATE_ATTRIBUTE)) {
            metadata.addProperty("update", createValue(context, element.getAttribute(UPDATE_ATTRIBUTE)));
        }
        
        context.getComponentDefinitionRegistry().registerComponentDefinition(metadata);
        return component;
    }

    private ComponentMetadata decorateManagedProperties(ParserContext context, Element element, ComponentMetadata component) {
        if (!(component instanceof MutableBeanMetadata)) {
            throw new ComponentDefinitionException("Element " + MANAGED_PROPERTIES_ELEMENT + " must be used inside a <bp:bean> element");
        }
        generateIdIfNeeded(context, ((MutableBeanMetadata<?>) component));
        
        MutableBeanMetadata<BeanMetadata> metadata = context.getMetadataBuilder().newBean()
        	.id(getId(context, element));
        
        metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.PROCESSOR_KEY, true);
        metadata.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, CmManagedProperties.class);
        
        
        String persistentId = element.getAttribute(PERSISTENT_ID_ATTRIBUTE);
        // if persistentId is "" the managed properties element in nested in managed-service-factory
        // and the configuration object will come from the factory. So we only really need to register
        // ManagedService if the persistentId is not an empty string.
        if (persistentId.length() > 0) {
            metadata.initMethod("init").destroyMethod("destroy");
        }
        
        metadata.addProperty("blueprintContainer", createRef(context, "blueprintContainer"))
        	.addProperty("configAdmin", createConfigAdminProxy(context))
        	.addProperty("managedObjectManager", createRef(context, MANAGED_OBJECT_MANAGER_NAME))
        	.addProperty("persistentId", createValue(context, persistentId))
        	.addProperty("beanName", createIdRef(context, component.getId()));
        	
        String updateStrategy = element.getAttribute(UPDATE_STRATEGY_ATTRIBUTE);
        if (updateStrategy != null) {
            metadata.addProperty("updateStrategy", createValue(context, updateStrategy));
        }
        
        if (element.hasAttribute(UPDATE_METHOD_ATTRIBUTE)) {
            metadata.addProperty("updateMethod", createValue(context, element.getAttribute(UPDATE_METHOD_ATTRIBUTE)));
        } else if ("component-managed".equals(updateStrategy)) {
            throw new ComponentDefinitionException(UPDATE_METHOD_ATTRIBUTE + " attribute must be set when " + UPDATE_STRATEGY_ATTRIBUTE + " is set to 'component-managed'");
        }
        
        context.getComponentDefinitionRegistry().registerComponentDefinition(metadata);
        return component;
    }

    /**
     * Create a reference to the ConfigurationAdmin service if not already done
     * and add it to the registry.
     *
     * @param context the parser context
     * @return a metadata pointing to the config admin
     */
    private Metadata createConfigAdminProxy(ParserContext context) {
    	MutableBeanMetadata<BeanMetadata> meta = context.getMetadataBuilder().newBean()
    		.factoryMethod("getConfigAdmin")
    		.activation(BeanMetadata.ACTIVATION_LAZY)
    		.scope(BeanMetadata.SCOPE_PROTOTYPE);
    	
    	meta.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, CmNamespaceHandler.class);
    	return meta;
    }

    private void registerManagedObjectManager(ParserContext context, ComponentDefinitionRegistry registry) {
        if (registry.getComponentDefinition(MANAGED_OBJECT_MANAGER_NAME) == null) {
        	MutableBeanMetadata<BeanMetadata> meta = context.getMetadataBuilder()
        		.newBean()
        		.scope(BeanMetadata.SCOPE_SINGLETON)
        		.id(MANAGED_OBJECT_MANAGER_NAME);
        	meta.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, ManagedObjectManager.class);
            registry.registerComponentDefinition(meta);
        }
    }
    
    private static ValueMetadata createValue(ParserContext context, String value) {
        return createValue(context, value, null);
    }

    private static ValueMetadata createValue(ParserContext context, String value, String type) {
    	return context.getMetadataBuilder().newValue().stringValue(value).type(type);
    }

    private static RefMetadata createRef(ParserContext context, String value) {
    	return context.getMetadataBuilder().newRef().componentId(value);
    }

    private static IdRefMetadata createIdRef(ParserContext context, String value) {
    	return context.getMetadataBuilder().newIdRef().componentId(value);
    }

    private static CollectionMetadata createList(ParserContext context, List<String> list) {
    	Builder builder = context.getMetadataBuilder();
    	
    	List<ValueMetadata> values = new ArrayList<ValueMetadata>();
        for (String v : list) {
            values.add(createValue(context, v, String.class.getName()));
        }
        
        return builder.newCollection().collectionClass(List.class).valueType(String.class.getName()).values(values);
    }

    private static String getTextValue(Element element) {
        StringBuffer value = new StringBuffer();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
                value.append(item.getNodeValue());
            }
        }
        return value.toString();
    }

    private static boolean nodeNameEquals(Node node, String name) {
        return (name.equals(node.getNodeName()) || name.equals(node.getLocalName()));
    }

    public static boolean isBlueprintNamespace(String ns) {
        return BLUEPRINT_NAMESPACE.equals(ns);
    }

    public String getId(ParserContext context, Element element) {
        if (element.hasAttribute(ID_ATTRIBUTE)) {
            return element.getAttribute(ID_ATTRIBUTE);
        } else {
            return generateId(context);
        }
    }

    public void generateIdIfNeeded(ParserContext context, MutableComponentMetadata<?,?> metadata) {
        if (metadata.getId() == null) {
            metadata.id(generateId(context));
        }
    }

    private String generateId(ParserContext context) {
        String id;
        do {
            id = ".cm-" + ++idCounter;
        } while (context.getComponentDefinitionRegistry().containsComponentDefinition(id));
        return id;
    }
    
    private Parser getParser(ParserContext ctx) {
        if (ctx instanceof ParserContextImpl) {
            return ((ParserContextImpl) ctx).getParser();
        }
        throw new RuntimeException("Unable to get parser");
    }

    public List<String> parseInterfaceNames(Element element) {
        List<String> interfaceNames = new ArrayList<String>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (nodeNameEquals(e, VALUE_ELEMENT)) {
                    String v = getTextValue(e).trim();
                    if (interfaceNames.contains(v)) {
                        throw new ComponentDefinitionException("The element " + INTERFACES_ELEMENT + " should not contain the same interface twice");
                    }
                    interfaceNames.add(getTextValue(e));
                } else {
                    throw new ComponentDefinitionException("Unsupported element " + e.getNodeName() + " inside an " + INTERFACES_ELEMENT + " element");
                }
            }
        }
        return interfaceNames;
    }

}
