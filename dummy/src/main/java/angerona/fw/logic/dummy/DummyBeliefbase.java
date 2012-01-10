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

/**
 * Just a dummy belief base for testing purposes.
 * TODO: Use operators for changing the beliefbase.
 * @author Tim Janus
 */
public class DummyBeliefbase extends BaseBeliefbase {

	FolBeliefSet fbs = new FolBeliefSet();
	
	private FolSignature signature;
	
	public DummyBeliefbase() {
		
	}
	
	public DummyBeliefbase(DummyBeliefbase other) {
		fbs.addAll(other.fbs);
	}
	
	public DummyBeliefbase(String filename) throws IOException {
		super(filename);
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
	protected void parseInt(BufferedReader br) throws ParserException, IOException {
		FolParser fp = new FolParser();
		fbs = (FolBeliefSet)fp.parseBeliefBase(br);
		signature = fp.getSignature();
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
