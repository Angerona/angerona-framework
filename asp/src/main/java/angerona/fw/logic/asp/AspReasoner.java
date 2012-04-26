package angerona.fw.logic.asp;

import java.util.List;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.Formula;
import net.sf.tweety.logicprogramming.asplibrary.solver.Clingo;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseBeliefbase;
import angerona.fw.logic.BaseReasoner;

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

	@Override
	public Answer query(Formula query) {		
		boolean negate = false;
		Atom aq = null;
		if(query instanceof Negation) {
			negate = true;
			Negation n = (Negation)query;
			aq = (Atom)n.getFormula();
		} else {
			negate = false;
			aq = (Atom)query;
		}
		AspBeliefbase bb = (AspBeliefbase)this.beliefBase;
		
		List<AnswerSet> reval = null;
		try {
			reval = runSolver(bb);
		} catch(SolverException ex) {
			LOG.error("Error occured: " + ex.getMessage());
			return null;
		}
		
		AnswerValue av = AnswerValue.AV_UNKNOWN;
		int falseInAS = 0;
		int trueInAS = 0;
		if(semantic == InferenceSemantic.S_CREDULOUS) {
			throw new NotImplementedException();
		} else if(semantic == InferenceSemantic.S_SKEPTICAL) {
			
			for(AnswerSet as : reval) {
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
		}
		
		LOG.info(reval.toString());

		if(trueInAS > 0 && falseInAS == 0)
			av = AnswerValue.AV_TRUE;
		else if(falseInAS > 0 && trueInAS == 0)
			av = AnswerValue.AV_FALSE;
		else 
			av = AnswerValue.AV_UNKNOWN;
		return new AngeronaAnswer(bb, query, av);
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

}
