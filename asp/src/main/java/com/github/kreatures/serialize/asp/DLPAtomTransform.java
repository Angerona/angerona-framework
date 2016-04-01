package com.github.kreatures.serialize.asp;

import java.io.StringReader;

import net.sf.tweety.lp.asp.parser.ASPParser;
import net.sf.tweety.lp.asp.parser.InstantiateVisitor;
import net.sf.tweety.lp.asp.syntax.DLPAtom;

import org.simpleframework.xml.transform.Transform;

public class DLPAtomTransform implements Transform<DLPAtom> {

	@Override
	public DLPAtom read(String value) throws Exception {
		ASPParser parser = new ASPParser(new StringReader(value));
		return (DLPAtom) new InstantiateVisitor().visit(parser.Atom(), null);
	}

	@Override
	public String write(DLPAtom value) throws Exception {
		return value.toString();
	}

}
