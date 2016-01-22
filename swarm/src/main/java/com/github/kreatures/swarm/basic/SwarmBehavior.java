package com.github.kreatures.swarm.basic;

/**
 * List of Default Desires
 */
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_MOVE;

/**
 * List of Default Actions
 */
import static com.github.kreatures.swarm.components.SwarmActionRef.ENTER_STATION;

import com.github.kreatures.core.KReaturesEnvironment;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.PlanComponent;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.simple.behavior.SimpleBehavior;
import com.github.kreatures.swarm.components.DefaultEdge;
import com.github.kreatures.swarm.components.DefaultStation;
import com.github.kreatures.swarm.components.StatusAgentComponents;
import com.github.kreatures.swarm.components.SwarmActionRef;
import com.github.kreatures.swarm.components.SwarmMappingGeneric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;

/**
 * 
 * @author donfack
 *
 */
public class SwarmBehavior extends SimpleBehavior{
	private static final Logger LOG = LoggerFactory.getLogger(SwarmBehavior.class);
	private static SwarmMappingGeneric swarmMapping;
	private SwarmActionRef currentAction;
	//private boolean runOneTime=true;
	
	@Override
	public void sendAction(KReaturesEnvironment env, Action action) {
		
		
		if(action instanceof SwarmAction){
			SwarmAction swarmAction=(SwarmAction) action;
			currentAction=swarmAction.getActionRef();
			
			StatusAgentComponents statusAgent=action.getAgent().getComponent(StatusAgentComponents.class);
			swarmMapping=action.getAgent().getComponent(SwarmMappingGeneric.class);

			if (statusAgent.getTarget()==null){
				LOG.info("Agent has no target's station");
				statusAgent.report(statusAgent.getAgent().getName()+" has no target's station ");
			}
			
			if(statusAgent.getCurrent()==null){
				LOG.info("Agent isn't on the current's station");
				statusAgent.report(statusAgent.getAgent().getName()+" isn't no a current's station ");
			}
			
			DefaultStation targetStation=swarmMapping.getStationIntance(statusAgent.getTarget());
			DefaultStation currentStation=swarmMapping.getStationIntance(statusAgent.getCurrent());
			DefaultEdge edge=swarmMapping.getEdgeIntance(statusAgent.getCurrent(), statusAgent.getTarget());
				
			switch (currentAction){
			case ENTER_STATION:
				if (targetStation==null){
					statusAgent.report("no target's Station where " +statusAgent.getAgent().getName()+" can enter!");
					return;
				}
				
				if(targetStation.isSpace()){
					targetStation.incPlacesBusy();
					statusAgent.setVisited(true);
					statusAgent.setDistance(0);
					statusAgent.setCurrent(statusAgent.getTarget());
					//statusAgent.setTarget(null);
					
				}
				
				break;
			case WAIT:
				if (targetStation==null){
					statusAgent.report("no target's Station where " +statusAgent.getAgent().getName()+" can enter!");
					return;
				}				
				break;
			case LEAVE_STATION:
				if (currentStation==null){
					statusAgent.report("no current's Station where " +statusAgent.getAgent().getName()+" can enter!");
					return;
				}
				if(edge==null){
					statusAgent.report("no edge between " +statusAgent.getCurrent()+" and " +statusAgent.getTarget());
					return;
				}
				currentStation.decPlacesBusy();
				statusAgent.setVisited(false);				
				break;
			case DO_WORK:
				//here is the agent's work.
				doWork(statusAgent);
				if(!findNextStation(statusAgent, currentStation, swarmMapping)){
					LOG.info("there is no target for agent "+statusAgent.getAgent().getName());
				}
				break;
			case ON_WAY:
				statusAgent.move();
				break;
			default:
				LOG.warn("unhandled action-id");
			}
			
		}
		
	}
	/**
	 * a small agent's work.
	 * @param statusAgent agent's component gives a status of a agent.
	 */
	public void doWork(StatusAgentComponents statusAgent){
		statusAgent.doWork();
		
	}
	/**
	 * a function for finding a next station, which a agent has to visit.
	 * @param statusAgent agent's component gives a status of a agent.
	 * @param current a current's station where agent is.
	 * @param swarmMapping map of stations and edges: a platform
	 * @return true when there are a next station.
	 */
	public boolean findNextStation(StatusAgentComponents statusAgent, DefaultStation current, SwarmMappingGeneric swarmMapping){
		for(DefaultEdge edge: current.getListofEdge()){
			if(edge.isUnderected()){
				if(edge.getIncommingStation()==current.getStationId()){
					statusAgent.setTarget(swarmMapping.getStationIntance(edge.getOutcommingStation()).getName());
					statusAgent.setDistance(edge.getEdgeLenght());
					statusAgent.report(statusAgent.getAgent().getName()+ " choises "+ statusAgent.getTarget()+" as next station.");
					return true;
				}
			}else{
				if(edge.getIncommingStation()==current.getStationId()){
					statusAgent.setTarget(swarmMapping.getStationIntance(edge.getOutcommingStation()).getName());
					statusAgent.setDistance(edge.getEdgeLenght());
					statusAgent.report(statusAgent.getAgent().getName()+ " choises "+ statusAgent.getTarget()+" as next station.");
					return true;
				}else if(edge.getOutcommingStation()==current.getStationId()){
					statusAgent.setTarget(swarmMapping.getStationIntance(edge.getIncommingStation()).getName());
					statusAgent.setDistance(edge.getEdgeLenght());
					statusAgent.report(statusAgent.getAgent().getName()+ " choises "+ statusAgent.getTarget()+" as next station.");
					return true;
				}
			}
			
		}
		statusAgent.setTarget(null);
		statusAgent.report(statusAgent.getAgent().getName()+ " cannot find any next station.");
		return false;
	}

	
	@Override
	protected void runEnvironment() {
		// TODO Auto-generated method stub
		LOG.debug("run Environment: {}");		
	}

