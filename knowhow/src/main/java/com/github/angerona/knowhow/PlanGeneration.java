package com.github.angerona.knowhow;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.serialize.SerializeHelper;
import com.github.angerona.knowhow.graph.GraphPlannerStrategy;
import com.github.angerona.knowhow.situation.Situation;

public class PlanGeneration extends Action {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(PlanGeneration.class);
	
	private String goalname;
	
	private Situation situation;
	
	public PlanGeneration(Agent planner, String filename, String goalname) {
		super(planner, planner.getName());
		this.goalname = goalname;
		
		try {
			situation = SerializeHelper.loadXml(Situation.class, new File(filename));
		} catch (Exception e) {
			LOG.error("Cannot find '" + filename + "' PlanGeneration will do nothing" );
		}
	}
}
