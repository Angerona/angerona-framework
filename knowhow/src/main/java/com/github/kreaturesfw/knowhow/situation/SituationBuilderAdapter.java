package com.github.kreaturesfw.knowhow.situation;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.tweety.lp.asp.parser.ASPParser;
import net.sf.tweety.lp.asp.parser.ParseException;
import net.sf.tweety.lp.asp.solver.Solver;
import net.sf.tweety.lp.asp.solver.SolverException;
import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;
import net.sf.tweety.lp.asp.util.AnswerSetList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.asp.logic.SolverWrapper;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.knowhow.KnowhowBase;
import com.github.kreaturesfw.knowhow.KnowhowStatement;
import com.github.kreaturesfw.knowhow.graph.GraphNode;
import com.github.kreaturesfw.knowhow.graph.KnowhowBaseGraphBuilder;
import com.github.kreaturesfw.knowhow.graph.Processor;
import com.github.kreaturesfw.knowhow.graph.Selector;

/**
 * 
 * @author Tim Janus
 */
public abstract class SituationBuilderAdapter implements SituationGraphBuilder {
	private static Logger LOG = LoggerFactory.getLogger(SituationBuilderAdapter.class);
	
	protected Graph<GraphNode, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
	
	protected Agent agent;
	
	protected Situation situation;
	
	public SituationBuilderAdapter(Situation situation, Agent agent) {
		this.agent = agent;
		this.situation = situation;
	}
	
	@Override
	public Graph<GraphNode, DefaultEdge> getGraph() {
		return graph;
	}
	
	abstract protected KnowhowBase knowhowBaseFromAnswerSet(AnswerSetList asl);
	
	abstract protected Program generateInputProgram();
	
	@Override
	public void build() {
		LOG.debug("Entering build()");
		
		Program input = generateInputProgram();
		AnswerSetList asl = processAnswerSets(input);
		if(asl != null) {
			LOG.trace("Created answer-sets:\n{}", asl.toString());
			graph = new DefaultDirectedGraph<>(DefaultEdge.class);
			KnowhowBase situationBase = knowhowBaseFromAnswerSet(asl);
			generateGraphByKnowhowBase(situation, situationBase);
			
			/*
			JGraphXAdapter<GraphNode, DefaultEdge> adapter = new JGraphXAdapter<>();
			adapter.setDataSource(new ListenableDirectedGraph((DirectedGraph)graph));
			mxHierarchicalLayout layout = new mxHierarchicalLayout(adapter);
			layout.execute(adapter.getDefaultParent());
			JFrame frame = new JFrame();
			frame.setSize(600, 400);
			frame.getContentPane().add(new JScrollPane(adapter.generateDefaultGraphComponent()));
			frame.setVisible(true);
			*/
		}
		
		LOG.debug("Leaving build() = void");
	}
	
	protected void generateGraphByKnowhowBase(Situation situation, KnowhowBase situationBase) {
		KnowhowBaseGraphBuilder builder = new KnowhowBaseGraphBuilder(situationBase, agent);
		builder.build();
		graph = builder.getGraph();
		
		// add top of graph:
		Selector connectorNode = new Selector(new DLPAtom(situation.getGoal()), graph);
		graph.addVertex(connectorNode);
		for(KnowhowStatement statement : situationBase.getStatements()) {
			graph.addEdge(connectorNode, new Processor(statement, graph));
		}
	}
	
	protected Program loadProgramFromJar(String jarPath) {
		Program reval = null;
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null) {
			LOG.error("Creation of InvestigationSituationBuilder not possible: Cannot read Resource: '{}'", jarPath);
		} else {
			try {
				reval = ASPParser.parseProgram(new InputStreamReader(stream));
			} catch (ParseException e) {
				e.printStackTrace();
				LOG.error("Creation of InvestigationSituationBuilder not possible: Cannot parse program in '{}'", jarPath);
			}
		}
		return reval;
	}
	
	protected AnswerSetList processAnswerSets(Program input) {
		SolverWrapper wrapper = SolverWrapper.DLV;
		Solver solver = wrapper.getSolver();
		AnswerSetList asl = null;
		try {
			asl = solver.computeModels(input, 100);
		} catch (SolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return asl;
	}
	
	protected Rule createRule(String str) {
		Rule reval = null;
		try {
			reval = ASPParser.parseRule(str);
		} catch (ParseException e) {
			LOG.warn("Cannot parse the ASP-Fact: '{}'", str);
			e.printStackTrace();
		}
		return reval;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
