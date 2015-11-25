package com.github.kreaturesfw.knowhow.parameter;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * The class SkillParameter is responsible
 * @author Tim Janus
 */
public class SkillParameter {
	/** the id (index of the responsible knowhow-statement */
	protected Integer statementId;

	/** the index of the subgoal the parameter belongs to */
	protected Integer subgoalIndex;
	
	/** the name of the skill the parameter belongs to */
	protected String skillName;
	
	/** the index of the parameter in context of the skill */
	protected Integer paramIndex;
	
	/** the value of the parameter represent by a term */
	protected Term<?> paramValue;
	
	public SkillParameter() {}
	
	public SkillParameter(int statementId, int subgoalIndex, String skillName, 
			int paramIndex, Term<?> paramValue) {
		this.statementId = statementId;
		this.subgoalIndex = subgoalIndex;
		this.skillName = skillName;
		this.paramIndex = paramIndex;
		this.paramValue = paramValue;
	}
	
	/** @return the id (index of the responsible knowhow-statement */
	public Integer getKnowhowStatementId() {
		return statementId;
	}

	/** @return the index of the subgoal the parameter belongs to */
	public Integer getSubgoalIndex() {
		return subgoalIndex;
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
		return "("+statementId+", " + subgoalIndex + ", " + skillName + ", " + paramIndex + ", " + paramValue + ")";
	}
}
