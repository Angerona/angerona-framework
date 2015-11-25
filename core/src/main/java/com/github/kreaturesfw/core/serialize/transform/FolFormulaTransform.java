package com.github.kreaturesfw.core.serialize.transform;

import java.io.StringReader;

import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML Transform implementation for FOL Formulas.
 * @author Tim Janus
 */
public class FolFormulaTransform implements Transform<FolFormula>{

	@Override
	public FolFormula read(String strFormula) throws Exception {
		FolParserB parser = new FolParserB(new StringReader(strFormula));
		return parser.formula(new FolSignature());
	}

	@Override
	public String write(FolFormula formula) throws Exception {
		return formula.toString();
	}

}
