package nstest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.metadata.Builder;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CollectionNSHandler implements NamespaceHandler {
	private final Class<?> prototype;
	
	public CollectionNSHandler(Class<?> prototype) {
		this.prototype = prototype;
	}
	
	public URL getSchemaLocation(String namespace) {
		return getClass().getResource("list.xsd");
	}

	public Set<Class> getManagedClasses() {
		return null;
	}

	public Metadata parse(Element element, ParserContext context) {
		String[] elements = element.getTextContent().split("\\s*,\\s*");
		
		Builder builder = context.getMetadataBuilder();

		List<Metadata> values = new ArrayList<Metadata>();
		for (String e : elements) {
			values.add(builder.newValue().stringValue(e));
		}
		
		return builder.newBean()
			.id(element.getAttribute("id"))
			.className(prototype.getName())
			.arguments(builder.newBeanArgument().value(
					builder.newCollection().values(values)));
	}

	public ComponentMetadata decorate(Node node, ComponentMetadata component, ParserContext context) {
		throw new UnsupportedOperationException();
	}

}
