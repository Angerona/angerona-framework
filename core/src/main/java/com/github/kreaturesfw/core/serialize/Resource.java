package com.github.kreaturesfw.core.serialize;

/**
 * General resource interface for resources of the Angerona project like
 * agent-configuration etc.
 * @author Tim Janus
 */
public interface Resource {
	/**
	 * @return A string representing the name of the resource.
	 */
	String getName();
	
	/**
	 * @return a string containing a description for the resource.
	 */
	String getDescription();
	
	/**
	 * @return A string representing the resource type of this resource.
	 */
	String getResourceType();
	
	/** 
	 *  @return A multi level category name where '/' indicates that a new category level begins 
	 *	scm/asp is a category with two levels which is represented by two nodes in a tree
	 *	view for example. An empty string indicates no category. 
	 */
	String getCategory();
}
