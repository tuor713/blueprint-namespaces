/*
 * Copyright (C) 2011 Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.aries.blueprint.ns.itests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import nstest.LinkedListHandlerActivator;
import nstest.ListHandlerActivator;

import org.apache.aries.unittest.fixture.ArchiveFixture;
import org.apache.aries.unittest.fixture.ArchiveFixture.Fixture;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.util.tracker.ServiceTracker;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.junit.Assert.*;

@RunWith( JUnit4TestRunner.class )
@ExamReactorStrategy( EagerSingleStagedReactorFactory.class )
public class SampleTest {

    @Configuration()
    public Option[] config()
    {
        return options(
            junitBundles(),
            equinox(),

            // this is how you set the default log level when using pax logging (logProfile)
            systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("DEBUG"),            
            
            mavenBundle("org.ops4j.pax.logging", "pax-logging-api"),
            mavenBundle("org.ops4j.pax.logging", "pax-logging-service"),
            mavenBundle("org.ops4j.pax.url", "pax-url-mvn"),
            mavenBundle("org.apache.aries", "org.apache.aries.util", "0.4-SNAPSHOT"),
            mavenBundle("org.apache.aries.proxy", "org.apache.aries.proxy", "0.4-SNAPSHOT"),
            mavenBundle("asm", "asm-all"),
            mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint", "0.3.2-SNAPSHOT"),
            mavenBundle("org.apache.aries.testsupport", "org.apache.aries.testsupport.unit")
        );
    }
    
    private final Set<Bundle> installed = new HashSet<Bundle>();
    
    /**
     * This really should be @After but by the looks of it @After does not get invoked in the
     * context of the running framework and so the installed member variable would be empty :(
     * 
     * This is only really a problem with EagerSingleStagedReactorFactory as the ExamReactorStrategy because
     * otherwise the framework will be recycled for every @Test. 
     */
    public void cleanup() {
    	for (Bundle b : installed) {
    		System.out.println("Clear out "+b.getSymbolicName()+"/"+b.getVersion());
    		if (b.getState() != Bundle.UNINSTALLED) {
	    		try {
	    			b.uninstall();
	    		} catch (BundleException be) {
	    			be.printStackTrace();
	    		}
    		}
    	}
    	
    	installed.clear();
    }
    
    @Test
    public void testNamespace( BundleContext ctx ) throws Exception
    {
    	try {
	    	installBundle(ctx, "nstest.list", makeCollectionHandler(ListHandlerActivator.class, "1.0.0"));
	    	installBundle(ctx, "nstest.sample", makeCollectionTestBundle("(version=1.0.0)"));
	    	
	    	Object list = waitForBlueprint(ctx, "nstest.sample").getComponentInstance("mylist");
	    	assertEquals(Arrays.asList("1", "2", "3", "4", "5"), list);
    	} finally {
    		cleanup();
    	}
    }
    
    @Test
    public void testNamespaceByVersionDistinction( BundleContext ctx ) throws Exception
    {
    	try {
	    	installBundle(ctx, "nstest.list_1.0.0", makeCollectionHandler(ListHandlerActivator.class, "1.0.0"));
	    	installBundle(ctx, "nstest.list_1.1.0", makeCollectionHandler(LinkedListHandlerActivator.class, "1.1.0"));
	    	
	    	// version 1.0.0
	    	
	    	Bundle b = installBundle(ctx, "nstest.sample", makeCollectionTestBundle("(version=1.0.0)"));
	
	    	assertTrue(waitForBlueprint(ctx, "nstest.sample").getComponentInstance("mylist") instanceof ArrayList);
	    	
	    	b.uninstall();
	    	
	    	// version 1.1.0
	    	
	    	b = installBundle(ctx, "nstest.sample", makeCollectionTestBundle("(version=1.1.0)"));
	    	
	    	assertTrue(waitForBlueprint(ctx, "nstest.sample").getComponentInstance("mylist") instanceof LinkedList);
	    	
	    	b.uninstall();
	    	
	    	// Impossible version
	    	try {
	    		installBundle(ctx, "nstest.sample", makeCollectionTestBundle("5.0.0"));
	    		fail("Expected BundleException");
	    	} catch (BundleException be) { /* Good */ }
    	
    	} finally {
    		cleanup();
    	}
    }
    
    @Test
    @Ignore
	// How could this actually work? Should it not be the one that it is wired to in the resolution?
    public void namespace_usage_with_version_range_picks_highest_version( BundleContext ctx ) throws Exception {
    	
    	try {
	    	installBundle(ctx, "nstest.list_1.0.0", makeCollectionHandler(ListHandlerActivator.class, "1.0.0"));
	    	installBundle(ctx, "nstest.list_1.0.1", makeCollectionHandler(ListHandlerActivator.class, "1.0.1"));
	    	installBundle(ctx, "nstest.list_1.0.2", makeCollectionHandler(ListHandlerActivator.class, "1.0.2"));
	    	installBundle(ctx, "nstest.list_1.1.0", makeCollectionHandler(LinkedListHandlerActivator.class, "1.1.0"));
	    	installBundle(ctx, "nstest.list_1.0.3", makeCollectionHandler(ListHandlerActivator.class, "1.0.3"));
    		
    		installBundle(ctx, "nstest.sample", makeCollectionTestBundle(""));
    		
    		assertTrue(waitForBlueprint(ctx, "nstest.sample").getComponentInstance("mylist") instanceof LinkedList);
    		
    	} finally {
    		cleanup();
    	}
    }
    
    @Test
    public void namespace_usage_without_require_capability_picks_higest_version( BundleContext ctx ) throws Exception {
    	try {
	    	installBundle(ctx, "nstest.list_1.0.0", makeCollectionHandler(ListHandlerActivator.class, "1.0.0"));
	    	installBundle(ctx, "nstest.list_1.0.1", makeCollectionHandler(ListHandlerActivator.class, "1.0.1"));
	    	installBundle(ctx, "nstest.list_1.0.2", makeCollectionHandler(ListHandlerActivator.class, "1.0.2"));
	    	installBundle(ctx, "nstest.list_1.1.0", makeCollectionHandler(LinkedListHandlerActivator.class, "1.1.0"));
	    	installBundle(ctx, "nstest.list_1.0.3", makeCollectionHandler(ListHandlerActivator.class, "1.0.3"));

	    	installBundle(ctx, "nstest.sample", makePlainCollectionTestBundle());

    		assertTrue(waitForBlueprint(ctx, "nstest.sample").getComponentInstance("mylist") instanceof LinkedList);
    		
    	} finally {
    		cleanup();
    	}
    }

    private BlueprintContainer waitForBlueprint(BundleContext ctx, String bundle) throws Exception {
    	return waitForBlueprint(ctx, bundle, 5000);
    }
    
    private BlueprintContainer waitForBlueprint(BundleContext ctx, String bundle, long timeout) throws Exception {
    	ServiceTracker tracker = new ServiceTracker(ctx, FrameworkUtil.createFilter("(osgi.blueprint.container.symbolicname="+bundle + ")"), null);
    	tracker.open();
    	
    	return (BlueprintContainer) tracker.waitForService(timeout);    	
    }
    
    private Fixture makeCollectionHandler(Class<?> activator, String version) throws Exception {
    	return ArchiveFixture.newJar()
			.manifest()
				.symbolicName("nstest.list")
				.version(version)
				.attribute("Import-Package", "org.osgi.framework, org.osgi.service.blueprint.reflect, org.apache.aries.blueprint, org.apache.aries.blueprint.metadata, org.w3c.dom")
				.attribute("Bundle-Activator", activator.getName())
				.attribute("Provide-Capability", "org.apache.aries.blueprint.NamespaceHandler;osgi.service.blueprint.namespace:String=http://aries.apache.org/ns/test/list;version:Version=\""+version+"\"")
			.end()
			.binary("nstest/"+activator.getSimpleName()+".class",
					SampleTest.class.getClassLoader().getResourceAsStream("nstest/"+activator.getSimpleName()+".class"))
			.binary("nstest/CollectionNSHandler.class", 
				SampleTest.class.getClassLoader().getResourceAsStream("nstest/CollectionNSHandler.class"))
			.binary("nstest/list.xsd", SampleTest.class.getClassLoader().getResourceAsStream("nstest/list.xsd"))
			.end();    	
    }
    
    private Fixture makePlainCollectionTestBundle() {
    	return ArchiveFixture.newJar()
		.manifest()
			.symbolicName("nstest.sample")
		.end()
		.file("OSGI-INF/blueprint/test.xml", 
				"<blueprint xmlns=\"http://www.osgi.org/xmlns/blueprint/v1.0.0\" xmlns:list=\"http://aries.apache.org/ns/test/list\">" +
				"<list:list id=\"mylist\">1,2,3,4,5</list:list>" +
				"</blueprint>");    	
    }
    
    private Fixture makeCollectionTestBundle(String version) {
    	return ArchiveFixture.newJar()
			.manifest()
				.symbolicName("nstest.sample")
				.attribute("Require-Capability", "org.apache.aries.blueprint.NamespaceHandler;filter:=\"(&(osgi.service.blueprint.namespace=http://aries.apache.org/ns/test/list)"+version+")")
			.end()
			.file("OSGI-INF/blueprint/test.xml", 
					"<blueprint xmlns=\"http://www.osgi.org/xmlns/blueprint/v1.0.0\" xmlns:list=\"http://aries.apache.org/ns/test/list\">" +
					"<list:list id=\"mylist\">1,2,3,4,5</list:list>" +
					"</blueprint>");
    }
    
    private Bundle installBundle(BundleContext ctx, String name, Fixture bundle) throws Exception {
    	ByteArrayOutputStream bout = new ByteArrayOutputStream();
    	bundle.writeOut(bout);
    	
    	Bundle b = ctx.installBundle(name, new ByteArrayInputStream(bout.toByteArray()));
    	installed.add(b);
    	
    	b.start();
    	
    	return b;
    }
}
