package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logicprogramming.asplibrary.solver.Clingo;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
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
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AngeronaDetailAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * Implementation of an ASP Reasoner using dlv or clingo as solver backends.
 * It supports multiple semantics and has a three valued answer behavior:
 * true, false, unknown
 * 
 * @author Tim Janus
 */
public class AspReasoner extends BaseReasoner {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspReasoner.class);
	
	/** enum encoding the possible semantics to interpret the answersets */
	private enum InferenceSemantic {
		S_SKEPTICAL,
		S_CREDULOUS
	}
	
	/** enum encoding the possible solver backends which can be use to retrieve the answer sets*/
	private enum SolverType {
		CLINGO,
		DLV,
		DLV_COMPLEX
	}
	
	/** the solver type used by this class instance */
	private SolverType solver = SolverType.DLV_COMPLEX;
	
	/** the actual used semantic for interpreting the received answer sets */
	private InferenceSemantic semantic = InferenceSemantic.S_SKEPTICAL;
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	/**
	 * Process the answer sets for the beliefbase owning the operator.
	 * @return a list of answer sets representing the solver output.
	 */
	public List<AnswerSet> processAnswerSets() {
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		if(bb == null)
			return null;
		
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
		List<AnswerSet> answerSets = processAnswerSets();
		AnswerValue av = AnswerValue.AV_UNKNOWN;
		
		String dParam = null; //Can I access the dParam from the reasoner? I only know how to from the secret. Or is it specified elsewhere?
		
		if(dParam != null)
		{
			double d = Double.parseDouble(dParam);
			av = dInference(answerSets, query, d);
		}
		else
		{
			if(semantic == InferenceSemantic.S_CREDULOUS) {
				av = credulousInference(answerSets, query);
			} else if(semantic == InferenceSemantic.S_SKEPTICAL) {
				av = skepticalInference(answerSets, query);
			}
		}
		
		AspBeliefbase bb = (AspBeliefbase)this.actualBeliefbase;
		return new AngeronaAnswer(bb, query, av);
	}

	/**
	 *  Generalized inference for any "d" parameter.
	 */
	public AnswerValue dInference(List <AnswerSet> answerSets, FolFormula query, double d)
	{
		AnswerValue av;
		int falseInAS = 0;
		int trueInAS = 0;
		
		boolean negate = false;
		Atom aq = null;
		if(query instanceof Negation) {
			negate = true;
			Negation n = (Negation)query;
			aq = (Atom)n.getFormula();
		} else if(query instanceof Atom ){
			negate = false;
			aq = (Atom)query;
		} else {
			//throw new AngeronaException("The query formula for the ASP Reasoner must be an atom or a negation:");
			LOG.error("The given query: '{}' is neither a atom nor a negation, so it cannot be process by the ASPReasoner.", query);
			return AnswerValue.AV_REJECT;
		}
		
		for(AnswerSet as : answerSets) {
			av = AnswerValue.AV_UNKNOWN;
			Set<Literal> literals = as.getLiteralsBySymbol(aq.getPredicate().getName());
			for(Literal l : literals) {
				if(l.isTrueNegated()) {
					av = negate ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
				} else {
					av = negate ? AnswerValue.AV_FALSE : AnswerValue.AV_TRUE;
				}
			}
			
			if(av == AnswerValue.AV_TRUE) {
				trueInAS += 1;
			} else if(av == AnswerValue.AV_FALSE) {
				falseInAS += 1;
			}
		}
		
		double trueQuotient = ((double) (trueInAS))/answerSets.size(); //Check the loss-of-precision of this operation
		double falseQuotient = ((double) (falseInAS))/answerSets.size();
		
		//Which should it choose if both meet the "d" criteria?
		//Further parameters may be needed for the operator
		//The behavior here would then be specific to one set of those operators
		if(trueQuotient >= d && falseQuotient >= d)
		{
			if(trueQuotient > falseQuotient)
				av = AnswerValue.AV_TRUE;
			else if (falseQuotient > trueQuotient)
				av = AnswerValue.AV_FALSE;
			else
				av = AnswerValue.AV_UNKNOWN;
		}
		else if(trueQuotient >= d)
			av = AnswerValue.AV_TRUE;
		else if(falseQuotient >= d)
			av = AnswerValue.AV_FALSE;
		else 
			av = AnswerValue.AV_UNKNOWN;
		return av;
	}
	
	/**
	 * Implements the credulous asp inference. 
	 * @param answerSets	List of answer sets representing the solver result.
	 * @param query			The question must be an atom or an Negation but no complexer formulas
	 * @return				AV_True, AV_FALSE or AV_UNKNOWN if the inference was successful,
	 * 						AV_REJECT if an error occured.
	 */
	public AnswerValue credulousInference(List <AnswerSet> answerSets, FolFormula query)
	{
		AnswerValue av;
		int falseInAS = 0;
		int trueInAS = 0;
		
		boolean negate = false;
		Atom aq = null;
		if(query instanceof Negation) {
			negate = true;
			Negation n = (Negation)query;
			aq = (Atom)n.getFormula();
		} else if(query instanceof Atom ){
			negate = false;
			aq = (Atom)query;
		} else {
			//throw new AngeronaException("The query formula for the ASP Reasoner must be an atom or a negation:");
			LOG.error("The given query: '{}' is neither a atom nor a negation, so it cannot be process by the ASPReasoner.", query);
			return AnswerValue.AV_REJECT;
		}
		
		for(AnswerSet as : answerSets) {
			av = AnswerValue.AV_UNKNOWN;
			Set<Literal> literals = as.getLiteralsBySymbol(aq.getPredicate().getName());
			for(Literal l : literals) {
				if(l.isTrueNegated()) {
					av = negate ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
				} else {
					av = negate ? AnswerValue.AV_FALSE : AnswerValue.AV_TRUE;
				}
			}
			
			if(av == AnswerValue.AV_TRUE) {
				trueInAS += 1;
			} else if(av == AnswerValue.AV_FALSE) {
				falseInAS += 1;
			}
		}
		//Should it give preference to "true" values, or "false" values, if both meet the "d" criteria?
		if(trueInAS > 0 && falseInAS == 0)
			av = AnswerValue.AV_TRUE;
		else if(falseInAS > 0 && trueInAS == 0)
			av = AnswerValue.AV_FALSE;
		else 
			av = AnswerValue.AV_UNKNOWN;
		return av;
	}
	
	/**
	 * Implements the skeptical asp inference. 
	 * @param answerSets	List of answer sets representing the solver result.
	 * @param query			The question must be an atom or an Negation but no complexer formulas
	 * @return				AV_True, AV_FALSE or AV_UNKNOWN if the inference was successful,
	 * 						AV_REJECT if an error occured.
	 */
	public AnswerValue skepticalInference(List<AnswerSet> answerSets, FolFormula query) {
		AnswerValue av;
		int falseInAS = 0;
		int trueInAS = 0;
		
		boolean negate = false;
		Atom aq = null;
		if(query instanceof Negation) {
			negate = true;
			Negation n = (Negation)query;
			aq = (Atom)n.getFormula();
		} else if(query instanceof Atom ){
			negate = false;
			aq = (Atom)query;
		} else {
			//throw new AngeronaException("The query formula for the ASP Reasoner must be an atom or a negation:");
			LOG.error("The given query: '{}' is neither a atom nor a negation, so it cannot be process by the ASPReasoner.", query);
			return AnswerValue.AV_REJECT;
		}
		
		for(AnswerSet as : answerSets) {
			av = AnswerValue.AV_UNKNOWN;
			Set<Literal> literals = as.getLiteralsBySymbol(aq.getPredicate().getName());
			for(Literal l : literals) {
				if(l.isTrueNegated()) {
					av = negate ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
				} else {
					av = negate ? AnswerValue.AV_FALSE : AnswerValue.AV_TRUE;
				}
			}
			
			if(av == AnswerValue.AV_TRUE) {
				trueInAS += 1;
			} else if(av == AnswerValue.AV_FALSE) {
				falseInAS += 1;
			}
		}
		
		if(trueInAS == answerSets.size())
			av = AnswerValue.AV_TRUE;
		else if(falseInAS == answerSets.size())
			av = AnswerValue.AV_FALSE;
		else 
			av = AnswerValue.AV_UNKNOWN;
		return av;
	}
	
	
	/**
	 * Helper method: Decides which solver to use when running an inference.
	 * @param bb	the solver will be applied on this beliefbase.
	 * @return		A list of answersets 
	 * @throws SolverException
	 */
	private List<AnswerSet> runSolver(AspBeliefbase bb) throws SolverException {
		Solver solver = null;
		// TODO: Find better solution replace duplo (REVISION)
		String postfix = "";
		postfix += System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? ".exe" : "";
		postfix += System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0 ? ".bin" : "";
		
		if(this.solver == SolverType.CLINGO)
			solver = new Clingo("tools/solver/asp/clingo/clingo");
		else if(this.solver == SolverType.DLV)
			solver = new DLV("tools/solver/asp/dlv/dlv"+postfix);
		else if(this.solver == SolverType.DLV_COMPLEX)
			solver = new DLVComplex("./tools/solver/asp/dlv/dlv-complex"+postfix);
		return solver.computeModels(bb.getProgram(), 10);
	}

	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}

	@Override
	//Assumes a skeptical inference operator at the moment, I think
	protected Set<FolFormula> inferInt() {
		List<AnswerSet> answerSets = processAnswerSets();
		List<Set<FolFormula>> answerSetsTrans = new LinkedList<Set<FolFormula>>();
		Set<FolFormula> reval = new HashSet<FolFormula>();
		for(AnswerSet as : answerSets) {
			Set<FolFormula> temp = new HashSet<FolFormula>();
			for(String name : as.literals.keySet()) {
				Set<Literal> literals = as.literals.get(name);
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
		
		if(semantic == InferenceSemantic.S_CREDULOUS)
			throw new NotImplementedException();
		else if(semantic == InferenceSemantic.S_SKEPTICAL) {
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
		}
		
		return reval;
	}
	@Override
	public Set<AngeronaDetailAnswer> queryForAllAnswers(FolFormula query) {
		// TODO Auto-generated method stub
		return null;
	}


}
