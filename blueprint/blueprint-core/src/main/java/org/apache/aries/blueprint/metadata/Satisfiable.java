package org.apache.aries.blueprint.metadata;

/*
 * This is for config admin support of waiting for properties
 */
public interface Satisfiable {
	boolean isSatisfied();
	void addListener(SatisfactionListener listener);
}
