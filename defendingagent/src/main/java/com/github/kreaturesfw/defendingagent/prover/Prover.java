package com.github.kreaturesfw.defendingagent.prover;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JOptionPane;

import se.sics.jasper.Query;
import se.sics.jasper.SICStus;
import se.sics.jasper.SPException;

/**
 * This class lets users to ask the theorem prover to find a closed tree for a
 * set of formulas; public method prove interacts with SICStus Prolog
 * implementation of SeqS using package se.sics.jasper's classes; these
 * implementations consist of files with 'sav' extension
 * 
 * SICStus only allows calls from the first thread that calls it. To make
 * this interface to the prover engine thread-safe, A seperate worker thread is
 * created that handles all calls to the SICStus prolog engine. The data
 * is handed over via a rendezvouz channel using two SynchronousQueues that
 * links the prover-method with the workerThread method. 
 */
public class Prover {
	
	private static Prover instance = new Prover();
	
	public enum InferenceSystem {
	    CUMMULATIV, LOOP_CUMMULATIV, PREFERENTIAL, 
	    RATIONAL, FREE_RATIONAL
	}
	
	private static SynchronousQueue<ProverInput> inputQueue = new SynchronousQueue<ProverInput>();
	private static SynchronousQueue<Object> outputQueue = new SynchronousQueue<Object>();
	private static boolean run = true;

	/**
	 * The following object is used to interact with the SICStus Prolog kernel
	 * */
	private static SICStus sp = null;

	
	/**
	 * Returns the singleton instance of this prover class.
	 * @return the singleton instance of this prover class.
	 */
	public static Prover getInstance() {
		return instance;
	}
	
