package angerona.fw.serialize.internal;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;

import angerona.fw.serialize.SkillConfig;
import angerona.fw.serialize.Statement;

@Root(name="skill-import")
public class SkillConfigImport implements SkillConfig {

	@Element
	private File source;

	@Resolve
	public SkillConfig substitute() throws Exception {
		return new Persister().read(SkillConfigReal.class, source);
	}
	
	@Override
	public String getName() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public List<Statement> getStatements() {
		throw new IllegalStateException("Method not supported.");
	}

	public static SkillConfigImport getTestObject()  {
		SkillConfigImport reval = new SkillConfigImport();
		reval.source = new File("config/skills/QueryAnswer.xml");
		return reval;
	}
}
