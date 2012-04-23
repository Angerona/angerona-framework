package angerona.fw.serialize;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Contains the configuration for a Skill. It holds the
 * Skill name and an ordered list of statements which process
 * the Skill.
 * @author Tim Janus
 */
@Root(name="skill")
public class SkillConfigReal implements SkillConfig {
		
	/** the name of the Skill */
	@Element(name="name")
	private String name;

	/** ordered list of statements performing the Skill */
	@ElementList(name="statements", inline=true)
	private List<Statement> statements = new LinkedList<Statement>();
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Statement> getStatements() {
		return statements;
	}
	
	/**
	 * Reads a skill description from the given xml file.
	 * @param source	reference to the xml file.
	 * @return	An java-object representing the skill.
	 */
	public static SkillConfig loadXml(File source) {
		return SerializeHelper.loadXml(SkillConfigReal.class, source);
	}	
	
	public static void main(String [] args) {
		SkillConfigReal test = new SkillConfigReal();
		test.name = "Test";
		Statement st = Statement.getTestObject();
		test.statements.add(st);
		SerializeHelper.outputXml(test, System.out);
	}
}
