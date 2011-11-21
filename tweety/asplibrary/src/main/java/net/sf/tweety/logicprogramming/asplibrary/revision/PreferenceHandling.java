package net.sf.tweety.logicprogramming.asplibrary.revision;

import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;
import net.sf.tweety.util.Pair;


/**
 * The implementation orients on the diploma thesis of Mirja Böhmer
 * 
 * in this class a variant of the approach
 * "A Preference-Based Framework for Updating Logic Programs" by James P.
 * Delgrande, Torsten Schaub and Hans Tompits is implemented, respectively the
 * operator *1 is used; first step: defaultification of two given programs
 * second step: computation of the answer sets of the concatenation of the two
 * defaulticated programs third step: computation of the revised program with
 * the help of these answer sets last step: computation of the answer sets of
 * the revised program
 * 
 * We are only check for conflicting rules and remove this rules with lesser priority.
 * 
 * @author Tim Janus
 **/
public class PreferenceHandling implements RevisionApproach {
	
	@Override
	public Program revision(Program p1, Program p2, Solver solver) throws SolverException {
		Program combined = new Program();
		Program concat = new Program();
		
		// Defaultication of given programs.
		Program pd1 = Program.defaultification(p1);
		Program pd2 = Program.defaultification(p2);
		
		// list of conflicting rules of the defaultificated programs
		List<Pair<Rule, Rule>> conflictsDef = getConflictingRules(pd1, pd2);

		// Assumption: Index of rules in p equals index of rules pd.
		// TODO: Proof if this assumption is really true.
		List<Pair<Rule, Rule>> conflicts = new LinkedList<Pair<Rule,Rule>>();
		for(Pair<Rule, Rule> defConf : conflictsDef) {
			int index1 = pd1.indexOf(defConf.getFirst());
			int index2 = pd2.indexOf(defConf.getSecond());
			
			conflicts.add(new Pair<Rule, Rule>(p1.get(index1), p2.get(index2)));
		}
		
		// get answerset of combined defaultificated programs.
		concat.add(pd1);
		concat.add(pd2);
		AnswerSetList asDefault = solver.computeModels(concat, 5);
		
		/*
		System.out.println("Default Program: " + concat);
		System.out.println("Default answer: " + asDefault);
		System.out.println("Conflicts: " + conflicts);
		*/
		
		// proof which rules can be removed from concat:
		// Let the rule R be the higher prioritized Rule in a conflict pair.
		// First test if the body of R is in the answerset but the head is not in the answerset.
		// If this is true mark the lower priorizied rule for remove.
		Set<Rule> toRemoveCollection = new HashSet<Rule>();
		for(Pair<Rule, Rule> conflict : conflicts) {
			for(AnswerSet as : asDefault) {
				if(	as.containsAll(conflict.getSecond().getBody()) &&
					!as.containsAll(conflict.getSecond().getHead())) {
					toRemoveCollection.add(conflict.getFirst());
				}
			}
		}
		
		
	//	System.out.println("To Remove: " + toRemoveCollection);
		
		combined.add(p1);
		combined.add(p2);
		combined.removeAll(toRemoveCollection);
		
		return combined;
	}
	
	/**
	 * Helper method: Finds all pairs of conflicting rules in program p1 and p2.
	 * A conflicting rule is a pair(r1, r2 | r1 € p1, r2 € p2, H(r1) = -H(r2)) 
	 * @param p1	The first program 
	 * @param p2	The second program
	 * @return		A list of all pairs representing the conflicting rules in p1 and p2.
	 */
	protected static List<Pair<Rule, Rule>> getConflictingRules(Program p1, Program p2) {
		int c1 = p1.size();
		int c2 = p2.size();
		List<Pair<Rule, Rule>> reval = new LinkedList<Pair<Rule,Rule>>();
		
		for(int i=0; i<c1; ++i) {
			Rule r1 = p1.get(i);
			if(r1.isConstraint())
				continue;
			
			// Create negated head of rule 1.
			Literal head1 = r1.getHead().get(0);
			Literal negHead1 = null;
			if(head1 instanceof Atom) {
				negHead1 = new Neg(head1.getAtom());
			} else if(head1 instanceof Neg) {
				negHead1 = head1.getAtom();
			} else {
				throw new RuntimeException("Head Atom must be normal or strict negated.");
			}
			
			// try to find the negated head in the rules of the other program.
			for(int k=0; k<c2; ++k) {
				Rule r2 = p2.get(k);
				if(r2.isConstraint())
					continue;
				if(r2.getHead().get(0).equals(negHead1)) {
					reval.add(new Pair<Rule, Rule>(r1, r2));
				}
			}
		}
		
		return reval;
	}
	
	/*
	 * Temporary functional test method.
	 */
	public static void main(String [] args) throws SolverException {
		PreferenceHandling ph = new PreferenceHandling();
		// Example from Mirja diplom thesis.
		String program1 = "a.\nb:-not c.";
		String program2 = "-a:-not b.";
		program1 = "sleep:-not tv_on.\nnight.\ntv_on.\nwatch_tv:-tv_on.";
		program2 = "-tv_on:-power_failure.\npower_failure.";
		
		DLV clingo = new DLV("/home/janus/workspace/angerona/software/test/src/main/tools/solver/asp/dlv/dlv.bin");
		
		Program p1 = Program.loadFrom(new StringReader(program1));
		Program p2 = Program.loadFrom(new StringReader(program2));
		
		System.out.println("P1:");
		System.out.println(p1.toString()+"\n" + clingo.computeModels(p1, 5) + "\n");
		
		System.out.println("P2:");
		System.out.println(p2.toString()+"\n" + clingo.computeModels(p2, 5) + "\n");
		
		Program r = ph.revision(p1, p2, clingo);		

		System.out.println("Revised:");
		System.out.println(r.toString()+"\n\n");
		
		System.out.println(clingo.computeModels(r, 5));
	}
}
