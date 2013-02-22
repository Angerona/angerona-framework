package angerona.fw.DefendingAgent.Prover;

import java.util.HashMap;

import se.sics.jasper.Query;
import se.sics.jasper.SICStus;

/**
 * This class lets users to ask the theorem prover to find a closed tree for a
 * set of formulas; public method prove interacts with SICStus Prolog
 * implementation of SeqS using package se.sics.jasper's classes; these
 * implementations consist of files with 'sav' extension
 */
public class Prover {

	private String[] kFormulas;
	private String formulaToProve;

	/**
	 * The following object is used to interact with the SICStus Prolog kernel
	 * */
	SICStus sp;

	/**
	 * C´tor
	 */
	public Prover() {
		sp = null;
	}

	/**
	 * Read the knowledgebase and the formula to prove and give it the prolog
	 * solver to prove
	 * 
	 * @param kFormulas
	 *            the knowledgebase
	 * @param formulaToProve
	 *            the formula to prove
	 * @param chooseProver
	 *            1 Cumulative logic, 
	 *            2 Loop-Cumulative logic, 
	 *            3 Preferential logic, 
	 *            4 Rational logic, 
	 *            5 Rational logic with free variables
	 * @return String with the result or null
	 */
	public String prove(String[] kFormulas, String formulaToProve,
			int chooseProver) {
		this.kFormulas = kFormulas;
		this.formulaToProve = formulaToProve;
		Query q;
		HashMap<Object, Object> map = new HashMap<Object, Object>();

		/* Initialize the SICStus Prolog engine */
		try {
			if (sp == null) {
				/* Instantiating a SICStus object */
				sp = new SICStus();
			}
			/* Parameter 1 determines the KLM logic to consider */
			switch (chooseProver) {
			case 1: {
				sp.restore("tct.sav");
				break;
			}
			case 2: {
				sp.restore("tclt.sav");
				break;
			}
			case 3: {
				sp.restore("tpt.sav");
				break;
			}
			case 4: {
				sp.restore("trt.sav");
				break;
			}
			case 5: {
				sp.restore("trtfree.sav");
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
			for (int i = 0; i < 32; i++) {
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
			q = sp.openPrologQuery(goal, map);
			if (q.nextSolution()) {
				// Success
				return new String(map.toString());

			} else {
				// Failure
				return null;
			}

		} catch (Exception choosingKLM) {
			System.out.println("\nERROR connecting to SICStus Prolog engine");
		}
		return null;

	}
}