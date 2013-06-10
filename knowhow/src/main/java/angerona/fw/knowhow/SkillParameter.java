package angerona.fw.knowhow;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * The class SkillParameter is responsible
 * @author Tim Janus
 */
public class SkillParameter {
	/** the id (index of the responsible knowhow-statement */
	protected Integer numKnowhowStatement;

	/** the index of the subgoal the parameter belongs to */
	protected Integer numSubgoal;
	
	/** the name of the skill the parameter belongs to */
	protected String skillName;
	
	/** the index of the parameter in context of the skill */
	protected Integer paramIndex;
	
	/** the value of the parameter represent by a term */
	protected Term<?> paramValue;
	
	/** @return the id (index of the responsible knowhow-statement */
	public Integer getNumKnowhowStatement() {
		return numKnowhowStatement;
	}

	/** @return the index of the subgoal the parameter belongs to */
	public Integer getNumSubgoal() {
		return numSubgoal;
	}
	
	/** @return the name of the skill the parameter belongs to */
	public String getSkillName() {
		return skillName;
	}
	
	/** @return the index of the parameter in context of the skill */
	public Integer getParamIndex() {
		return paramIndex;
	}
	
	/** @return the value of the parameter represent by a string */
	public Term<?> getParamValue() {
		return paramValue;
	}
	
	@Override
	public String toString() {
		return "("+numKnowhowStatement+", " + numSubgoal + ", " + skillName + ", " + paramIndex + ", " + paramValue + ")";
	}
}
