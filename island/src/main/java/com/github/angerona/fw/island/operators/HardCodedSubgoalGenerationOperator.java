package com.github.angerona.fw.island.operators;

import static com.github.angerona.fw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.island.data.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.island.data.IslandDesires.SECURE_SITE;
import static com.github.angerona.fw.island.data.IslandDesires.isEqual;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.data.IslandAction;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class HardCodedSubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	@Override
	protected Boolean processImpl(PlanParameter param) {
		Area area = param.getAgent().getComponent(Area.class);
		PlanComponent plans = param.getActualPlan();

		if (area != null && !plans.getPlans().isEmpty()) {
			Desire desire = plans.getPlans().get(0).getFulfillsDesire();

			for (Subgoal plan : plans.getPlans()) {
				plans.removePlan(plan);
			}

			List<IslandAction> sequence = calcMinSequence(desire, area);

			if (sequence != null) {
				Subgoal plan = new Subgoal(param.getAgent(), desire);
				Stack<PlanElement> temp = new Stack<>();

				for (IslandAction a : new Revit<IslandAction>(sequence)) {
					temp.push(new PlanElement(a));
				}

				plan.setStack(0, temp);
				plans.addPlan(plan);
			}

			return true;
		}

		return false;
	}

	public static List<IslandAction> calcMinSequence(Desire desire, Area area) {
		if (isEqual(desire, FINISH_WORK)) {
			return calcFinishWork(area);
		} else if (isEqual(desire, FILL_BATTERY)) {
			return calcFillBattery(area);
		} else if (isEqual(desire, FIND_SHELTER)) {
			return calcFindShelter(area);
		} else if (isEqual(desire, SECURE_SITE)) {
			return calcSecureSite(area);
		}

		return null;
	}

	protected static List<IslandAction> calcFinishWork(Area a) {
		// TODO Auto-generated method stub
		return null;
	}

	protected static List<IslandAction> calcFillBattery(Area a) {
		// TODO Auto-generated method stub
		return null;
	}

	protected static List<IslandAction> calcFindShelter(Area a) {
		// TODO Auto-generated method stub
		return null;
	}

	protected static List<IslandAction> calcSecureSite(Area a) {
		// TODO Auto-generated method stub
		return null;
	}

	protected class Revit<T> implements Iterator<T>, Iterable<T> {

		private List<T> core;
		private int pos;

		public Revit(List<T> core) {
			if (core == null) {
				throw new NullPointerException("core must not be null");
			}

			this.core = core;
			this.pos = core.size() - 1;
		}

		@Override
		public Iterator<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return pos >= 0;
		}

		@Override
		public T next() {
			return core.get(pos--);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}

	}

}