	@Override
	protected boolean cycleCondition(KReaturesEnvironment env, Agent agent) {
		
		if(tick<2){//&&runOneTime){
			//runOneTime=false;
			//currentAction=ENTER_STATION;
			/*
			//creates all Desires of a agent.
			Desires desires=agent.getComponent(Desires.class);
			Desire desire=new Desire(WANT_TO_VISIT);
			desires.add(desire);
			desire=new Desire(WANT_TO_MOVE);
			desires.add(desire);
			desire=new Desire(WANT_TO_LEAVE);
			desires.add(desire);
			desire=new Desire(IS_VISIT);
			desires.add(desire);
			*/
		
			//creates all plans of a agent.
			PlanComponent plan=agent.getComponent(PlanComponent.class);
			Subgoal subGoal=new Subgoal(agent,WANT_TO_MOVE);
			plan.addPlan(subGoal);
			/*
			Subgoal subGoal=new Subgoal(agent,WANT_TO_LEAVE);
			plan.addPlan(subGoal);
			subGoal=new Subgoal(agent,WANT_TO_MOVE);
			plan.addPlan(subGoal);
			subGoal=new Subgoal(agent,WANT_TO_VISIT);
			plan.addPlan(subGoal);
			subGoal=new Subgoal(agent,IS_VISIT);
			plan.addPlan(subGoal);
			*/
		}
		 
		 
		return !terminationCriterion(env, agent);
	}

	@Override
	protected Perception createPerception(KReaturesEnvironment env, Agent agent) {
		StatusAgentComponents statusAgent = agent.getComponent(StatusAgentComponents.class);
		SwarmMappingGeneric swarmMapping = agent.getComponent(SwarmMappingGeneric.class);
		return new SwarmPerception(swarmMapping.getAllStations(),statusAgent);
	}
	
	/**
	 * This will be execute after a agent's cycle.
	 */
	@Override
	protected void postCycle(KReaturesEnvironment env, Agent agent) {
		StatusAgentComponents statusAgent=agent.getComponent(StatusAgentComponents.class);		
		if(currentAction==ENTER_STATION &&  !statusAgent.getVisited()){
			statusAgent.report(agent.getName()+" is waitting at "+ statusAgent.getTarget()+ " because ENTER_STATION wasn't performed.");
		}
	}

	@Override
	protected boolean terminationCriterion(KReaturesEnvironment env, Agent agent) {
		StatusAgentComponents statusAgent=agent.getComponent(StatusAgentComponents.class);
		DefaultStation currentStation =agent.getComponent(SwarmMappingGeneric.class).getStationIntance(statusAgent.getCurrent());
		
		if(statusAgent.finishWwork()){
			statusAgent.setVisited(false);
			currentStation.decPlacesBusy();
			statusAgent.report("My work is finish.");
			return true;
		}	
		
		return false;
	}

	
}
