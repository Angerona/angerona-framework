package com.github.angerona.knowhow.situation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.am.secrecy.HonestyType;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowStatement;
import com.github.angerona.knowhow.situation.DefendingSituation.AdditionalInformation;
import com.github.angerona.knowhow.situation.DefendingSituation.Behavior;
import com.github.angerona.knowhow.situation.DefendingSituation.Question;

public class DefendingSituationBuilder extends SituationBuilderAdapter {
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(InvestigationSituationBuilder.class);

	private DefendingSituation situation;
	
	private Program programDefending;
	
	public DefendingSituationBuilder(DefendingSituation situation, Agent agent) {
		super(situation, agent);
		this.situation = situation;
		programDefending = loadProgramFromJar("/com/github/angerona/knowhow/situation/defending.dlp");
	}


	@Override
	protected KnowhowBase knowhowBaseFromAnswerSet(AnswerSetList asl) {
		KnowhowBase reval = new KnowhowBase();
		DLPAtom targetAtom = new DLPAtom(situation.getGoal());
		
		for(AnswerSet as : asl) {			
			List<DLPLiteral> answerAtom = new ArrayList<>(as.getLiteralsWithName("answer"));
			Collections.sort(answerAtom, new Comparator<DLPLiteral>() {
				@Override
				public int compare(DLPLiteral o1, DLPLiteral o2) {
					NumberTerm t1 = (NumberTerm)o1.getArguments().get(2);
					NumberTerm t2 = (NumberTerm)o2.getArguments().get(2);
					return t1.get() - t2.get();
				}
			});
			
			String attackerName = situation.getAttackerName();
			if(!attackerName.startsWith("a_"))
				attackerName = "a_"+attackerName;
			
			int index = 0;
			List<DLPAtom> askedHistory = new ArrayList<>();
			for(DLPLiteral lit : answerAtom) {
				DLPAtom asked = new DLPAtom("asked", new NumberTerm(index++), lit.getArguments().get(0));
				askedHistory.add(asked);
				
				String questionSymbol = lit.getArguments().get(0).toString();
				Question questiton = null;
				for(Question cur : situation.getQuestions()) {
					// there is not more than one question with the same symbol
					// see the deserialization method (commit) of DefendingSituation.
					if(cur.getSymbol().equals(questionSymbol)) {
						questiton = cur;
						break;
					}
				}
				
				
				List<DLPAtom> subTargets = new ArrayList<>();
				DLPAtom atom = new DLPAtom("s_QueryAnswer", new Constant(attackerName), questiton.asTerm(), 
						lit.getArguments().get(1));
				subTargets.add(atom);
				KnowhowStatement stmt = new KnowhowStatement(targetAtom, subTargets, askedHistory, 
						askedHistory.size(), null);
				reval.addStatement(stmt);
			}
			
		}
		
		return reval;
	}

	@Override
	protected Program generateInputProgram() {
		Program input = new Program();
		
		for(Question question : situation.getQuestions()) {
			input.addFact(new DLPAtom("question", new Constant(question.getSymbol())));
		}
		
		
		for(Behavior behavior : situation.getAllowedBehaviors()) {
			input.addFact(new DLPAtom("answer_behavior", new Constant(behavior.getQuestion().getSymbol()), 
					new Constant("p_"+behavior.getHonestyType().toString())));
		}
		
		for(AdditionalInformation ai : situation.getAdditonalInformation()) {
			DLPLiteral factToAdd = null;
			HonestyType honesty = ai.getBehavior().getHonestyType();
			if(honesty == HonestyType.HT_BULLSHITTING) {
				throw new NotImplementedException("Bullshitting not implemented yet");
			// TODO: Test and implement
			//	factToAdd = new DLPAtom("answer_bs", ai.getBehavior().getQuestion().asTerm(), 
			//			new Constant(ai.getAdditionalInformation().toString()));
			} else if(honesty == HonestyType.HT_WITHHOLDING) {
				factToAdd = new DLPAtom("answer_wh", 
									new Constant(ai.getBehavior().getQuestion().getSymbol()), 
									new Constant("c_REJECT"));
			} else {
				throw new IllegalStateException();
			}
			input.addFact(factToAdd);
		}
		input.add(programDefending);
		input.add(situation.getBackground());
		return input;
	}

}
