package angerona.fw.logic.asp;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * Implementation of an ASP Reasoner using dlv or clingo as solver backends.
 * It supports multiple semantics and has a three valued answer behavior:
 * true, false, unknown
 * 
 * @author Tim Janus, Daniel Dilger
 */
public class AspReasoner extends BaseReasoner {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspReasoner.class);
	
	/** the solver type used by this class instance */
	private SolverWrapper solver = SolverWrapper.DLV_COMPLEX;
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	/**
	 * Process the answer sets for the beliefbase owning the operator.
	 * @return a list of answer sets representing the solver output.
	 */
	public List<AnswerSet> processAnswerSets(AspBeliefbase bb) {
		List<AnswerSet> reval = null;
		
		try {
			reval = runSolver(bb);
		} catch(SolverException ex) {
			LOG.error("Error occured: " + ex.getMessage());
		}
		
		LOG.info(reval.toString());
		return reval;
	}
	
	@Override
	protected Answer queryInt(FolFormula query) {		
		Set<FolFormula> answers = inferInt();
		AnswerValue av = AnswerValue.AV_UNKNOWN;
				
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		if(query.isGround()) {
			// Check if the inferred answer-set contains the query or its
			// negation and adapt the AnswerValue:
			if(answers.contains(query)) {
				av = AnswerValue.AV_TRUE;
			} else if (answers.contains(new Negation(query))) {
				av = AnswerValue.AV_FALSE;
			}
			return new AngeronaAnswer(bb, query, av);
		} else {
			// Find all answers with the same predicate like the query.
			Predicate queryPred = null;
			if(query instanceof Atom) {
				queryPred = ((Atom)query).getPredicate();
			} else if(query instanceof Negation) {
				if(((Negation)query).getFormula() instanceof Atom) {
					queryPred = ((Atom)((Negation)query).getFormula()).getPredicate();
				}
			}
			Set<FolFormula> realAnswers = new HashSet<>();
			for(FolFormula answer : answers) {
				Atom a = null;
				if(answer instanceof Atom) {
					a = (Atom)answer;
				} else if(answer instanceof Negation) {
					if(((Negation)answer).getFormula() instanceof Atom) {
						a = (Atom)((Negation)answer).getFormula();
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
			return new AngeronaAnswer(bb, query, realAnswers);
		}
	}
	
	/**
	 * Helper method: Decides which solver to use when running an inference.
	 * @param bb	the solver will be applied on this beliefbase.
	 * @return		A list of answersets 
	 * @throws SolverException
	 */
	private List<AnswerSet> runSolver(AspBeliefbase bb) throws SolverException {
		try {
			Solver s = solver.getSolver();
			return s.computeModels(bb.getProgram(), 10);
		} catch (FileNotFoundException fnfe) {
			throw new SolverException(fnfe.getMessage(), SolverException.SE_CANNOT_FIND_SOLVER);
		}
	}

	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}

	@Override
	protected Set<FolFormula> inferInt() {
		List<AnswerSet> answerSets = processAnswerSets((AspBeliefbase)actualBeliefbase);
		List<Set<FolFormula>> answerSetsTrans = new LinkedList<Set<FolFormula>>();
		
		// Translate the elp to fol:
		for(AnswerSet as : answerSets) {
			Set<FolFormula> temp = new HashSet<FolFormula>();
			for(String name : as.literals.keySet()) {
				Set<Literal> literals = as.literals.get(name);
				
				//TODO: The code in this loop is mostly conversion. Logic conversion module?
				for(Literal l : literals) {
					int arity = l.getAtom().getArity();
					Atom a = new Atom(new Predicate(l.getAtom().getName(), arity));
					for(int i=0; i<arity; ++i) {
						a.addArgument(new Constant(l.getAtom().getTerm(i).get()));
					}
					if(!l.isTrueNegated()) {
						temp.add(a);
					} else {
						temp.add(new Negation(a));
					}
				}
			}
			
			answerSetsTrans.add(temp);
		}
		
		// find the right inference method:
		double dValue = -1;
		String dParam = getParameters().get("d");
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
		for(FolFormula a : reval) {
			if((frequencies.get(a) / (double) answerSetsTrans.size() ) < dValue)
			{
				reval.remove(a);
			}
		}
		
		//TODO: Step Three: resolve contradictions
		//Requires identifying contradictions first...
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
		reval.addAll(answerSetsTrans.get(0));
		answerSetsTrans.remove(0); 
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
