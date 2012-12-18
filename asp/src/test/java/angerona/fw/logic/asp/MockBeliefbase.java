package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import angerona.fw.BaseOperator;
import angerona.fw.OperationSet;
import angerona.fw.OperatorSet;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.util.OSValidator;

/**
 * Initializes an ASP-Beliefbase for unit tests.
 * @author Tim Janus
 */
public class MockBeliefbase extends AspBeliefbase {
	private class SpecialOperatorSet extends OperatorSet {
		public SpecialOperatorSet() {
			OperationSet rSet = new OperationSet(BaseReasoner.OPERATION_TYPE);
			AspReasoner arOp = new AspReasoner();
			rSet.addOperator(arOp);
			rSet.setPrefered(AspReasoner.class.getName());
			
			operators.put(AspReasoner.class.getName(), arOp);
			operatorsByOperationType.put(BaseReasoner.OPERATION_TYPE, rSet);
			
			OperationSet tSet = new OperationSet(BaseTranslator.OPERATION_TYPE);
			AspTranslator tOp = new AspTranslator();
			tSet.addOperator(tOp);
			tSet.setPrefered(tOp.getClass().getName());
			
			operators.put(tOp.getClass().getName(), tOp);
			operatorsByOperationType.put(BaseTranslator.OPERATION_TYPE, tSet);
			
			OperationSet cSet = new OperationSet(BaseChangeBeliefs.OPERATION_TYPE);
			AspExpansion expan = new AspExpansion();
			cSet.addOperator(expan);
			cSet.setPrefered(expan.getClass().getName());
			
			operators.put(expan.getClass().getName(), expan);
			operatorsByOperationType.put(BaseChangeBeliefs.OPERATION_TYPE, cSet);
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
				"/../test/src/main/tools/" + os + "/solver/asp/dlv/dlv" + ending;
		
		((AspReasoner)getReasoningOperator()).setSolverWrapper(new ISolverWrapper() {
			@Override
			public Solver getSolver() {
				
				return new DLV(path);
			}
		});
		getReasoningOperator().infer(this);
	}
	
	@Override
	public void pushOperator(BaseOperator op) {}
	
	@Override
	public void popOperator() {}
}
