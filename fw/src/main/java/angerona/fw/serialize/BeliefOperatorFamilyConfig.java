package angerona.fw.serialize;

import org.simpleframework.xml.Element;

/**
 * A base class for de/serializing belief-operator-families. It only
 * contains an optional name for the belief-operator-family.
 * 
 * The factory BeliefOperatorFamilyFactory is used to generated different
 * types of belief-operator-families.
 * 
 * @author Tim Janus
 */
public class BeliefOperatorFamilyConfig {
	@Element(name="name", required=false)
	private String name;
	
	public String getName() {
		return name;
	}
}
