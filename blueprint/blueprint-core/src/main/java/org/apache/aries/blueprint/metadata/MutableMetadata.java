package org.apache.aries.blueprint.metadata;

public interface MutableMetadata<T> {
	/** 
	 * Freeze this component and all subcomponents
	 * 
	 * Can metadata be referenced from two places?
	 * What about clients who want to retain a copy of the metadata (tough luck! not working today anyway)
	 */
	T freeze();
	
	/**
	 * Returns a new instance of this metadata. The returned metadata should again be mutable.
	 */
	T copy();
}
