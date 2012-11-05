package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.solver.DLV;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import angerona.fw.Agent;
import angerona.fw.util.OSValidator;

/**
 * Initializes an ASP-Beliefbase for unit tests.
 * @author Tim Janus
 */
public class MockBeliefbase extends AspBeliefbase {
	public MockBeliefbase() {
		Agent BobTest = new Agent("BobTest");
		getChangeOperators().setDefault(new AspExpansion());
		getReasoningOperators().setDefault(new AspReasoner());
		getTranslators().setDefault(new AspTranslator());
		
		getChangeOperators().setOwner(BobTest);
		getReasoningOperators().setOwner(BobTest);
		getTranslators().setOwner(BobTest);
		
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
}
