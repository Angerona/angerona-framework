package net.sf.tweety.logicprogramming.asplibrary.solver;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logicprogramming.asplibrary.util.*;

import java.io.StringReader;
import java.util.*;


public class Clingo extends SolverBase {

	protected String path2clingo = null;
	
	public Clingo(String path2clingo) {
		this.path2clingo = path2clingo;
	}
	
	
	private List<Literal> parseAnswerSet(String s) {
		List<Literal> ret = null;
		try {
			ELPParser ep = new ELPParser( new StringReader( s ));
			ret = ep.clasp_answerset();
		} catch (Exception e) {
			System.err.println("clingo: error parsing answer set!");
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public AnswerSetList computeModels(Program p, int maxModels) throws SolverException {
		checkSolver(path2clingo);
		try {
			ai.executeProgram( path2clingo, p.toStringFlat() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.checkErrors();
		return this.buildASL(ai.getOutput());
	}

	
	@Override
	public AnswerSetList computeModels(String s, int maxModels) throws SolverException {
		checkSolver(path2clingo);
		try {
			ai.executeProgram( path2clingo, s );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.checkErrors();
		return this.buildASL(ai.getOutput());
	}

	
	@Override
	public AnswerSetList computeModels(List<String> files, int maxModels) throws SolverException {
		checkSolver(path2clingo);
		try {			
			LinkedList<String> f2 = new LinkedList<String>(files);
			f2.addFirst(path2clingo);
			ai.executeProgram(f2, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.checkErrors();
		return this.buildASL(ai.getOutput());				
	}
	
	
	@Override
	protected void checkErrors() throws SolverException {
		// process possible errors and throw exception
		if (ai.getError().size() > 0) {
			// skip any warning, anything else is critical!
			Iterator<String> iter = ai.getError().iterator();
			while (iter.hasNext()) {
				String l = iter.next();
				
				if (l.startsWith("% warning"))
					; // TODO: Find a warning policy like logging it at least.
				
				if (l.startsWith("ERROR:")) {
					if (iter.hasNext())
						l += iter.next();
					
					throw new SolverException( l, SolverException.SE_ERROR );
				}
					
			}
		}
	}
	
	
	protected AnswerSetList buildASL(List<String> output) {
		// process output and return answer set
		AnswerSetList asl = new AnswerSetList();
		boolean prevIsAnswer = false;
		for (String s : output) {
			//System.out.println(s);
			if (s.startsWith("Answer:")) {
				prevIsAnswer = true;
			} else if (prevIsAnswer) {
				prevIsAnswer = false;
				List<Literal> lits = parseAnswerSet(s);
				if (lits != null)
					asl.add( new AnswerSet(lits,0,0));
			}
		}
		return asl;
	}
}
