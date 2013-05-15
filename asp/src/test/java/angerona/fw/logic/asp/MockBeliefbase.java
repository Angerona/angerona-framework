package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import angerona.fw.OperatorProvider;
import angerona.fw.OperatorSet;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.OperatorCallWrapper;
import angerona.fw.util.OSValidator;

/**
 * Initializes an ASP-Beliefbase for unit tests.
 * @author Tim Janus
 */
public class MockBeliefbase extends AspBeliefbase {
	private class SpecialOperatorSet extends OperatorProvider {
		public SpecialOperatorSet() {
			OperatorSet rSet = new OperatorSet(BaseReasoner.OPERATION_TYPE);
			AspReasoner arOp = new AspReasoner();
			rSet.addOperator(new OperatorCallWrapper(arOp));
			rSet.setPrefered(AspReasoner.class.getName());			
			this.addOperationSet(rSet);
			
			OperatorSet tSet = new OperatorSet(BaseTranslator.OPERATION_TYPE);
			AspTranslator tOp = new AspTranslator();
			tSet.addOperator(new OperatorCallWrapper(tOp));
			tSet.setPrefered(tOp.getClass().getName());
			this.addOperationSet(tSet);
			
			OperatorSet cSet = new OperatorSet(BaseChangeBeliefs.OPERATION_TYPE);
			AspExpansion expan = new AspExpansion();
			cSet.addOperator(new OperatorCallWrapper(expan));
			cSet.setPrefered(expan.getClass().getName());
			this.addOperationSet(cSet);
		}
	}
	
	public MockBeliefbase() {
		
		this.operators = new SpecialOperatorSet();
		
		String os = "";
		String ending = "";
		if(OSValidator.isWindows()) {
			os = "win";
			ending = ".exe";
		} else if(OSValidator.isUnix()) {
			os = "unix";
			ending = "";
		} else if(OSValidator.isMac()) {
			os = "mac";
			ending = ".bin";
		}
		
		final String path = System.getProperty("user.dir") +
				"/../app/src/main/tools/" + os + "/solver/asp/dlv/dlv" + ending;
		
		((AspReasoner)getReasoningOperator().getImplementation()).setSolverWrapper(new ISolverWrapper() {
			@Override
			public Solver getSolver() {
				
				return new DLV(path);
			}
		});
		//getReasoningOperator().infer(this);
	}
}
