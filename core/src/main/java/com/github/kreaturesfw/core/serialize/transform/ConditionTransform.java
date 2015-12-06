package com.github.kreaturesfw.core.serialize.transform;

import java.io.StringReader;

import org.simpleframework.xml.transform.Transform;

import com.github.kreaturesfw.core.parser.ASMLParser;
import com.github.kreaturesfw.core.reflection.Condition;

public class ConditionTransform implements Transform<Condition> {

	@Override
	public Condition read(String toParse) throws Exception {
		ASMLParser parser = new ASMLParser(new StringReader(toParse));
		return parser.booleanExpression();
	}

	@Override
	public String write(Condition condition) throws Exception {
		return condition.toString();
	}

}
