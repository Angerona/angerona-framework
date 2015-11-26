package com.github.kreaturesfw.knowhow.graph.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.Intention;
import com.github.kreaturesfw.core.legacy.Perception;
import com.github.kreaturesfw.knowhow.graph.ActionAdapter;
import com.github.kreaturesfw.knowhow.graph.ActionAdapterResume;
import com.github.kreaturesfw.knowhow.graph.GraphIntention;
import com.github.kreaturesfw.knowhow.graph.GraphNode;
import com.github.kreaturesfw.knowhow.graph.Processor;
import com.github.kreaturesfw.knowhow.graph.Selector;
import com.github.kreaturesfw.knowhow.graph.WorkingPlan;
import com.github.kreaturesfw.knowhow.graph.parameter.Parameter.TYPE;

public class DefaultPlanConverter implements PlanConverter {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DefaultPlanConverter.class);
	
	private Agent agent;
	
	public DefaultPlanConverter() {}
	
	@Override 
	public void init(Agent agent) {
		this.agent = agent;
	}
	
	private List<DefaultEdge> pathFromRoot(GraphIntention gi) {
		Graph<GraphNode, DefaultEdge> graph = gi.getNode().getGraph();
		
		List<DefaultEdge> reval = new ArrayList<>();
		reval.add(0, graph.getEdge(gi.getSelector(), gi.getNode()));
		if(gi.getParent() != null) {
			reval.add(0, graph.getEdge(gi.getParent().getNode(), gi.getSelector()));
			reval.addAll(0, pathFromRoot(gi.getParent()));
		}
		
		return reval;
	}
	
	private List<Parameter> mapParameters(Graph<GraphNode, DefaultEdge> graph, List<DefaultEdge> edges) {
		Map<Parameter, Parameter> mapping = new HashMap<>();
		List<Parameter> current = new ArrayList<>();;
		List<Parameter> parent = new ArrayList<>();
		List<Parameter> reval = new ArrayList<>();
		
		for(DefaultEdge edge : edges) {
			GraphNode node = graph.getEdgeSource(edge);
			current = node.getParameters();
			
			if(node instanceof Processor) {
				// parameter mapping
				int index = 0;
				for(Parameter param : current) {
					if(param.isVariable() && !mapping.values().contains(param) && parent.size() > index) {
						mapping.put(param, parent.get(index));
						LOG.info("Added parameter mapping: '" + parent.get(index) + "' --> '" + param + "'");
						index += 1;
					}
				}
			} else if(node instanceof Selector) {
				reval = new ArrayList<>(node.getParameters());
				for(int i=0; i<reval.size(); ++i) {
					
					// check for mapping:
					if(i < parent.size()) {
						Parameter old = reval.get(i);
						Parameter alternative = mapping.get(old);
						if(alternative != null) {
							reval.set(i, alternative);
							LOG.info("Replaced parameter '" + old + "' with '" + alternative + "'");
						}
					} else {
						Parameter curPar = reval.get(i);
						for(Entry<Parameter, Parameter> entry: mapping.entrySet()) {
							if(curPar.getIdentifier().contains(entry.getKey().getIdentifier())) {
								Parameter alternative = new Parameter(curPar.getIdentifier().replace(
										entry.getKey().getIdentifier(), interpretParameter(entry.getValue())));
								reval.set(i, alternative);								
								LOG.info("Replaced inline-parameter '" + curPar + "' with '" + alternative + "'" );
								curPar = alternative;
							}
						}
					}
				}
				
				LOG.debug("Parameter Mapping: '{}'", mapping);
			}
			
			parent = current;
		}
		
		LOG.debug("Returning Parameters: '{}'", reval);
		return reval;
	}
	
	@Override
	public List<Intention> convert(WorkingPlan plan, GraphIntention graphIntention) {
		List<Intention> reval = new ArrayList<>();
				
		if(graphIntention.isAtomic()) {
			List<DefaultEdge> edges = pathFromRoot(graphIntention);
			List<Parameter> parameters = mapParameters(graphIntention.getNode().getGraph(), edges);
			
			String action = graphIntention.getNode().getName();
			Perception context = plan.getGoal().getPerception();
			Action toAdd = new ActionAdapter(agent, action, parameters, context);
			
			LOG.info("Created Action Adapter for: '{}'", toAdd.toString());
			reval.add(toAdd);
		} else if (graphIntention == GraphIntention.TBD) {
			
		} else {
			for(int i=0; i<graphIntention.getSubIntentions().size(); ++i) {
				GraphIntention gi = graphIntention.getSubIntentions().get(i);
				if(gi == GraphIntention.TBD) {
					reval.add(new ActionAdapterResume(plan, graphIntention, i));
				} else {
					reval.addAll(this.convert(plan, gi));
				}
			}
		}
		return reval;
	}

	private String interpretParameter(Parameter param) {
		if(param.getType() == TYPE.T_AGENT) {
			return mapAgent(param);
		} 
		
		return param.getIdentifier();
	}
	
	
	private String mapAgent(Parameter param) {
		String reval = param.getIdentifier().substring(2);
		return reval.equals("SELF") ? agent.getName() : reval;
	}
}
