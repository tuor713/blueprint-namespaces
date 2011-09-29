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
package org.apache.aries.blueprint.ext;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.aries.blueprint.ExtendedReferenceListMetadata;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.metadata.Builder;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.apache.aries.blueprint.metadata.MutableComponentMetadata;
import org.apache.aries.blueprint.metadata.MutableReferenceMetadata;
import org.apache.aries.blueprint.metadata.MutableServiceReferenceMetadata;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.BeanProperty;
import org.osgi.service.blueprint.reflect.CollectionMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ReferenceListMetadata;
import org.osgi.service.blueprint.reflect.ReferenceMetadata;
import org.osgi.service.blueprint.reflect.ServiceReferenceMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A namespace handler for Aries blueprint extensions
 *
 * @version $Rev: 1071628 $, $Date: 2011-02-17 14:44:49 +0000 (Thu, 17 Feb 2011) $
 */
public class ExtNamespaceHandler implements org.apache.aries.blueprint.NamespaceHandler {

    public static final String BLUEPRINT_NAMESPACE = "http://www.osgi.org/xmlns/blueprint/v1.0.0";
    public static final String BLUEPRINT_EXT_NAMESPACE_V1_0 = "http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.0.0";
    public static final String BLUEPRINT_EXT_NAMESPACE_V1_1 = "http://aries.apache.org/blueprint/xmlns/blueprint-ext/v1.1.0";

    public static final String PROPERTY_PLACEHOLDER_ELEMENT = "property-placeholder";
    public static final String DEFAULT_PROPERTIES_ELEMENT = "default-properties";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String VALUE_ELEMENT = "value";
    public static final String LOCATION_ELEMENT = "location";

    public static final String ID_ATTRIBUTE = "id";
    public static final String PLACEHOLDER_PREFIX_ATTRIBUTE = "placeholder-prefix";
    public static final String PLACEHOLDER_SUFFIX_ATTRIBUTE = "placeholder-suffix";
    public static final String DEFAULTS_REF_ATTRIBUTE = "defaults-ref";
    public static final String IGNORE_MISSING_LOCATIONS_ATTRIBUTE = "ignore-missing-locations";

    public static final String SYSTEM_PROPERTIES_ATTRIBUTE = "system-properties";
    public static final String SYSTEM_PROPERTIES_NEVER = "never";
    public static final String SYSTEM_PROPERTIES_FALLBACK = "fallback";
    public static final String SYSTEM_PROPERTIES_OVERRIDE = "override";

    public static final String PROXY_METHOD_ATTRIBUTE = "proxy-method";
    public static final String PROXY_METHOD_DEFAULT = "default";
    public static final String PROXY_METHOD_CLASSES = "classes";
    public static final String PROXY_METHOD_GREEDY = "greedy";

    public static final String ROLE_ATTRIBUTE = "role";
    public static final String ROLE_PROCESSOR = "processor";
    
    public static final String FIELD_INJECTION_ATTRIBUTE = "field-injection";
    
    public static final String DEFAULT_REFERENCE_BEAN = "default";


