package angerona.fw.logic;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * A confidential target as defined in Def 3. in 
 * "Torwards Enforcement of Confidentially in Agent Interactions"
 * by Biskup, Kern-Isberner, Thimm
 * 
 * @author Tim Janus
 */
public class ConfidentialTarget implements Cloneable {
	/** name of the agent who should not get the information */
	private String name;
	
	/** formula representing the confidential information */
	private FolFormula information;
	
	/**
	 * Ctor: Generates a new confidential target with the given parameters.
	 * @param name					name of the agent who should not get the information
	 * @param information			formula representing the confidential information
	 */
	public ConfidentialTarget(String name, FolFormula information) {
		this.name = name;
		this.information = information;
	}
	
	/**	@return name of the agent who should not get the information */
	public String getSubjectName() {
		return name;
	}
	
	/** @return formula representing the confidential information */
	public Formula getInformation() {
		return information;
	}
	
	@Override
	public Object clone() {
		return new ConfidentialTarget(name, information);
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + information + ")";
	}
}
