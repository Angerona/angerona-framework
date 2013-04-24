package angerona.fw.defendingagent.Prover;

import java.util.HashMap;

import se.sics.jasper.Query;
import se.sics.jasper.SICStus;
import se.sics.jasper.SPException;

/**
 * This class lets users to ask the theorem prover to find a closed tree for a
 * set of formulas; public method prove interacts with SICStus Prolog
 * implementation of SeqS using package se.sics.jasper's classes; these
 * implementations consist of files with 'sav' extension
 */
public class Prover {
	
	public enum Solver {
	    CUMMULATIV, LOOP_CUMMULATIV, PREFERENTIAL, 
	    RATIONAL, FREE_RATIONAL
	}

	private String[] kFormulas;
	private String formulaToProve;

	/**
	 * The following object is used to interact with the SICStus Prolog kernel
	 * */
	private static SICStus sp;

	/**
	 * C´tor
	 */
	public Prover() {
		if (sp == null) {
			/* Instantiating a SICStus object */
			try {
				sp = new SICStus();
			} catch (SPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Read the knowledgebase and the formula to prove and give it the prolog
	 * solver to prove
	 * 
	 * @param kFormulas
	 *            the knowledgebase
	 * @param formulaToProve
	 *            the formula to prove
	 * @param chooseSolver
	 *            CUMMULATIV - Cumulative logic, 
	 *            LOOP_CUMMULATIV - Loop-Cumulative logic, 
	 *            PREFERENTIAL - Preferential logic, 
	 *            RATIONAL - Rational logic, 
	 *            FREE_RATIONAL - Rational logic with free variables
	 * @return String with the result or null
	 */
	public boolean prove(String[] kFormulas, String formulaToProve,
			Solver chooseSolver) {
		this.kFormulas = kFormulas;
		this.formulaToProve = formulaToProve;
		Query q;
		HashMap<Object, Object> map = new HashMap<Object, Object>();

		/* Initialize the SICStus Prolog engine */
		try {
			/* Parameter 1 determines the KLM logic to consider */
			switch (chooseSolver) {
			case CUMMULATIV: {
				sp.restore("resources/tct.sav");
				break;
			}
			case LOOP_CUMMULATIV: {
				sp.restore("resources/tclt.sav");
				break;
			}
			case PREFERENTIAL: {
				sp.restore("resources/tpt.sav");
				break;
			}
			case RATIONAL: {
				sp.restore("resources/trt.sav");
				break;
			}
			case FREE_RATIONAL: {
				sp.restore("resources/trtfree.sav");
				break;
			}
			}

			/*
			 * The following statements build the goal to query to the theorem
			 * prover
			 */
			/*
			 * Step 1: formulas of the knowledge base must be in the KLM
			 * language
			 */
			String kBaseList = new String("[");
			for (int i = 0; i < kFormulas.length; i++) {
				String currentFormula = this.kFormulas[i];
				if (currentFormula.length() > 0)
					kBaseList = kBaseList + currentFormula + ",";
			}
			if (kBaseList.charAt(kBaseList.length() - 1) == ',')
				kBaseList = kBaseList.substring(0, kBaseList.length() - 1);
			kBaseList = kBaseList + "]";

			String goal = new String("parseinput(" + kBaseList + ").");
			q = sp.openPrologQuery(goal, map);
			if (!(q.nextSolution())) {
				System.err
						.println("Error in the knowledge base: you cannot use nested conditionals");
			}

			/* Step 2: formulas to prove base must be in the KLM language */
			String toProveList = new String("[" + this.formulaToProve + "]");

			goal = new String("parseinput(" + toProveList + ").");
			System.out.println("GOAL: "+goal);
			q = sp.openPrologQuery(goal, map);
			if (!(q.nextSolution())) {
				System.err
						.println("Error in the formula to prove: you cannot use nested conditionals");
			}
			/*
			 * Step 3: finding a derivation of the formula from the knowledge
			 * base by using the calculi for KLM logics
			 */

			goal = new String("unsatinterface(" + kBaseList + "," + toProveList
					+ ",Tree).");
			System.out.println("kbaselist:" + kBaseList);
			q = sp.openPrologQuery(goal, map);
			if (q.nextSolution()) {
				// Success
				//return new String(map.toString());
				return true;

			} else {
				// Failure
				//return null;
				return false;
			}

		} catch (Exception choosingKLM) {
			
			System.out.println("\nERROR SICStus Prolog engine");
			choosingKLM.printStackTrace();
		}
		return false;

	}
}