	/**
	 * C´tor, starting the seperate worker thread (see above for an explanation)
	 */
	private Prover() {
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				if (sp == null) {
					/* Instantiating a SICStus object */
					try {
						System.out.println("initialising sicstus runtime engine");
						sp = new SICStus();
					} catch (SPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error with SICStus: Couldn't initialise sicstus runtime engine ", "SICStus error", JOptionPane.ERROR_MESSAGE);
					}
				}
				workerThread();
			}
		}).start();
	}
	
	/**
	 * Stops the current worker thread rendering this Singleton-class unusable.
	 * Use with care!
	 */
	public void stopSICStusThread() {
		run = false;
	}
	
	private void workerThread() {
		while(run) {
			ProverInput input;
				try {
					input = inputQueue.take();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					continue;
				}
				System.out.println("sicstus worker: invoked sicstus worker thread");
				boolean result = false;
				try{
				result = runProver(input.kFormulas, input.formulaToProve, input.chooseInferenceSystem);
				} catch(Exception e) {
					try {
						e.printStackTrace();
						outputQueue.put(e);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						continue;
					}
					
				}
			System.out.println("sicstus worker: prover result " + result);
			try {
				outputQueue.put(result);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				continue;
			}
		}
	}
	
	/**
	 * Read the knowledgebase and the formula to prove and give it the prolog
	 * solver to prove.
	 * IMPORTANT: All atoms have to start with a lower case letter. Otherwise
	 * the prolog engine will use up all available memory and die with a
	 * resource_error.
	 * 
	 * @param kFormulas
	 *            the knowledgebase
	 * @param formulaToProve
	 *            the formula to prove
	 * @param chooseInferenceSystem
	 *            CUMMULATIV - Cumulative logic, 
	 *            LOOP_CUMMULATIV - Loop-Cumulative logic, 
	 *            PREFERENTIAL - Preferential logic, 
	 *            RATIONAL - Rational logic, 
	 *            FREE_RATIONAL - Rational logic with free variables
	 * @return true if formulaToProve can be inferred from kFormulas, false otherwise
	 */
	public boolean prove(List<String> kFormulas, String formulaToProve,
			InferenceSystem chooseInferenceSystem) throws SICStusException {
		
		ProverInput input = new ProverInput(kFormulas, formulaToProve, chooseInferenceSystem);
		
		
		try {
			inputQueue.put(input);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			throw new SICStusException();
		}
		
		try {
			System.out.println("waiting for sicstus result");
			Object result = outputQueue.take();
			if(result instanceof Boolean) {
				return (boolean) result;
			} else if(result instanceof Exception) {
				throw new SICStusException(((Exception)result).getMessage());
			} else throw new SICStusException("Illegal Value returned by klmlean solver.");
		} catch (InterruptedException e) {
			throw new SICStusException();
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
	 * @param chooseInferenceSystem
	 *            CUMMULATIV - Cumulative logic, 
	 *            LOOP_CUMMULATIV - Loop-Cumulative logic, 
	 *            PREFERENTIAL - Preferential logic, 
	 *            RATIONAL - Rational logic, 
	 *            FREE_RATIONAL - Rational logic with free variables
	 * @return true if formulaToProve can be inferred from kFormulas, false otherwise
	 * @throws Exception 
	 */
	public static boolean runProver(List<String> kFormulas, String formulaToProve,
			InferenceSystem chooseInferenceSystem) throws Exception {
		
		Query q;
		HashMap<Object, Object> map = new HashMap<Object, Object>();

		/* Initialize the SICStus Prolog engine */
		/* Parameter 1 determines the KLM logic to consider */
		String filename =null;
			switch (chooseInferenceSystem) {
			case CUMMULATIV: {
				filename = "tct.sav";
				break;
			}
			case LOOP_CUMMULATIV: {
				filename = "tclt.sav";
				break;
			}
			case PREFERENTIAL: {
				filename = "tpt.sav";
				break;
			}
			case RATIONAL: {
				filename = "trt.sav";
				break;
			}
			case FREE_RATIONAL: {
				filename = "trtfree.sav";
				break;
			}
			}
			String path = "resources/" + filename;
			try{
				sp.restore(path);
			}catch(SPException e) {
				System.err.println("\nERROR Prolog engineon restore file: " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Error with SICStus: Error on restore file: " + e.toString(), "SICStus error", JOptionPane.ERROR_MESSAGE);
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
			for (String currentFormula : kFormulas) {
				if (currentFormula.length() > 0)
					kBaseList = kBaseList + currentFormula + ",";
			}
			if (kBaseList.charAt(kBaseList.length() - 1) == ',')
				kBaseList = kBaseList.substring(0, kBaseList.length() - 1);
			kBaseList = kBaseList + "]";
			String goal = new String("parseinput(" + kBaseList + ").");
			System.out.println("Prover input: " + goal + ", map: "+ map);
			
			try {
				q = sp.openPrologQuery(goal, map);
			} catch (SPException e) {
				System.err.println("\nERROR Prolog engine on open Query: " + e.getMessage());
				throw e;
			}
			try {
				if (!(q.nextSolution())) {
					System.err
							.println("Error in the knowledge base: you cannot use nested conditionals");
				}
			} catch (NoSuchMethodException e) {
				System.err.println("Error on finding solution: " + e.getMessage());
				throw e;
			} catch (InterruptedException e) {
				System.err.println("Error on finding solution: " + e.getMessage());
				throw e;
			} catch (Exception e) {
				System.err.println("Error on finding solution: " + e.getMessage());
				throw e;
			}

			/* Step 2: formulas to prove base must be in the KLM language */
			String toProveList = new String("[" + formulaToProve + "]");

			goal = new String("parseinput(" + toProveList + ").");
			System.out.println("GOAL: "+goal);
			try {
				q = sp.openPrologQuery(goal, map);
			} catch (SPException e) {
				System.err.println("\nERROR Prolog engine on open Query: " + e.getMessage());
				throw e;
			}
			try {
				if (!(q.nextSolution())) {
					System.err
							.println("Error in the formula to prove: you cannot use nested conditionals");
				}
			} catch (NoSuchMethodException e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			} catch (InterruptedException e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			} catch (Exception e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			}
			/*
			 * Step 3: finding a derivation of the formula from the knowledge
			 * base by using the calculi for KLM logics
			 */

			goal = new String("unsatinterface(" + kBaseList + "," + toProveList
					+ ",Tree).");
			System.out.println("kbaselist:" + kBaseList);
			try {
				q = sp.openPrologQuery(goal, map);
			} catch (SPException e) {
				System.err.println("\nERROR Prolog engine on open Query: " + e.getMessage());
				throw e;
			}
			try {
				if (q.nextSolution()) {
					q.close();
					return true;

				} else {
					q.close();
					return false;
				}
			} catch (NoSuchMethodException e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			} catch (InterruptedException e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			} catch (Exception e) {
				System.err.println("Error on finding solution: " + e.toString() + " " + e.getMessage());
				throw e;
			}
	}
}