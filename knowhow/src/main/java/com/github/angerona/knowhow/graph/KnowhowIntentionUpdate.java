package com.github.angerona.knowhow.graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.logics.commons.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.example.operators.GenerateOptionsOperator;
import com.github.angerona.fw.example.operators.IntentionUpdateOperator;
import com.github.angerona.fw.example.operators.SubgoalGenerationOperator;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.operators.OperatorCallWrapper;
import com.github.angerona.knowhow.graph.parameter.DefaultPlanConverter;
import com.github.angerona.knowhow.situation.SituationBuilderAdapter;

/**
 * An IntentionUpdate operator implementation that does the same as
 * {@link IntentionUpdateOperator} but handles the {@link ActionAdapter}
 * actions correctly.
 * 
 * @author Tim Janus
 */
public class KnowhowIntentionUpdate extends IntentionUpdateOperator {
	
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(SituationBuilderAdapter.class);
	
	
	@Override
	protected PlanElement processInternal(PlanParameter param) {
		PlanElement reval = null;
		
		// check for desires that have no plan yet:
		boolean subgoalGenNeeded = false;
		for(Desire des : param.getAgent().getComponent(Desires.class).getDesires()) {
			if(param.getActualPlan().countPlansFor(des) == 0) {
				subgoalGenNeeded = true;
			}
		}
		
		if(subgoalGenNeeded) {
			OperatorCallWrapper ocw = param.getAgent().getOperators().getOperationSetByType(SubgoalGenerationOperator.OPERATION_NAME).getPreferred();
			ocw.process(param);
		}
		
		// sort plans such that long term plans are paused to
		// answer queries and later be resumed.
		List<Subgoal> orderedPlans = new ArrayList<>(param.getActualPlan().getPlans());
		Collections.sort(orderedPlans, new Comparator<Subgoal>() {
			@Override
			public int compare(Subgoal sg1, Subgoal sg2) {
				Predicate prio = GenerateOptionsOperator.prepareQueryProcessing;
				Predicate des1 = (Predicate)sg1.getFulfillsDesire().getFormula().getPredicates().iterator().next();
				Predicate des2 = (Predicate)sg2.getFulfillsDesire().getFormula().getPredicates().iterator().next();
				
				if(des1.equals(prio) && !des2.equals(prio))
					return 1;
				else if(!des1.equals(prio) && des2.equals(prio))
					return -1;
				else
					return 0;
			}
		});
		
		// find next plan element
		for(Subgoal plan : orderedPlans) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				PlanElement pe = plan.peekStack(i);
				if(check(param, pe)) {
					reval = pe;
					break;
				}
			}
		}
		
		// evaluate action parameters:
		if(reval != null) {
			// first check if resume planning is needed:
			if(reval.getIntention() instanceof ActionAdapterResume) {
				ActionAdapterResume aar = (ActionAdapterResume)reval.getIntention();
				KnowhowGraphSubgoal.planningStrategy.resumePlan(aar.getWorkingPlan());
				
				// fill the new planned steps in the agent's plan component:
				Subgoal planSequence = (Subgoal)aar.getParent();
				for(PlanElement pe : planSequence.getStack(0)) {
					if( pe.getIntention() instanceof ActionAdapterResume) {
						ActionAdapterResume cur = (ActionAdapterResume)pe.getIntention();
						GraphIntention atomicGraphIntention = cur.getParentIntention().getSubIntentions().get(cur.getIndexInIntention());
						DefaultPlanConverter converter = new DefaultPlanConverter();
						converter.init(param.getAgent());
						List<Intention> translatedAction = converter.convert(aar.getWorkingPlan(), atomicGraphIntention);
						if(translatedAction.size() != 1) {
							throw new IllegalStateException();
						}
						Intention parent = pe.getIntention().getParent();
						pe.setIntention(translatedAction.get(0));
						pe.getIntention().setParent(parent);
					} 
				}
				param.getAgent().getPlanComponent().report("Resumed and extended Plan");
			}
			
			// then check if the current intention contains an ActionAdapter and evaluate it.
			// this order is important because the ActionAdapterReumse becomes an ActionAdapter when 
			// resume planning.
			if(reval.getIntention() instanceof ActionAdapter) {
				ActionAdapter aa = (ActionAdapter)reval.getIntention();
				Action realAction = aa.evaluateAction();
				if(realAction != null) {
					reval.setIntention(realAction);
				} else {
					return null;
				}
			} 
		}
		
		return reval;
	}
}
