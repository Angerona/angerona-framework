package com.github.angerona.knowhow.situation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;
import net.sf.tweety.lp.asp.util.AnswerSet;
import net.sf.tweety.lp.asp.util.AnswerSetList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowStatement;

/**
 * 
 * 
 * @author Tim Janus
 */
public class InvestigationSituationBuilder extends SituationBuilderAdapter {
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(InvestigationSituationBuilder.class);
	
	private InvestigationSituation situation;
	
	/** @todo move in resource management */
	private Program investigate;
	
	public InvestigationSituationBuilder(InvestigationSituation situation, Agent agent) {
		super(situation, agent);
		LOG.debug("Entering InvestigationBuilder({})", situation);
		this.situation = situation;
		investigate = loadProgramFromJar("/com/github/angerona/knowhow/situation/investigation.dlp");
		LOG.debug("Leaving InvestigationBuilder()");
	}
	
	@Override
	protected Program generateInputProgram() {
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
		return input;
	}

	@Override
	protected KnowhowBase knowhowBaseFromAnswerSet(AnswerSetList asl) {
		KnowhowBase situationBase = new KnowhowBase();
		DLPAtom targetAtom = new DLPAtom(situation.getGoal());
		
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
		return situationBase;
	}
}
