package com.github.angerona.knowhow.graph;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.parser.ParseException;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.example.operators.GenerateOptionsOperator;
import com.github.angerona.fw.example.operators.SubgoalGenerationOperator;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.knowhow.graph.parameter.DefaultPlanConverter;
import com.github.angerona.knowhow.graph.parameter.PlanConverter;
import com.github.angerona.knowhow.penalty.PenaltyFunction;
import com.github.angerona.knowhow.penalty.RestrictiveSecretsPenalty;

/**
 * A subgoal generation operator tries to generate plans for the desires
 * of the agent using the knowhow over graphs approach.
 * @author Tim Janus
 */
public class KnowhowGraphSubgoal extends SubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(KnowhowGraphSubgoal.class);
	
	private GraphPlannerStrategy planningStrategy = new GraphPlanner();
	
	@Override
	protected Boolean processInternal(PlanParameter param) {
		param.report("Using Knowhow for Subgoal Generation.");
		
		KnowhowGraph comp = param.getAgent().getComponent(KnowhowGraph.class);
		if(comp == null) {
			LOG.warn("Using KnowhowGraphSubgoal without KnowhowGraph data component");
			return super.processInternal(param);
		}
		ListenableDirectedGraph<GraphNode, DefaultEdge> graph = comp.getGraph();
		
		boolean gen  = false;
		for (Desire des : param.getAgent().getComponent(Desires.class).getDesires()) {			
			if(param.getActualPlan().countPlansFor(des) > 0)
				continue;
			
			List<WorkingPlan> plans = new ArrayList<>();
			if(des.getFormula().toString().startsWith("revisionRequestProcessing")) {	
				plans = handleInform(param, des, graph);
			} else if(des.getFormula().toString().startsWith(GenerateOptionsOperator.prepareQueryProcessing.getName())) {
				Query context = (Query)des.getPerception();
				String desAsStr = "answer_query(" + context.getQuestion().toString() +")";
				FolParserB parser = new FolParserB(new StringReader(desAsStr));
				FolFormula formula = null;
				try {
					formula = parser.formula(new FolSignature());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(formula != null) {
					Desire extDes = new Desire(formula, context);
					plans = handeDefaultDesire(param, extDes, graph);
				}
				
				if(plans.isEmpty()) {
					Desire defDes = new Desire(new FOLAtom(new Predicate("answer_query")), des.getPerception());
					plans = handeDefaultDesire(param, defDes, graph);
				}
			} else {
				plans = handeDefaultDesire(param, des, graph);
			}
			
			gen = translate(plans, param, des) || gen;
		}
		
		if(gen)
			return true;
		else
			return super.processInternal(param);
	}

	private boolean translate(List<WorkingPlan> plans, PlanParameter param, Desire des) {
		PlanComponent pc = param.getActualPlan();
		Subgoal sg = new Subgoal(param.getAgent(), des);
		boolean gen = false;
		for(WorkingPlan plan : plans) {
			PlanConverter converter = getPlanConverter(param);
			List<Intention> intentions = converter.convert(plan.getRootIntention(), des.getPerception());
						
			
			if(intentions.isEmpty())
				continue;
			
			LOG.info("Converting WorkingPlan into Angerona format");
					
			// reverse add the intentions to the stack (the list of intentions is ordered in the way that the first 
			// intention has to be done first and the stack of the angerona planning is ordered the other way round.
			int index = sg.newStack();
			for(int i=intentions.size()-1; i>=0; --i) {
				sg.addToStack(new PlanElement(intentions.get(i)), index);
			}
			pc.addPlan(sg);
			gen = true;
		}
		return gen;
	}
	
	private List<WorkingPlan> handleInform(PlanParameter param, Desire des, ListenableDirectedGraph<GraphNode, DefaultEdge> graph) {
		List<WorkingPlan> plans = new ArrayList<>();
		Inform toReact = (Inform)des.getPerception();
		if(toReact.getSentences().size() == 0)
			return plans;
		
		Set<FolFormula> infered = param.getAgent().getBeliefs().getWorldKnowledge().infere();
		for(FolFormula info : toReact.getSentences()) {
			if(!infered.contains(info)) {
				FolParserB parser = new FolParserB(new StringReader("not_sure(" + info.toString() + ")"));
				FolFormula formula = null;
				try {
					formula = parser.formula(new FolSignature());
					Desire tempDes = new Desire(formula, toReact);
					plans.addAll(handeDefaultDesire(param, tempDes, graph));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		}
		
		return plans;
	}
	
	private List<WorkingPlan> handeDefaultDesire(PlanParameter param, Desire des,
			ListenableDirectedGraph<GraphNode, DefaultEdge> graph) {
		planningStrategy.setPenaltyTemplate(getPenaltyFunction(param));
		planningStrategy.setPlanConverter(getPlanConverter(param));
		
		List<WorkingPlan> plans = planningStrategy.controlPlan(graph, des);
		String output = String.format("The desire '%s' generates", des.toString());
		if(plans.isEmpty()) {
			output += " no plans";
		} else {
			output += String.format(" the plan '%s'", plans.toString());
		}
		LOG.info(output);

		return plans;
	}
	
	private PenaltyFunction getPenaltyFunction(PlanParameter param) {
		PenaltyFunction reval = null;
		
		String penClsName = param.getSetting("penaltyFunction", RestrictiveSecretsPenalty.class.getName());
		try {
			Class<?> penaltyCls = Class.forName(penClsName);
			reval = (PenaltyFunction)penaltyCls.newInstance();
			reval.init(param.getAgent());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException e) {
			e.printStackTrace();
			LOG.error("Cannot create PenaltyFunction: '{}' - '{}'", penClsName, e);
		} 
		
		return reval;
	}
	
	private PlanConverter getPlanConverter(PlanParameter param) {
		PlanConverter reval = null;
		
		String convClsName = param.getSetting("planConverter", DefaultPlanConverter.class.getName());
		try {
			Class<?> converterCls = Class.forName(convClsName);
			reval = (PlanConverter)converterCls.newInstance();
			reval.init(param.getAgent());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException e) {
			e.printStackTrace();
			LOG.error("Cannot create PlanConverter: '{}' - '{}'", convClsName, e);
		} 
		
		return reval;
	}
}
