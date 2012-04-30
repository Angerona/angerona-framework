package angerona.fw;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ch.qos.logback.core.db.dialect.MsSQLDialect;

import net.sf.tweety.Formula;
import angerona.fw.internal.EntityAtomic;
import angerona.fw.internal.IdGenerator;

public class MasterPlan extends Subgoal implements EntityAtomic {

	private Long id;
	
	private Long parent;
	
	public MasterPlan(Agent agent) {
		super(agent);
		id = IdGenerator.generate(this);
		parent = agent.getGUID();
	}
	
	public MasterPlan(MasterPlan plan) {
		super(plan);
		id = plan.getGUID();
		parent = plan.getParent();
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {
		super.onSubgoalFinished(subgoal);

		Angerona.getInstance().report("Step on plan executed, plan updated.", getAgent().getEnvironment(), (MasterPlan)this);
	}
	
	@Override
	public Long getGUID() {
		return id;
	}

	@Override
	public Long getParent() {
		return parent;
	}

	@Override
	public List<Long> getChilds() {
		return new LinkedList<Long>();
	}

	@Override
	public Object clone() {
		return new MasterPlan(this);
	}
}