    public static String FIELD_INJECTION_KEY = "field-injection";
    public static String DEFAULT_BEAN_KEY = "default-bean";
    public static String PROCESSOR_KEY = "processor";
    public static String PROXY_METHOD_KEY = "proxy-method";
    public static String RUNTIME_CLASS_KEY = "runtime-class";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtNamespaceHandler.class);

    private int idCounter;

    public URL getSchemaLocation(String namespace) {
        if (BLUEPRINT_EXT_NAMESPACE_V1_0.equals(namespace)) {
          return getClass().getResource("blueprint-ext.xsd");
        } else if (BLUEPRINT_EXT_NAMESPACE_V1_1.equals(namespace)) {
          return getClass().getResource("blueprint-ext-1.1.xsd");
        } else {
          return null;
        }
    }

    public Set<Class> getManagedClasses() {
        return new HashSet<Class>(Arrays.asList(
                PropertyPlaceholder.class
        ));
    }

    public Metadata parse(Element element, ParserContext context) {
        LOGGER.debug("Parsing element {{}}{}", element.getNamespaceURI(), element.getLocalName());
        if (nodeNameEquals(element, PROPERTY_PLACEHOLDER_ELEMENT)) {
            return parsePropertyPlaceholder(context, element);
        } else if (nodeNameEquals(element, "identity")) {
        	NodeList list = element.getChildNodes();
        	for (int i=0; i<list.getLength(); i++) {
        		Node n = list.item(i);
        		if (n instanceof Element) {        		
        			return context.parseElement(BeanMetadata.class, context.getEnclosingComponent(), (Element) n);
        		}
        	}
        	
        	return null;
        } else {
            throw new ComponentDefinitionException("Unsupported element: " + element.getNodeName());
        }
    }

    public ComponentMetadata decorate(Node node, ComponentMetadata component, ParserContext context) {
        if (node instanceof Attr && nodeNameEquals(node, PROXY_METHOD_ATTRIBUTE)) {
            return decorateProxyMethod(node, component, context);
        } else if (node instanceof Attr && nodeNameEquals(node, ROLE_ATTRIBUTE)) {
            return decorateRole(node, component, context);
        } else if (node instanceof Attr && nodeNameEquals(node, FIELD_INJECTION_ATTRIBUTE)) {
            return decorateFieldInjection(node, component, context);
        } else if (node instanceof Attr && nodeNameEquals(node, DEFAULT_REFERENCE_BEAN)) {
            return decorateDefaultBean(node, component, context);
        } else {
            throw new ComponentDefinitionException("Unsupported node: " + node.getNodeName());
        }
    }
    
    private ComponentMetadata decorateDefaultBean(Node node,
        ComponentMetadata component, ParserContext context) 
    {
        if (!(component instanceof ReferenceMetadata)) {
            throw new ComponentDefinitionException("Attribute " + node.getNodeName() + " can only be used on a <reference> element");
        }
      
        if (!(component instanceof MutableReferenceMetadata)) {
            throw new ComponentDefinitionException("Expected an instanceof MutableReferenceMetadata");
        }
        
        String value = ((Attr) node).getValue();
        ((MutableReferenceMetadata<?>) component).addCustomData(ExtNamespaceHandler.class, DEFAULT_BEAN_KEY, value);
        return component;
    }

    private ComponentMetadata decorateFieldInjection(Node node, ComponentMetadata component, ParserContext context) {
        if (!(component instanceof BeanMetadata)) {
            throw new ComponentDefinitionException("Attribute " + node.getNodeName() + " can only be used on a <bean> element");
        }
        
        if (!(component instanceof MutableBeanMetadata)) {
            throw new ComponentDefinitionException("Expected an instanceof MutableBeanMetadata");
        }
        
        String value = ((Attr) node).getValue();
        ((MutableBeanMetadata<?>) component).addCustomData(ExtNamespaceHandler.class, FIELD_INJECTION_KEY, "true".equals(value) || "1".equals(value));
        return component;
    }

    private ComponentMetadata decorateRole(Node node, ComponentMetadata component, ParserContext context) {
        if (!(component instanceof BeanMetadata)) {
            throw new ComponentDefinitionException("Attribute " + node.getNodeName() + " can only be used on a <bean> element");
        }
        if (!(component instanceof MutableBeanMetadata)) {
            throw new ComponentDefinitionException("Expected an instance of MutableBeanMetadata");
        }
        boolean processor = false;
        String value = ((Attr) node).getValue();
        String[] flags = value.trim().split(" ");
        for (String flag : flags) {
            if (ROLE_PROCESSOR.equals(flag)) {
                processor = true;
            } else {
                throw new ComponentDefinitionException("Unknown proxy method: " + flag);
            }
        }
        ((MutableBeanMetadata<?>) component).addCustomData(ExtNamespaceHandler.class, PROCESSOR_KEY, processor);
        return component;
    }

    private ComponentMetadata decorateProxyMethod(Node node, ComponentMetadata component, ParserContext context) {
        if (!(component instanceof ServiceReferenceMetadata)) {
            throw new ComponentDefinitionException("Attribute " + node.getNodeName() + " can only be used on a <reference> or <reference-list> element");
        }
        if (!(component instanceof MutableServiceReferenceMetadata)) {
            throw new ComponentDefinitionException("Expected an instance of MutableServiceReferenceMetadata");
        }
        int method = 0;
        String value = ((Attr) node).getValue();
        String[] flags = value.trim().split(" ");
        for (String flag : flags) {
            if (PROXY_METHOD_DEFAULT.equals(flag)) {
                method += ExtendedReferenceListMetadata.PROXY_METHOD_DEFAULT;
            } else if (PROXY_METHOD_CLASSES.equals(flag)) {
                method += ExtendedReferenceListMetadata.PROXY_METHOD_CLASSES;
            } else if (PROXY_METHOD_GREEDY.equals(flag)) {
                method += ExtendedReferenceListMetadata.PROXY_METHOD_GREEDY;
            } else {
                throw new ComponentDefinitionException("Unknown proxy method: " + flag);
            }
        }
        if ((method & ExtendedReferenceListMetadata.PROXY_METHOD_GREEDY) != 0 && !(component instanceof ReferenceListMetadata)) {
            throw new ComponentDefinitionException("Greedy proxying is only available for <reference-list> element");
        }
        ((MutableServiceReferenceMetadata<?,?>) component).addCustomData(ExtNamespaceHandler.class, PROXY_METHOD_KEY, method);
        return component;
    }

    private Metadata parsePropertyPlaceholder(ParserContext context, Element element) {
    	Builder builder = context.getMetadataBuilder();
    	
        MutableBeanMetadata<?> metadata = builder.newBean();
        
        metadata.addCustomData(ExtNamespaceHandler.class, PROCESSOR_KEY, true);
        metadata.addCustomData(ExtNamespaceHandler.class, RUNTIME_CLASS_KEY, PropertyPlaceholder.class);
        
        metadata.id(getId(context, element))
        	.scope(BeanMetadata.SCOPE_SINGLETON)
        	.initMethod("init");
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
        String ignoreMissingLocations = element.hasAttribute(IGNORE_MISSING_LOCATIONS_ATTRIBUTE) ? element.getAttribute(IGNORE_MISSING_LOCATIONS_ATTRIBUTE) : null;
        if (ignoreMissingLocations != null) {
            metadata.addProperty(builder.newBeanProperty().name("ignoreMissingLocations").value(createValue(context, ignoreMissingLocations)));
        }
        String systemProperties = element.hasAttribute(SYSTEM_PROPERTIES_ATTRIBUTE) ? element.getAttribute(SYSTEM_PROPERTIES_ATTRIBUTE) : null;
        if (systemProperties != null) {
            metadata.addProperty(builder.newBeanProperty().name("systemProperties").value(createValue(context, systemProperties)));
        }
        // Parse elements
        List<String> locations = new ArrayList<String>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (BLUEPRINT_EXT_NAMESPACE_V1_0.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, DEFAULT_PROPERTIES_ELEMENT)) {
                        if (defaultsRef != null) {
                            throw new ComponentDefinitionException("Only one of " + DEFAULTS_REF_ATTRIBUTE + " attribute or " + DEFAULT_PROPERTIES_ELEMENT + " element is allowed");
                        }
                        Metadata props = parseDefaultProperties(context, metadata, e);
                        metadata.addProperty(builder.newBeanProperty().name("defaultProperties").value(props));
                    } else if (nodeNameEquals(e, LOCATION_ELEMENT)) {
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

    private Metadata parseDefaultProperties(ParserContext context, MutableBeanMetadata<?> enclosingComponent, Element element) {
        
        NodeList nl = element.getChildNodes();
        
        List<MapEntry> entries = new ArrayList<MapEntry>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element e = (Element) node;
                if (BLUEPRINT_EXT_NAMESPACE_V1_0.equals(e.getNamespaceURI())) {
                    if (nodeNameEquals(e, PROPERTY_ELEMENT)) {
                        BeanProperty prop = context.parseElement(BeanProperty.class, enclosingComponent, e);
                        entries.add(
                        		context.getMetadataBuilder().newMapEntry()
                        			.key(createValue(context, prop.getName(), String.class.getName()))
                        			.value(prop.getValue()));
                    }
                }
            }
        }
        return context.getMetadataBuilder().newMap().entries(entries);
    }

    public String getId(ParserContext context, Element element) {
        if (element.hasAttribute(ID_ATTRIBUTE)) {
            return element.getAttribute(ID_ATTRIBUTE);
        } else {
            return generateId(context);
        }
    }

    public void generateIdIfNeeded(ParserContext context, MutableComponentMetadata<?, ?> metadata) {
        if (metadata.getId() == null) {
            metadata.id(generateId(context));
        }
    }

    private String generateId(ParserContext context) {
        String id;
        do {
            id = ".ext-" + ++idCounter;
        } while (context.getComponentDefinitionRegistry().containsComponentDefinition(id));
        return id;
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

    private static CollectionMetadata createList(ParserContext context, List<String> list) {
    	List<ValueMetadata> values = new ArrayList<ValueMetadata>();
        for (String v : list) {
            values.add(createValue(context, v, String.class.getName()));
        }
    	return context.getMetadataBuilder().newCollection()
    		.collectionClass(List.class)
    		.valueType(String.class.getName())
    		.values(values);
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

}
