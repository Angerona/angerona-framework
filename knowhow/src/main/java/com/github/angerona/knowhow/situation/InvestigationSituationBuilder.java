package com.github.angerona.knowhow.situation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.logic.asp.SolverWrapper;
import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowStatement;
import com.github.angerona.knowhow.graph.KnowhowBaseGraphBuilder;
import com.github.angerona.knowhow.graph.Processor;
import com.github.angerona.knowhow.graph.Selector;

/**
 * 
 * 
 * @author Tim Janus
 */
public class InvestigationSituationBuilder extends SituationBuilderAdapter {
	private static Logger LOG = LoggerFactory.getLogger(InvestigationSituationBuilder.class);
	
	private InvestigationSituation situation;
	
	/** @todo move in resource management */
	private Program investigate;
	
	public InvestigationSituationBuilder(InvestigationSituation situation, Agent agent, Selector connectingNode) {
		super(agent, connectingNode);
		LOG.debug("Entering InvestigationBuilder({})", situation);
		this.situation = situation;
		this.agent = agent;
		
		String jarPath = "/com/github/angerona/knowhow/situation/investigation.dlp";
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null) {
			LOG.error("Creation of InvestigationSituationBuilder not possible: Cannot read Resource: '{}'", jarPath);
		}
		try {
			investigate = ASPParser.parseProgram(new InputStreamReader(stream));
		} catch (ParseException e) {
			e.printStackTrace();
			LOG.error("Creation of InvestigationSituationBuilder not possible: Cannot parse program in '{}'", jarPath);
		}
		LOG.debug("Leaving InvestigationBuilder()");
	}
	
	@Override
	public void build() {
		LOG.debug("Entering build()");
		
		Program input = new Program();
		for(DLPAtom query : situation.getQueries()) {
			input.addFact(new DLPAtom("search_info", new Constant(query.toString())));
		}
			
		for(String source : situation.getSources()) {
			Rule fact = createRule("source_info(" + source + ").");
			if(fact != null)
				input.add(fact);
		}
	
		input.add(situation.getBackground());
		LOG.trace("Created input program:\n{}", input.toString());
		
		input.add(investigate);
		SolverWrapper wrapper = SolverWrapper.DLV;
		Solver solver = wrapper.getSolver();
		AnswerSetList asl = null;
		try {
			asl = solver.computeModels(input, 10);
		} catch (SolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DLPAtom targetAtom = new DLPAtom(situation.getGoal());
		if(asl != null) {
			LOG.trace("Created answer-sets:\n{}", asl.toString());
			graph = new DefaultDirectedGraph<>(DefaultEdge.class);
			Selector connectorNode = new Selector(situation.getGoal(), graph);
			graph.addVertex(connectorNode);
			
			KnowhowBase situationBase = new KnowhowBase();
			for(AnswerSet as : asl) {
				List<DLPAtom> queryAtom = new ArrayList<>();
				for(DLPLiteral lit : as.getLiteralsWithName("query")) {
					if(lit instanceof DLPAtom) {
						queryAtom.add((DLPAtom)lit);
					}
				}
				
				// check if the literals are in the correct format 
				// we can bypass this check if background program is empty:
				if(!situation.getBackground().isEmpty()) {
					String err = "";
					
					for(DLPLiteral lit : queryAtom) {
						if(lit.getArguments().size() != 3) {
							err += "The literal '" + lit.toString() + "' does not have 3 arguments.\n";
						}
						
						Term<?> sortArg = lit.getArguments().get(2);
						if(! (sortArg instanceof NumberTerm)) {
							err += "The third argument of the literal '" + lit.toString() + "' is no number.\n";
						}
					}
					
					if(!err.isEmpty()) {
						err += "using answer-set: '" + queryAtom + "'.";
						LOG.warn("Skipping one generated plan because of the following errors: {}", err);
						continue;
					}
				}
				
				
				Collections.sort(queryAtom, new Comparator<DLPLiteral>() {
					@Override
					public int compare(DLPLiteral o1, DLPLiteral o2) {
						NumberTerm t1 = (NumberTerm)o1.getArguments().get(2);
						NumberTerm t2 = (NumberTerm)o2.getArguments().get(2);
						return t1.get() - t2.get();
					}
				});
				
				List<DLPAtom> subTargets = new ArrayList<>();
				for(DLPAtom atom : queryAtom) {
					List<Term<?>> args = new ArrayList<>();
					String recvWithPrefix = "a_"+atom.getArguments().get(1).toString();
					args.add(new Constant(recvWithPrefix)); // add receiver
					args.add(atom.getArguments().get(0)); // add info
					DLPAtom converted = new DLPAtom("s_Query", args);
					subTargets.add(converted);
				}
				
				KnowhowStatement ks = new KnowhowStatement(targetAtom, 
						subTargets, new ArrayList<DLPAtom>());
				situationBase.addStatement(ks);
				
			}
			
			// build graph from base
			KnowhowBaseGraphBuilder builder = new KnowhowBaseGraphBuilder(situationBase, agent);
			builder.build();
			graph = builder.getGraph();
			
			
			// add top of graph:
			graph.addVertex(connectorNode);
			for(KnowhowStatement statement : situationBase.getStatements()) {
				graph.addEdge(connectorNode, new Processor(statement, graph));
			}
			
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
}
