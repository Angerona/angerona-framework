package com.github.angerona.knowhow.graph.parameter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.util.Utility;
import com.github.angerona.knowhow.graph.GraphIntention;
import com.github.angerona.knowhow.graph.GraphNode;
import com.github.angerona.knowhow.graph.Processor;
import com.github.angerona.knowhow.graph.Selector;

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
						mapping.put(param, interpretParameter(parent.get(index)));
						LOG.info("Added parameter mapping: '" + parent.get(index) + "' --> '" + param + "'");
						index += 1;
					}
				}
			} else if(node instanceof Selector) {
				reval = new ArrayList<>(node.getParameters());
				for(int i=0; i<reval.size(); ++i) {
					
					// check for mapping:
					if(parent.size() < i) {
						Parameter old = parent.get(i);
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
										entry.getKey().getIdentifier(), entry.getValue().getIdentifier()));
								reval.set(i, alternative);								
								LOG.info("Replaced inline-parameter '" + curPar + "' with '" + alternative + "'" );
								curPar = alternative;
							}
						}
					}
				}
			}
			
			parent = current;
		}
		return reval;
	}
	
	@Override
	public List<Intention> convert(GraphIntention graphIntention, Perception context) {
		List<Intention> reval = new ArrayList<>();
				
		if(graphIntention.isAtomic()) {
			List<DefaultEdge> edges = pathFromRoot(graphIntention);
			List<Parameter> parameters = mapParameters(graphIntention.getNode().getGraph(), edges);
			
			String action = graphIntention.getNode().getName();
			if(action.equals("Inform")) {
				reval.add(createInform(parameters));
			} else if(action.equals("Query")) {
				reval.add(createQuery(parameters));
			} else if(action.equals("QueryAnswer")) {
				if(! (context instanceof Query))
					throw new IllegalStateException("Context of Answer is no 'Query' but '" + context.getClass().getSimpleName() + "'");
				reval.add(createAnswer(parameters, (Query)context));
			} else {
				throw new NotImplementedException("Generation of Action '" + action + "' not implemented yet");
			}
		} else {
			for(GraphIntention gi : graphIntention.getSubIntentions()) {
				reval.addAll(this.convert(gi, context));
			}
		}
		return reval;
	}

	private Answer createAnswer(List<Parameter> parameters, Query context) {
		if(parameters.size() != 1) {
			throw new IllegalStateException("The parameter count for an answer must be '1' not '" + parameters.size() + "'");
		}
		
		// TODO this bunch of code is duplo, create a factory somewhere:
		if(context.getQuestion().isGround()) {
			AnswerValue simpleAnswer = agent.getBeliefs().getWorldKnowledge().reason(context.getQuestion()).getAnswerValue();		
			Parameter param = parameters.get(0);
			if(param.getIdentifier().equals("p_honest")) {
				// do nothing
			} else if(param.getIdentifier().equals("p_lie")) {
				simpleAnswer = Utility.lie(simpleAnswer);
			} else {
				throw new NotImplementedException("The honesty type: '" + param.getIdentifier().substring(2) + "' is not implemented yet.");
			}
			
			return new Answer(this.agent, context.getSenderId(), context.getQuestion(), 
					new AngeronaAnswer(context.getQuestion(), simpleAnswer));
		}
		
		throw new NotImplementedException("Cannot handle the answer on open queries yet.");
	}
	
	private Query createQuery(List<Parameter> parameters) {
		if(parameters.size() != 2) {
			// TODO: error
		}
		
		Parameter recvP = parameters.get(0);
		Parameter questionP = parameters.get(1);
		
		Query query = new Query(agent, interpretParameter(recvP).getIdentifier(), createFormula(questionP));
		return query;
	}
	
	private Inform createInform(List<Parameter> parameters) {
		if(parameters.size() != 2) {
			// TODO Error:
		}
		
		Parameter recvP = parameters.get(0);
		Parameter formulaP = parameters.get(1);
		
		Set<FolFormula> formulas = new HashSet<>();
		formulas.add(createFormula(formulaP));
		Inform inform = new Inform(agent.getName(), 
				mapAgent(recvP), formulas);
		
		return inform;
	}
	
	private FolFormula createFormula(Parameter param) {
		FolParserB parser = new FolParserB(new StringReader(param.getIdentifier()));
		FolFormula reval = null;
		try {
			reval = parser.formula(new FolSignature());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reval;
	}
	
	private Parameter interpretParameter(Parameter param) {
		if(param.getIdentifier().startsWith("a_"))
			return new Parameter(mapAgent(param));
		return param;
	}
	
	private String mapAgent(Parameter param) {
		String reval = param.getIdentifier().substring(2);
		return reval.equals("SELF") ? agent.getName() : reval;
	}
}
