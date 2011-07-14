package nstest;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.aries.blueprint.NamespaceHandler;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ListHandlerActivator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		CollectionNSHandler nshandler = new CollectionNSHandler(ArrayList.class);
		
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.service.blueprint.namespace", "http://aries.apache.org/ns/test/list");
		props.put("version", context.getBundle().getVersion());
		
		context.registerService(NamespaceHandler.class, nshandler, props);
	}

	public void stop(BundleContext arg0) throws Exception {}

}
