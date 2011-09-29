/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.aries.blueprint.testbundlea;

import java.net.URL;
import java.util.Set;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.ext.ExtNamespaceHandler;
import org.apache.aries.blueprint.metadata.MutableBeanMetadata;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NSHandlerThree implements NamespaceHandler{
    public static String NSURI = "http://ns.handler.three";
    
    private static String ATTRIB_ONE = "attribone";
    
    public ComponentMetadata decorate(Node node, ComponentMetadata component,
            ParserContext context) {
        if(node.getLocalName().equals(ATTRIB_ONE)){
            if(component instanceof BeanMetadata){
                if(context.getComponentDefinitionRegistry().getComponentDefinition(NSURI+"/BeanProcessor")==null) {
                	MutableBeanMetadata<BeanMetadata> mbm = context.getMetadataBuilder().newBean()
                		.scope(BeanMetadata.SCOPE_SINGLETON)
                		.id(NSURI+"/BeanProcessor");
                	
                	mbm.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.PROCESSOR_KEY, true);
                	mbm.addCustomData(ExtNamespaceHandler.class, ExtNamespaceHandler.RUNTIME_CLASS_KEY, BeanProcessorTest.class);
                    
                    context.getComponentDefinitionRegistry().registerComponentDefinition(mbm);
                }
            }
        }
        return component;
    }

    //process elements
    public Metadata parse(Element element, ParserContext context) {
        return null;
    }    

    //supply schema back to blueprint.
    public URL getSchemaLocation(String namespace) {
        return this.getClass().getResource("nshandlerthree.xsd");
    }

    public Set<Class> getManagedClasses() {
        return null;
    }

}
