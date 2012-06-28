package angerona.fw.logic;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * A secret as defined Def. 4 in "Agent-based Epistemic Secrecy" 
 * of Krümpelmann and Kern-Isberner.
 * The belief/reasoning-operator is not dynamically yet, but the
 * reasoning operator linked to the used knowledge base is used.
 * 
 * For example:
 * We have the agents Alice, Bob and Claire. And Bob does not want
 * his wife Alice to know that he has an affair with Claire. Also
 * he does not want Claire to know that he has children.
 * 
 * @remark
 * In Angerona Bobs data has the Beliefbases B_A and B_C
 * representing the view on Alice or Claire. The reasoning operators
 * linked to these beliefbases are used to instead one defined in
 * this data-strcuture.
 * TODO: Change this
 * 
 * @author Tim Janus
 */
public class Secret implements Cloneable {
	public static final String DEFAULT_BELIEFCHANGE = "__DEFAULT__";
	
	/** name of the agent who should not get the information */
	private String name;
	
	/** formula representing the confidential information */
	private FolFormula information;
	
	/** the java class name which should be used to proof this secret */
	private String beliefChangeClassName;
	/**
	 * Ctor: Generates a new confidential target with the given parameters.
	 * @param name					name of the agent who should not get the information
	 * @param information			formula representing the confidential information
	 */
	public Secret(String name, FolFormula information) {
		this(name, information, DEFAULT_BELIEFCHANGE);
	}
	
	public Secret(String name, FolFormula information,
			String changeClass) {
		this.name = name;
		this.information = information;
		this.beliefChangeClassName = changeClass;
	}
	
	/**	@return name of the agent who should not get the information */
	public String getSubjectName() {
		return name;
	}
	
	/** @return formula representing the confidential information */
	public Formula getInformation() {
		return information;
	}
	
	/** 
	 * @return 	the full java class name which should be used for revision 
	 *			might be DEFAULT_CHANGEBELIEF with the meaning the default
	 *			change operator of the beliefbase should be used.
	 */
	public String getBeliefChangeClassName() {
		return beliefChangeClassName;
	}
	
	@Override
	public Object clone() {
		return new Secret(name, information);
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + information + ")";
	}
}
