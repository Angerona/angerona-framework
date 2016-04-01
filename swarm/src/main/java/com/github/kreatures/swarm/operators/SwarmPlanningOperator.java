package com.github.kreatures.swarm.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * List of Default Desires
 */
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_MOVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.IS_VISIT;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_LEAVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_VISIT;
import static com.github.kreatures.swarm.basic.SwarmDesires.isEqual;
/**
 * List of Default Actions
 */
import static com.github.kreatures.swarm.components.SwarmActionRef.ENTER_STATION;
import static com.github.kreatures.swarm.components.SwarmActionRef.WAIT;
import static com.github.kreatures.swarm.components.SwarmActionRef.DO_WORK;
import static com.github.kreatures.swarm.components.SwarmActionRef.LEAVE_STATION;
import static com.github.kreatures.swarm.components.SwarmActionRef.ON_WAY;

//import com.github.kreatures.swarm.basic.SwarmDesires; 
import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Desire;
import com.github.kreatures.core.PlanComponent;
import com.github.kreatures.core.PlanElement;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.simple.operators.PlanningOperator;
import com.github.kreatures.swarm.basic.SwarmAction;
import com.github.kreatures.swarm.components.DefaultStation;
import com.github.kreatures.swarm.components.StatusAgentComponents;
import com.github.kreatures.swarm.components.SwarmMappingGeneric;
//import com.github.kreatures.swarm.components.SwarmMappingGeneric;

public class SwarmPlanningOperator extends PlanningOperator{

	@Override
	protected Boolean processImpl(PlanParameter preprocessedParameters) {
		StatusAgentComponents statusAgent = preprocessedParameters.getAgent().getComponent(StatusAgentComponents.class);
		//SwarmMappingGeneric swarmMappingGeneric = preprocessedParameters.getAgent().getComponent(SwarmMappingGeneric.class);
		PlanComponent plans = preprocessedParameters.getActualPlan();

		//if(statusAgent!=null && swarmMappingGeneric!=null && !plans.getPlans().isEmpty() ){
		if(statusAgent!=null && !plans.getPlans().isEmpty() ){
			Desire desire = plans.getPlans().get(0).getFulfillsDesire();
			plans.clear();

			List<Action> sequence = getSequenceActionOfDesire(desire, preprocessedParameters.getAgent(), statusAgent);
//			preprocessedParameters.report(desire.toString());
			if (sequence != null) {
				Subgoal plan = new Subgoal(preprocessedParameters.getAgent(), desire);

				Stack<PlanElement> temp = new Stack<>();
				for (Action a : sequence) {
					temp.push(new PlanElement(a));
				}
				plan.newStack();
				plan.setStack(0, temp);
				plans.addPlan(plan);
			}
			return true;
		}
		return false;
	}
	/**
	 *  
	 * @param desire this is what a agent want to do next
	 * @param agent the agent, that is running.
	 * @param statusAgent this gives information about the status of the running agent.
	 * @return List<Action> a sequence of action ,which will be execute by agent in the given oder.
	 */
	private List<Action> getSequenceActionOfDesire(Desire desire, Agent agent, StatusAgentComponents statusAgent){

		if (isEqual(desire, WANT_TO_LEAVE)) {
			return leaveStationTarget(new LinkedList<Action>(),agent,statusAgent);
		} else if (isEqual(desire, WANT_TO_VISIT)) {
			return VisitStationTarget(new LinkedList<Action>(),agent,statusAgent);
		} else if (isEqual(desire, WANT_TO_MOVE)) {
			return onWaytoStationTarget(new LinkedList<Action>(),agent,statusAgent);
		} else if (isEqual(desire, IS_VISIT)) {
			return doWorkonStationCurrent(new LinkedList<Action>(),agent,statusAgent);
		}
		return null;
	}
	/**
	 * This is use when the desire of a agent is: It want to leave a target station.  
	 * @param desire this is what a agent want to do next
	 * @param agent the agent, that is running.
	 * @param statusAgent this gives information about the status of the running agent.
	 * @return List<Action> a sequence of action ,which will be execute by agent in the given oder.
	 */
	private List<Action> leaveStationTarget(List<Action> sequenceAction,Agent agent, StatusAgentComponents statusAgent){
		if(statusAgent.getTarget()!=null){
			if (statusAgent.isLeave()){
				sequenceAction.add(new SwarmAction(agent, LEAVE_STATION) );
				return sequenceAction;
			}
		}
		return null;
	}
	/**
	 * This is use when the desire of a agent: It want to visit a target station.  
	 * @param desire this is what a agent want to do next
	 * @param agent the agent, that is running.
	 * @param statusAgent this gives information about the status of the running agent.
	 * @return List<Action> a sequence of action ,which will be execute by agent in the given oder.
	 */
	private List<Action> VisitStationTarget(List<Action> sequenceAction,Agent agent, StatusAgentComponents statusAgent){
		if(statusAgent.getTarget()!=null){
			if (statusAgent.isEnterStation()){
				DefaultStation currentStation=agent.getComponent(SwarmMappingGeneric.class).getStationIntance(statusAgent.getTarget());
				if(currentStation.isSpace()){
					sequenceAction.add(new SwarmAction(agent, ENTER_STATION) );
				}else{
					sequenceAction.add(new SwarmAction(agent, WAIT) );
				}
				return sequenceAction;
			}
		}
		return null;
	}
	/**
	 * This is use when the desire of a agent is: It is on way to target station.  
	 * @param desire this is what a agent want to do next
	 * @param agent the agent, that is running.
	 * @param statusAgent this gives information about the status of the running agent.
	 * @return List<Action> a sequence of action ,which will be execute by agent in the given oder.
	 */
	private List<Action> onWaytoStationTarget(List<Action> sequenceAction,Agent agent, StatusAgentComponents statusAgent){
		
		if(statusAgent.getTarget()!=null){
			if(statusAgent.isOnWay()){
				if( statusAgent.getDistance()>0){
					sequenceAction.add(new SwarmAction(agent, ON_WAY) );
					//statusAgent.restWay();
					//return onWaytoStationTarget(sequenceAction,agent,statusAgent);	
				//}else{
					return sequenceAction;
				}
			}
		}
		return null;
	}
	/**
	 * This is use when the desire of a agent is: It doing its job.  
	 * @param desire this is what a agent want to do next
	 * @param agent the agent, that is running.
	 * @param statusAgent this gives information about the status of the running agent.
	 * @return List<Action> a sequence of action ,which will be execute by agent in the given oder.
	 */
	private List<Action> doWorkonStationCurrent(List<Action> sequenceAction,Agent agent, StatusAgentComponents statusAgent){
		if(statusAgent.getTarget()!=null){
			if( statusAgent.isVisit()){
				sequenceAction.add(new SwarmAction(agent, DO_WORK) );
				return sequenceAction;
			}
		}
		return null;
	}
}