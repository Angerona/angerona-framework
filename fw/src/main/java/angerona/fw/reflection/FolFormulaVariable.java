package angerona.fw.reflection;

import java.io.StringReader;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import angerona.fw.error.AngeronaException;

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
	protected FolFormula createInstanceFromString(String content) throws AngeronaException {
		FolParserB parser = new FolParserB(new StringReader(content));
		try {
			FolFormula reval = parser.formula(new FolSignature());
			return reval;
		} catch (ParseException e) {
			throw new AngeronaException("Error during string to FolFormula Conversion: " + e.getMessage());
		}
	}

}
