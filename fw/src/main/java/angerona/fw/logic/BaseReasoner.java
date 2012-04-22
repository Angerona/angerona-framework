package angerona.fw.logic;

import net.sf.tweety.Reasoner;

/**
 * Base class for all reasoner used by the Angerona project.
 * To query the reasoner a subset of the FOL Language defined in tweety is used.
 * It is the responsibility of the Reasoner implementation to translate the given
 * query into its native language. 
 * 
 * @author Tim Janus
 */
public abstract class BaseReasoner 
	extends Reasoner{
	
	public BaseReasoner() {
		super(null);	
	}
	
	public void setBeliefbase(BaseBeliefbase bb) {
		this.beliefBase = bb;
	}
	
	/**
	 * @return the class definition of the belief base this reasoner supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
