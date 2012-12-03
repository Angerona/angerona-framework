package angerona.fw.serialize.transform;

import java.io.StringReader;

import org.simpleframework.xml.transform.Transform;

import angerona.fw.parser.DAMLParser;
import angerona.fw.reflection.Condition;

public class ConditionTransform implements Transform<Condition> {

	@Override
	public Condition read(String toParse) throws Exception {
		DAMLParser parser = new DAMLParser(new StringReader(toParse));
		return parser.booleanExpression();
	}

	@Override
	public String write(Condition condition) throws Exception {
		return condition.toString();
	}

}
