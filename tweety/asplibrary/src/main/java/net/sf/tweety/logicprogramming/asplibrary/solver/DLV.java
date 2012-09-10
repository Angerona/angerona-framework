package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logicprogramming.asplibrary.util.*;

/**
 * wrapper class for the dlv answer set solver command line
 * utility.
 * 
 * @author Thomas Vengels, Tim Janus
 *
 */
public class DLV extends SolverBase {

	String path2dlv = null;
	
	public DLV(String path2dlv) {
		this.path2dlv = path2dlv;
	}
	
	public AnswerSetList computeModels(Program p, int models) throws SolverException{
		
		return runDLV(p,models,null);
		
	}
	
	protected AnswerSetList parseResults() throws SolverException {
		AnswerSetList ret = new AnswerSetList();
				
		// process results
		List<Literal> lastAS = null;
		for (String s : ai.getOutput()) {
			if (s.length() <= 0)
				continue;
		
			// answer sets starts with a '{'
			if (s.charAt(0) == '{') {
				if (lastAS != null) {
					ret.add( new AnswerSet(lastAS,0,0));
				}
				lastAS = parseAnswerSet(s);
			}
			// answer set with weak constraints
			else if (s.startsWith("Best model")) {
				
				if (lastAS != null) {
					ret.add( new AnswerSet(lastAS,0,0));
				}
				
				s = s.substring(11);
				lastAS = parseAnswerSet(s);
			}
			// Cost of best model
			else if (s.startsWith("Cost")) {
				s = s.substring(25, s.length()-2 );
				String[] wl = s.split(":");
				int weight = Integer.parseInt(wl[0]);
				int level = Integer.parseInt(wl[1]);
				ret.add(new AnswerSet(lastAS,weight,level));
				lastAS = null;
			}
			// error
			else {
				
			}
		}
		
		if (lastAS != null)
			ret.add( new AnswerSet(lastAS,0,0));
		
		return ret;
	}
	
	protected AnswerSetList runDLV(Program p, int nModels, String otherOptions) throws SolverException {
	
		checkSolver(path2dlv);
		String cmdLine = path2dlv + " -- " + "-N=" + nModels; 
		
		// try running dlv
		try {
			ai.executeProgram(cmdLine,p.toStringFlat());
		} catch (Exception e) {
			System.out.println("dlv error!");
			e.printStackTrace();
		}
		
		checkErrors();	
		return parseResults();
	}
	
	protected List<Literal> parseAnswerSet(String s) {
		List<Literal> ret = null;
		try {
			ELPParser ep = new ELPParser( new StringReader( s ));
			ret = ep.dlv_answerset();
		} catch (Exception e) {
			System.err.println("dlv::parseAnswerSet error");
			e.printStackTrace();
		}
		return ret;
	}

	
	@Override
	public AnswerSetList computeModels(String s, int maxModels) throws SolverException {

		String cmdLine = path2dlv + " -- " + "-N=" + maxModels; 

		checkSolver(path2dlv);
		// try running dlv
		try {
			ai.executeProgram(cmdLine,s);
		} catch (Exception e) {
			System.out.println("dlv error!");
			e.printStackTrace();
		}
		checkErrors();	
		return parseResults();
	}

	@Override
	public AnswerSetList computeModels(List<String> files, int maxModels) throws SolverException {
		// TODO Auto-generated method stub
		return null;
	}
}
