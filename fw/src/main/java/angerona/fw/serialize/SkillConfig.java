package angerona.fw.serialize;

import java.util.List;

public interface SkillConfig {
	/** @return the name of the Skill */
	public String getName();

	/** @return ordered list of statements performing the Skill */
	public List<Statement> getStatements();
}
