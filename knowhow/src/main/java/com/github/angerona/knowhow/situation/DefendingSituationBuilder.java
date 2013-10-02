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
			int penalty = 0;
			for(DLPLiteral lit : as.getLiteralsWithName("penalty")) {
				Integer curPen = (Integer)lit.getArguments().get(0).get();
				if(curPen > penalty) {
					penalty = curPen;
				}
			}
			
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
			
			List<DLPAtom> subTargets = new ArrayList<>();
			for(DLPLiteral lit : answerAtom) {
				DLPAtom atom = new DLPAtom("s_QueryAnswer", new Constant(attackerName), lit.getArguments().get(0), 
						lit.getArguments().get(1));
				subTargets.add(atom);
			}
			
			KnowhowStatement stmt = new KnowhowStatement(targetAtom, subTargets, new ArrayList<DLPAtom>());
			reval.addStatement(stmt);
		}
		
		return reval;
	}

	@Override
	protected Program generateInputProgram() {
		Program input = new Program();
		for(DLPLiteral question : situation.getQuestions()) {
			input.addFact(new DLPAtom("question", new Constant(question.toString())));
		}
		
		for(Behavior behavior : situation.getAllowedBehaviors()) {
			input.addFact(new DLPAtom("answer_behavior", new Constant(behavior.getQuestion().toString()), 
					new Constant("p_"+behavior.getHonestyType().toString())));
		}
		
		for(AdditionalInformation ai : situation.getAdditonalInformation()) {
			DLPLiteral factToAdd = null;
			HonestyType honesty = ai.getBehavior().getHonestyType();
			if(honesty == HonestyType.HT_BULLSHITTING) {
				factToAdd = new DLPAtom("answer_bs", new Constant("p_"+ai.getBehavior().getQuestion().toString()), 
						new Constant(ai.getAdditionalInformation().toString()));
			} else if(honesty == HonestyType.HT_WITHHOLDING) {
				throw new NotImplementedException("Withholding not implemnted in defending situation yet" );
			} else {
				throw new IllegalStateException();
			}
			input.addFact(factToAdd);
		}
		input.add(programDefending);
		return input;
	}

}
