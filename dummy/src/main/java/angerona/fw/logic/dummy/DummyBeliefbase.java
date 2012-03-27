package angerona.fw.logic.dummy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.firstorderlogic.FolBeliefSet;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.parser.ParseException;

/**
 * Just a dummy belief base for testing purposes.
 *
 * @author Tim Janus
 */
public class DummyBeliefbase extends BaseBeliefbase {

	/** the beliefset of the beliefbase */
	FolBeliefSet fbs = new FolBeliefSet();
	
	/** the signature of the beliefbase */
	private FolSignature signature;
	
	/** Default Ctor: Needed for dynamic instantiation */
	public DummyBeliefbase() {	
	}
	
	/** Copy-Ctor: Copies a Dummy-Beliefbase, the copied object has the
	 * 	same object id like the given parameter.
	 * 	@param other
	 */
	public DummyBeliefbase(DummyBeliefbase other) {
		super(other);
		fbs.addAll(other.fbs);
	}
	
	@Override
	public Signature getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		String reval = "";
		
		for(Formula f : fbs)
			reval += f.toString()+"\n";
		
		return reval;
	}

	@Override
	public Object clone() {
		return new DummyBeliefbase(this);
	}

	// Belief base is a simple FOL Beliefbase.
	@Override
	protected void parseInt(BufferedReader br) throws ParseException, IOException {
		FolParser fp = new FolParser();
		try{
			fbs = (FolBeliefSet)fp.parseBeliefBase(br);
			signature = fp.getSignature();
		} catch (ParserException ex) {
			throw new ParseException(ex.getMessage());
		}
	}

	@Override
	public String getFileEnding() {
		return "dum";
	}

	@Override
	public List<String> getAtoms() {
		List<String> reval = new LinkedList<String>();
		for(FolFormula ff : fbs)
			reval.add(ff.toString());
		return reval;
	}
}
