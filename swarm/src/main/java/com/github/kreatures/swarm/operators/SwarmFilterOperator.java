package com.github.kreatures.swarm.operators;



import java.util.Set;
/**
* List of Default Desires
*/
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_MOVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.IS_VISIT;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_LEAVE;
import static com.github.kreatures.swarm.basic.SwarmDesires.WANT_TO_VISIT;
import static com.github.kreatures.swarm.basic.SwarmDesires.isEqual;

import com.github.kreatures.core.Desire;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.simple.operators.FilterOperator;
//import com.github.kreatures.swarm.components.DefaultStation;
import com.github.kreatures.swarm.components.StatusAgentComponents;
//import com.github.kreatures.swarm.components.SwarmMappingGeneric;

public class SwarmFilterOperator extends FilterOperator {
	
	@Override
	protected Desire choose(Set<Desire> options, Desire pursued, PlanParameter preprocessedParameters) {
		StatusAgentComponents statusAgent=preprocessedParameters.getAgent().getComponent(StatusAgentComponents.class);
		//SwarmMappingGeneric swarmMapping=preprocessedParameters.getAgent().getComponent(SwarmMappingGeneric.class);
		//DefaultStation targetStation=swarmMapping.getStationIntance(statusAgent.getTarget());
	//	DefaultStation currentStation=swarmMapping.getStationIntance(statusAgent.getCurrent());
		// agent is Moving to next station
		if(isEqual(pursued, WANT_TO_MOVE)&& statusAgent.getDistance()>0 && options.contains(WANT_TO_MOVE)){
			return WANT_TO_MOVE;
		}
		//agent will be Waiting until there are places. 
		if(isEqual(pursued, WANT_TO_MOVE) && statusAgent.getDistance()==0 && options.contains(WANT_TO_VISIT)){//&& targetStation.getCapacity()>targetStation.getPlacesBusy()){
			return WANT_TO_VISIT;
		}
		//agent has waited and will be Waiting until there are places.
		if(options.contains(WANT_TO_VISIT)&&isEqual(pursued, WANT_TO_VISIT) && statusAgent.getTarget()!=null && statusAgent.getDistance()==0){
			//currentStation.setPlacesBusy(currentStation.getPlacesBusy()-1);
			
			return WANT_TO_VISIT;
		}
		
		//agent will be entering target's station and do its job. 
		if(options.contains(IS_VISIT)&& isEqual(pursued, WANT_TO_VISIT) && statusAgent.getVisited()){
			//targetStation.setPlacesBusy(targetStation.getPlacesBusy()+1);
			//statusAgent.setCurrent(statusAgent.getTarget());
			//statusAgent.setTarget(null);
			return IS_VISIT;
		}
		//agent will be leave a current's station. 
		if(options.contains(WANT_TO_LEAVE)&&isEqual(pursued, IS_VISIT) && statusAgent.getTarget()!=null){
			//currentStation.setPlacesBusy(currentStation.getPlacesBusy()-1);
			
			return WANT_TO_LEAVE;
		}
		//agent will be move to target's station. 
		if(options.contains(WANT_TO_MOVE)&& isEqual(pursued, WANT_TO_LEAVE) ){
			//currentStation.setPlacesBusy(currentStation.getPlacesBusy()-1);
			
			return WANT_TO_MOVE;
		}
		
		return null;
	}
}
