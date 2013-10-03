package com.github.angerona.knowhow.situation;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.InstantiateVisitor;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import com.github.angerona.fw.am.secrecy.HonestyType;
import com.github.angerona.fw.serialize.SerializeHelper;
import com.github.angerona.fw.util.Utility;
import com.github.angerona.serialize.asp.DLPAtomTransform;

@Root(name="defending-situation")
public class DefendingSituation extends Situation {
	
	@Root(name="question")
	public static class Question {
		@Attribute(name="id")
		private int id;
		
		@Attribute(name="symbol")
		private String symbol;
		
		@ElementList(entry="argument", inline=true, required=false, empty=false)
		private List<String> arguments = new ArrayList<>();
		
		public Question(int id, DLPLiteral literal) {
			this.id = id;
			this.symbol = literal.getName();
			for(Term<?> arg : literal.getArguments()) {
				this.arguments.add(arg.toString());
			}
		}
		
		public Question(
				@Attribute(name="id") int id,
				@Attribute(name="symbol") String symbol,
				@ElementList(entry="argument", inline=true, required=false, empty=false) List<String> arguments) {
			this.id = id;
			this.symbol = symbol;
			this.arguments = arguments;
		}
		
		public String getSymbol() {
			return symbol;
		}
		
		public Term<?> asTerm() {
			
			ASPParser parser = new ASPParser(new StringReader(toString()));
			try {
				return new InstantiateVisitor().visit(parser.Term(), null);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<String> getArguments() {
			return Collections.unmodifiableList(arguments);
		}
		
		@Override
		public String toString() {
			String reval = symbol;
			if(!arguments.isEmpty()) {
				reval+="(";
				for(String arg : arguments) {
					reval += arg + ",";
				}
				reval = reval.substring(0, reval.length()-1) + ")";
			}
			return reval;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)	return true;
			if(other == null || other.getClass() != this.getClass()) return false;
			Question q = (Question)other;
			
			return id == q.id && Utility.equals(symbol, q.symbol) && Utility.equals(arguments, q.arguments);
		}
		
		@Override
		public int hashCode() {
			return (id * 7) + ((symbol.hashCode() + arguments.hashCode()) * 13);
		}
	}
	
	@Root(name="behavior")
	public static class Behavior {
		@Attribute(name="id")
		private int id;
		
		@Attribute(name="question")
		private int questionId = -1;
		
		@Attribute(name="honesty-type")
		private HonestyType honestyType;
		
		private Question question;
		
		public Question getQuestion() {
			return question;
		}
		
		public HonestyType getHonestyType() {
			return honestyType;
		}
		
		@Override
		public String toString() {
			return (question == null ? questionId : question.toString()) + "=" + honestyType.toString();
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)	return true;
			if(other == null || other.getClass() != getClass()) return false;
			Behavior o = (Behavior)other;
			return 	id == o.id && 
					honestyType == o.honestyType && 
					questionId == o.questionId && 
					Utility.equals(question, o.question);
		}
		
		@Override
		public int hashCode() {
			return (id + questionId + Utility.hash(question) + Utility.hash(honestyType)) * 17;
		}
	}
	
	@Root(name="additional-information")
	public static class AdditionalInformation {
		private Behavior behavior;

		@Attribute(name="behavior")
		private int behaviorId;
		
		@Attribute(name="answer")
		private DLPLiteral information;
		
		public Behavior getBehavior() {
			return behavior;
		}
		
		public DLPLiteral getAdditionalInformation() {
			return information;
		}
		
		@Override
		public String toString() {
			return "(" + (behavior == null ? behaviorId : behavior.toString()) + ")" + " -> " + information.toString();
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == this)	return true;
			if(other == null || other.getClass() != getClass()) return false;
			AdditionalInformation o = (AdditionalInformation)other;
			
			return Utility.equals(information, o.information) && Utility.equals(behavior, o.behavior);
		}
		
