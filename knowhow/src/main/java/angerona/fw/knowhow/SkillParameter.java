package angerona.fw.knowhow;

public class SkillParameter {
	protected Integer numKnowhowStatement;

	protected Integer numSubgoal;
	
	protected String skillName;
	
	protected Integer paramIndex;
	
	protected String paramValue;
	
	public Integer getNumKnowhowStatement() {
		return numKnowhowStatement;
	}

	public Integer getNumSubgoal() {
		return numSubgoal;
	}

	public String getSkillName() {
		return skillName;
	}

	public Integer getParamIndex() {
		return paramIndex;
	}

	public String getParamValue() {
		return paramValue;
	}
	
	@Override
	public String toString() {
		return "("+numKnowhowStatement+", " + numSubgoal + ", " + skillName + ", " + paramIndex + ", " + paramValue + ")";
	}
}
