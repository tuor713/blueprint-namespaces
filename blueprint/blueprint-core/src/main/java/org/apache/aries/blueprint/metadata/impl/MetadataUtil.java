package org.apache.aries.blueprint.metadata.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanMetadata;

public class MetadataUtil {
    public static boolean isPrototypeScope(BeanMetadata metadata) {
        return (BeanMetadata.SCOPE_PROTOTYPE.equals(metadata.getScope()) || 
                (metadata.getScope() == null && metadata.getId() == null));
    }
    
    public static boolean isSingletonScope(BeanMetadata metadata) {
        return (BeanMetadata.SCOPE_SINGLETON.equals(metadata.getScope())  ||
                (metadata.getScope() == null && metadata.getId() != null));
    }
    
    public static boolean isCustomScope(BeanMetadata metadata) {
        return (metadata.getScope() != null &&
                !BeanMetadata.SCOPE_PROTOTYPE.equals(metadata.getScope()) &&
                !BeanMetadata.SCOPE_SINGLETON.equals(metadata.getScope()));
    }
    
    public static final Comparator<BeanArgument> BEAN_COMPARATOR = new BeanArgumentComparator();

    private static class BeanArgumentComparator implements Comparator<BeanArgument>, Serializable {
		private static final long serialVersionUID = 5020002518922431582L;

		public int compare(BeanArgument object1, BeanArgument object2) {
            return object1.getIndex() - object2.getIndex();
        }        
    }    
    
    public static List<BeanArgument> validateBeanArguments(List<BeanArgument> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return arguments;
        }
        // check if all or none arguments have index attribute
        boolean hasIndexFirst = (arguments.get(0).getIndex() > -1);
        for (int i = 1; i < arguments.size(); i++) {
            boolean hasIndex = (arguments.get(i).getIndex() > -1);
            if ( (hasIndexFirst && !hasIndex) ||
                 (!hasIndexFirst && hasIndex) ) {
                throw new IllegalArgumentException("Index attribute must be specified either on all or none constructor arguments");
            }
        }
        if (hasIndexFirst) {
            // sort the arguments
            List<BeanArgument> argumentsCopy = new ArrayList<BeanArgument>(arguments);
            Collections.sort(argumentsCopy, MetadataUtil.BEAN_COMPARATOR);
            arguments = argumentsCopy;
            
            // check if the indexes are sequential
            for (int i = 0; i < arguments.size(); i++) {
                int index = arguments.get(i).getIndex();
                if (index > i) {
                    throw new IllegalArgumentException("Missing attribute index");                    
                } else if (index < i) {
                    throw new IllegalArgumentException("Duplicate attribute index");
                } // must be the same
            }            
        }
        
        return arguments;
    }
}