		@Override
		public int hashCode() {
			return (Utility.hash(behavior) + Utility.hash(information)) * 13;
		}
	}
	
	private Map<Integer, Question> questionMap = new HashMap<>();
	
	private Map<Integer, Behavior> behaviorMap = new HashMap<>();
	
	@Element(name="attacker")
	private String attacker;
	
	@ElementList(inline=true, entry="question")
	private Set<Question> questions = new HashSet<>();
	
	@ElementList(inline=true, entry="allowedBehavior")
	private Set<Behavior> allowedBehaviors = new HashSet<>();
	
	@ElementList(inline=true, entry="additionalInformation")
	private Set<AdditionalInformation> additionalInformation = new HashSet<>();
	
	public String getAttackerName() {
		return attacker;
	}
	
	public Set<Question> getQuestions() {
		return Collections.unmodifiableSet(questions);
	}
	
	public Set<Behavior> getAllowedBehaviors() {
		return Collections.unmodifiableSet(allowedBehaviors);
	}
	
	public Set<AdditionalInformation> getAdditonalInformation() {
		return Collections.unmodifiableSet(additionalInformation);
	}
	
	@Validate
	public void validate() throws PersistenceException {
		List<String> err = new ArrayList<>();
		
		// check if no question is double (id or real-equal)):
		List<Question> temp = new ArrayList<>(questions);
		for(int i=0; i<temp.size(); ++i) {
			Question iQ = temp.get(i);
			for(int k=i+1; k<temp.size(); ++k) {
				Question kQ = temp.get(k); 
				
				// check for duplo id:
				if(iQ.id == kQ.id) {
					err.add("The questions '" + iQ.toString() + "' and '" + 
							kQ.toString() + "' share the same id: '" + iQ.id + "'");
				// check for equal questions with mismatching ids
				} else if(Utility.equals(iQ.symbol, kQ.symbol)) {
					err.add("There exist two versions of questions with the same symbol: '" + iQ.toString() + 
							"' one with 'id=" + iQ.id + "' and the other with id='" + kQ.id + "', questions with same " +
							"symbol but difference arguments are not supported.");
				}
			}
		}
		
		// check if all the behavior and additional information can be referenced
		for(Question question : questions) {
			questionMap.put(question.id, question);
		}
		
		for(Behavior behavior : this.allowedBehaviors) {
			if(!questionMap.keySet().contains(behavior.questionId)) {
				err.add("The behavior '" + behavior.toString() + "' references not existing question with id='" 
						+ behavior.questionId + "'");
			}
			behaviorMap.put(behavior.id, behavior);
		}
		
		for(AdditionalInformation ai : this.additionalInformation) {
			if(!behaviorMap.keySet().contains(ai.behaviorId)) {
				err.add("The additional-information: '" + ai.toString() + 
						"' references not existing behavior with id='" + ai.behaviorId + "'");
			}
		}
		
		if(!err.isEmpty()) {
			String out = "Cannot deserialize DefendingSituation:";
			for(String line : err) {
				out += "\n" + line;
			}
			throw new PersistenceException(out);
		}
		
		super.validate();
	}
	
	@Commit
	public void build() throws PersistenceException {
		for(Behavior behavior : allowedBehaviors) {
			behavior.question = questionMap.get(behavior.questionId);
		}
		
		for(AdditionalInformation ai : additionalInformation) {
			ai.behavior = behaviorMap.get(ai.behaviorId);
		}
		
		super.build();
	}
	
	public static void main(String [] args) {
		SerializeHelper sh = SerializeHelper.get();
		sh.addTransformMapping(DLPAtom.class, DLPAtomTransform.class);
		
		DefendingSituation sit = new DefendingSituation();
		sit.goal = "defending";
		sit.attacker = "claire";
		
		DLPAtom se = new DLPAtom("se");
		DLPAtom wd = new DLPAtom("wd", new Variable("X"));
		sit.questions.add(new Question(1, se));
		sit.questions.add(new Question(2, wd));
		
		Behavior behavior = new Behavior();
		behavior.id = 1;
		behavior.questionId = 1;
		behavior.honestyType = HonestyType.HT_HONEST;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.id = 2;
		behavior.questionId = 1;
		behavior.honestyType = HonestyType.HT_LIE;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.id = 3;
		behavior.questionId = 2;
		behavior.honestyType = HonestyType.HT_HONEST;
		sit.allowedBehaviors.add(behavior);
		
		behavior = new Behavior();
		behavior.id = 4;
		behavior.questionId = 2;
		behavior.honestyType = HonestyType.HT_BULLSHITTING;
		sit.allowedBehaviors.add(behavior);
		
		AdditionalInformation ai = new AdditionalInformation();
		ai.behaviorId = 4;
		ai.information = new DLPAtom("samsung");
		sit.additionalInformation.add(ai);
		
		
		sh.outputXml(sit, System.out);
	}
}
