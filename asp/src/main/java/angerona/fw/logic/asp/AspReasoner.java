package angerona.fw.logic.asp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNeg;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.StringTerm;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Angerona;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;
import angerona.fw.serialize.GlobalConfiguration;
import angerona.fw.util.Pair;

/**
 * Implementation of an ASP Reasoner using dlv or clingo as solver backends.
 * It supports multiple semantics and has a three valued answer behavior:
 * true, false, unknown
 * 
 * @author Tim Janus
 * @author Daniel Dilger
 */
public class AspReasoner extends BaseReasoner {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspReasoner.class);
	
	/** the solver type used by this class instance */
	private ISolverWrapper solver;
	
	public AspReasoner() {
		GlobalConfiguration config = Angerona.getInstance().getConfig();
		if(config != null) {
			String solverStr = config.getParameters().get("asp-solver");
			if(solverStr != null)
				this.solver = SolverWrapper.valueOf(solverStr);
			else
				this.solver = SolverWrapper.DLV;
		}
	}
	
	public void setSolverWrapper(ISolverWrapper wrapper) {
		solver = wrapper;
	}
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	/**
	 * Process the answer sets for the belief base owning the operator.
	 * @param bb	The AspBeliefbase used to process the answer sets.
	 * @return a list of answer sets representing the solver output.
	 */
	public List<AnswerSet> processAnswerSets(AspBeliefbase bb) {
		List<AnswerSet> reval = null;
		
		try {
			reval = runSolver(bb);
			LOG.info(reval.toString());
		} catch(SolverException ex) {
			LOG.error("Error occured: " + ex.getMessage());
		}
		
		return reval;
	}
	
	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryInt(ReasonerParameter params) {		
		Set<FolFormula> answers = inferInt(params);
		AnswerValue av = AnswerValue.AV_UNKNOWN;

		FolFormula query = params.getQuery();
		if(query.isGround()) {
			// Check if the inferred answer-set contains the query or its
			// negation and adapt the AnswerValue:
			if(answers.contains(query)) {
				av = AnswerValue.AV_TRUE;
			} else if (answers.contains(new Negation(query))) {
				av = AnswerValue.AV_FALSE;
			}
			return new Pair<>(answers, new AngeronaAnswer(query, av));
		} else {
			// Find all answers with the same predicate like the query.
			Predicate queryPred = null;
			if(query instanceof FOLAtom) {
				queryPred = ((FOLAtom)query).getPredicate();
			} else if(query instanceof Negation) {
				if(((Negation)query).getFormula() instanceof FOLAtom) {
					queryPred = ((FOLAtom)((Negation)query).getFormula()).getPredicate();
				}
			}
			Set<FolFormula> realAnswers = new HashSet<>();
			for(FolFormula answer : answers) {
				FOLAtom a = null;
				if(answer instanceof FOLAtom) {
					a = (FOLAtom)answer;
				} else if(answer instanceof Negation) {
					if(((Negation)answer).getFormula() instanceof FOLAtom) {
						a = (FOLAtom)((Negation)answer).getFormula();
					}
				}
				
				if(a == null) {
					LOG.warn("The answer: '{}' is neither an atom nor a negation.", answer);
					continue;
				}
				
				if(a.getPredicate().equals(queryPred)) {
					realAnswers.add(answer);
				}
			}
			return new Pair<>(answers, new AngeronaAnswer(query, realAnswers));
		}
	}
	
	/**
	 * Helper method: Decides which solver to use when running an inference.
	 * @param bb	the solver will be applied on this beliefbase.
	 * @return		A list of answersets 
	 * @throws SolverException
	 */
	private List<AnswerSet> runSolver(AspBeliefbase bb) throws SolverException {
		if(solver == null) {
			LOG.warn("No asp solver linked to AspReasoner operator");
			return new LinkedList<>();
		}
		
		Solver s = solver.getSolver();
		return s.computeModels(bb.getProgram(), 10);
	}

	/** @todo The code in the literal loop is mostly conversion. Logic conversion module? */
	@Override
	protected Set<FolFormula> inferInt(ReasonerParameter params) {
		List<AnswerSet> answerSets = processAnswerSets((AspBeliefbase)params.getBeliefBase());
		List<Set<FolFormula>> answerSetsTrans = new LinkedList<Set<FolFormula>>();
		
		// Translate the elp to fol:
		for(AnswerSet as : answerSets) {
			
			//TODO: The code in this loop is mostly conversion. Logic conversion module?
			Set<FolFormula> temp = new HashSet<>();
			for(DLPLiteral l : as) {
				int arity = l.getAtom().getArity();
				FOLAtom a = new FOLAtom(new Predicate(l.getAtom().getName(), arity));
				for(int i=0; i<arity; ++i) {
					String str = ((StringTerm)l.getAtom().getTerm(i)).get();
					a.addArgument(new Constant(str));
				}
				if(!(l instanceof DLPNeg)) {
					temp.add(a);
				} else {
					temp.add(new Negation(a));
				}
			}
			
			answerSetsTrans.add(temp);
		}
		
		// find the right inference method:
		double dValue = -1;
		String dParam = params.getSetting("d", "1.0");
		if(dParam != null) {
			dValue = Double.parseDouble(dParam);
		}
		
		// select the inference semantic depending on d:
		// call the select method:
		if(dValue == -1) {
			LOG.warn("No Parameter d given: Use skeptical inference");
		}
		if(dValue == 0) {
			return credulousSelection(answerSetsTrans);
		} else if(dValue == 1 || dValue == -1) {
			return skepticalSelection(answerSetsTrans);
		} else {
			return dSelection(answerSetsTrans, dValue);
		}
	}

	/**
	 * Helper method: Performs a d base inference selection on the given answersets.
	 * The parameter d is a factor between 0 and 1 which defines in how many percent
	 * of answer-sets a literal must occur to be inferred.
	 * @param answerSetsTrans	The answersets
	 * @param dValue			The value of the parameter d.
	 * @return					A list of FOL formulas representing the result of the d-
	 * 							inference-selection.
	 * @todo 	Step Three: resolve contradictions,
	 *			requires identifying contradictions first.
	 */
	private Set<FolFormula> dSelection(List<Set<FolFormula>> answerSetsTrans,
			double dValue) {
		Set<FolFormula> reval = new HashSet<FolFormula>();
		HashMap<FolFormula, Integer> frequencies = new HashMap<FolFormula, Integer>();
		
		// Step One: Associate each formula with a frequency
		for(Set<FolFormula> as : answerSetsTrans) {
			for(FolFormula a : as) {
				if(frequencies.containsKey(a)) {
					Integer newFreq = frequencies.get(a) + 1;
					frequencies.put(a, newFreq); //Hopefully these maps can be mutated...
				} else {
					frequencies.put(a, 1);
				}
				reval.add(a);
			}
		}
		
		
		// Step Two: Filter out formulas without proper frequency
		Set<FolFormula> toRemove = new HashSet<>();
		for(FolFormula a : reval) {
			if((frequencies.get(a) / (double) answerSetsTrans.size() ) <= dValue)
			{
				toRemove.add(a);
			}
		}
		reval.removeAll(toRemove);
		
		// Step three, contradiction
		/// 
		
		
		return reval;
	}

	/**
	 * Helper method: Performs a skeptical selection. Only literals are
	 * taken whick are in every answerset
	 * @param answerSetsTrans	The answersets in FOL
	 * @return	A list of FOL formulas representing the result of the septical
	 * 			inference-selection.
	 */
	private Set<FolFormula> skepticalSelection(
			List<Set<FolFormula>> answerSetsTrans) {
		Set<FolFormula> reval = new HashSet<>();
		if(answerSetsTrans.size() > 0) {
			reval.addAll(answerSetsTrans.get(0));
			answerSetsTrans.remove(0); 
		}
		Set<FolFormula> toRemove = new HashSet<FolFormula>();
		for(Set<FolFormula> as : answerSetsTrans) {
			for(FolFormula a : reval) {
				if(!as.contains(a)) {
					toRemove.add(a);
				}
			}
		}
		reval.removeAll(toRemove);
		return reval;
	}

	/**
	 * Helper method: Performs a credulous selecton. Every literal which is in at
	 * least one answer-set will be returned.
	 * BEWARE: This might generate contradictions...
	 * @param answerSetsTrans	The answersets
	 * @return	A list of FOL formulas representing the result of the credulous
	 * 			inference-selection.
	 */
	private Set<FolFormula> credulousSelection(
			List<Set<FolFormula>> answerSetsTrans) {
		Set<FolFormula> reval = new HashSet<>();
		reval.addAll(answerSetsTrans.get(0)); 
		answerSetsTrans.remove(0);
		
		for(Set<FolFormula> as : answerSetsTrans) {
			for(FolFormula a : as) {
				if(!reval.contains(a)) {
					reval.add(a);
				}
			}
		}
		
		return reval;
	}
}
