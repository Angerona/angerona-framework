package com.github.angerona.knowhow.situation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logics.commons.syntax.Variable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.github.angerona.fw.am.secrecy.HonestyType;
import com.github.angerona.fw.serialize.SerializeHelper;
import com.github.angerona.serialize.asp.DLPAtomTransform;

@Root(name="defending-situation")
public class DefendingSituation extends Situation {
	
	@Root(name="behavior")
	public static class Behavior {
		@Attribute(name="question")
		private DLPLiteral question;
		
		@Attribute(name="honesty-type")
		private HonestyType honestyType;
		
		public DLPLiteral getQuestion() {
			return question;
		}
		
		public HonestyType getHonestyType() {
			return honestyType;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)	return true;
			if(other == null || other.getClass() != getClass()) return false;
			Behavior o = (Behavior)other;
			return honestyType == o.honestyType && question.equals(o.question);
		}
		
		@Override
		public int hashCode() {
			return (question.hashCode() + honestyType.hashCode()) * 17;
		}
	}
	
	@Root(name="additional-information")
	public static class AdditionalInformation {
		@Element(name="behavior")
		private Behavior behavior;
		
		@Attribute(name="answer")
		private DLPLiteral additionalInformation;
		
		public Behavior getBehavior() {
			return behavior;
		}
		
		public DLPLiteral getAdditionalInformation() {
			return additionalInformation;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)	return true;
			if(other == null || other.getClass() != getClass()) return false;
			AdditionalInformation o = (AdditionalInformation)other;
			return additionalInformation.equals(o.additionalInformation) && behavior.equals(o.behavior);
		}
		
		@Override
		public int hashCode() {
			return (behavior.hashCode() + additionalInformation.hashCode()) * 13;
		}
	}
	
	@Element(name="attacker")
	private String attacker;
	
	@ElementList(inline=true, entry="question")
	private Set<DLPLiteral> questions = new HashSet<>();
	
	@ElementList(inline=true, entry="allowedBehavior")
	private Set<Behavior> allowedBehaviors = new HashSet<>();
	
	@ElementList(inline=true, entry="additionalInformation")
	private Set<AdditionalInformation> additionalInformation = new HashSet<>();
	
	public String getAttackerName() {
		return attacker;
	}
	
	public Set<DLPLiteral> getQuestions() {
		return Collections.unmodifiableSet(questions);
	}
	
	public Set<Behavior> getAllowedBehaviors() {
		return Collections.unmodifiableSet(allowedBehaviors);
	}
	
	public Set<AdditionalInformation> getAdditonalInformation() {
		return Collections.unmodifiableSet(additionalInformation);
	}
	
	public static void main(String [] args) {
		SerializeHelper sh = SerializeHelper.get();
		sh.addTransformMapping(DLPAtom.class, DLPAtomTransform.class);
		
		DefendingSituation sit = new DefendingSituation();
		sit.goal = "defending";
		DLPAtom se = new DLPAtom("se");
		DLPAtom wd = new DLPAtom("wd", new Variable("X"));
		sit.questions.add(se);
		sit.questions.add(wd);
		
		Behavior behavior = new Behavior();
		behavior.question = se;
		behavior.honestyType = HonestyType.HT_HONEST;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.question = se;
		behavior.honestyType = HonestyType.HT_LIE;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.question = wd;
		behavior.honestyType = HonestyType.HT_HONEST;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.question = wd;
		behavior.honestyType = HonestyType.HT_BULLSHITTING;
		sit.allowedBehaviors.add(behavior);
		
		AdditionalInformation ai = new AdditionalInformation();
		ai.behavior = behavior;
		ai.additionalInformation = new DLPAtom("samsung");
		sit.additionalInformation.add(ai);
		
		
		sh.outputXml(sit, System.out);
	}
}
