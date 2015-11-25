package com.github.kreaturesfw.core.reflection;

import java.io.StringReader;

import com.github.kreaturesfw.core.error.AngeronaException;
import com.github.kreaturesfw.core.error.ConversionException;

import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.parser.ParseException;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

/**
 * Implementation of a Variable wrapper for the FolFormula.
 * @author Tim Janus
 */
public class FolFormulaVariable extends BaseVariable<FolFormula> {

	/** DCtor: Used by simple xml deserilization */
	public FolFormulaVariable() {}
	
	/** Ctor: Used to generate a simple wrapper mapping directly to an instance */
	public FolFormulaVariable(FolFormula instance) {
		super(instance);
	}

	@Override
	public FolFormula createInstanceFromString(String content) throws AngeronaException {
		FolParserB parser = new FolParserB(new StringReader(content));
		try {
			FolFormula reval = parser.formula(new FolSignature());
			return reval;
		} catch (ParseException e) {
			throw new ConversionException(String.class, FolFormula.class, e);
		}
	}

}
