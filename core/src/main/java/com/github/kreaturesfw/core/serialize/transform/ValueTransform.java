package com.github.kreaturesfw.core.serialize.transform;

import java.io.StringReader;

import org.simpleframework.xml.transform.Transform;

import com.github.kreaturesfw.core.parser.ASMLParser;
import com.github.kreaturesfw.core.reflection.Value;

/**
 * Transforms s string from an XML file into a Value class and vice
 * versa. 
 * @author Tim Janus
 */
public class ValueTransform implements Transform<Value> {

	@Override
	public Value read(String str) throws Exception {
		ASMLParser parser = new ASMLParser(new StringReader(str));
		return parser.value();
	}

	@Override
	public String write(Value value) throws Exception {
		return value.toString();
	}

}
