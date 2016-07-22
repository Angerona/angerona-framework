package com.github.angerona.fw.plwithknowledge.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.Sat4jEntailment;
import net.sf.tweety.logics.pl.semantics.NicePossibleWorld;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.BaseReasoner;
import com.github.angerona.fw.operators.parameter.ReasonerParameter;
import com.github.angerona.fw.util.Pair;
/**
 * A reasoner for propositional beliefbase with knowledge and assertions
 * 
 * @author Pia Wierzoch
 */
public class PLWithKnowledgeReasoner extends BaseReasoner{

	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(PLWithKnowledgeReasoner.class);
	
	private ModelTupel beliefbaseModels;
	
	public PLWithKnowledgeReasoner(){
		
	}

	public ModelTupel inferModels(ModelTupel m, PropositionalFormula f, Set<PropositionalFormula> knowledge){
		Sat4jEntailment test = new Sat4jEntailment();
		Set<PropositionalFormula> testFormulaSet;
		
		testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
		testFormulaSet.add(f);
		if(!test.isConsistent(testFormulaSet)){
			//Update is inconsistent with knowledge, no need for changes
			return m;
		}else{
			Set<NicePossibleWorld> contextModels = new HashSet<NicePossibleWorld>();
			Set<NicePossibleWorld> iterator = new HashSet<NicePossibleWorld>(m.getModels());
						
			testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
			testFormulaSet.add(f);
			for(NicePossibleWorld world: iterator){	
				if(world.satisfies(testFormulaSet)){
					contextModels.add(world);
					
				}else{
					int nearest = Integer.MAX_VALUE;
					Set<NicePossibleWorld> possibleNewModels = new HashSet<NicePossibleWorld>();
					for(NicePossibleWorld a: m.getModelsOfKnowledge()){
						if(!a.equals(world) && a.satisfies(f)){
							int i = distnace(a, world);
							if(i == nearest){
								possibleNewModels.add(a);
							}else if(i < nearest){
								//nearer world found delete others
								nearest = distnace(a, world);
								possibleNewModels.clear();
								possibleNewModels.add(a);
							}
						}
					}
					contextModels.addAll(possibleNewModels);
				}
			}
			return new ModelTupel(m.getModelsOfKnowledge(), contextModels);
		}
	}
	
	@Override
	protected Set<FolFormula> inferImpl(ReasonerParameter params) {
		//calculate the models of the beliefbase
		this.calculateModels(params);
		
		PLWithKnowledgeBeliefbase beliefbase = (PLWithKnowledgeBeliefbase) params.getBeliefBase();
		Collection<Proposition> propositions = (Collection<Proposition>) beliefbase.getSignature();
		
		//calculate the facts that can be inferred from the models of the beliefbase
		Set<FolFormula> retval = new HashSet<FolFormula>();
		for(Proposition prop: propositions){
			int fal = 0, tru = 0;
			for(NicePossibleWorld world : beliefbaseModels.getModels()){
				if(world.satisfies(prop)){
					tru++;
				}else if(world.satisfies(new Negation(prop))){
					fal++;
				}
			}
			if(fal == 0 && tru > 0){
				retval.add(new FOLAtom(new Predicate(prop.getName())));
			}else if(fal > 0 && tru == 0){
				retval.add(new net.sf.tweety.logics.fol.syntax.Negation(new FOLAtom(new Predicate(prop.getName()))));
			}
		}
		return retval;
	}
	
	public AngeronaAnswer queryAnswer(ModelTupel m, FolFormula question){
		boolean t= true, f=true;
		FOLPropTranslator translator = new FOLPropTranslator();
		PropositionalFormula q = translator.toPropositional(question);
		for(NicePossibleWorld world: m.getModels()){
			if(!world.satisfies(q)){
				t = false;
			}
			if(world.satisfies(q)){
				f = false;
			}
		}
		if(t==true){
			return new AngeronaAnswer(question, AnswerValue.AV_TRUE);
		}else if(f==true){
			return new AngeronaAnswer(question, AnswerValue.AV_FALSE);
		}else{
			return new AngeronaAnswer(question, AnswerValue.AV_UNKNOWN);
		}
	}
	
	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryImpl(
			ReasonerParameter params) {
		//calculate the Models of the beliefbase
		this.calculateModels(params);
		
		FOLPropTranslator translator = new FOLPropTranslator();
		
		AnswerValue answer = AnswerValue.AV_FALSE;
		
		boolean satisfy = true;
		int i = 0;
		for(NicePossibleWorld a : beliefbaseModels.getModels()){
			if(!a.satisfies(translator.toPropositional(params.getQuery()))){
				satisfy = false;
				break;
			}else{
				i++;
			}
		}
		if(satisfy){//Query is true in every Model of the beliefbase
			answer = AnswerValue.AV_TRUE;
		}else if(!satisfy && i > 0) {// Query is true in some Models and false in some others
			answer = AnswerValue.AV_UNKNOWN;
		}else {//Query is in all Models false
			answer = AnswerValue.AV_FALSE;
		}
		Set<FolFormula> answers = new HashSet<FolFormula>();
		return new Pair<Set<FolFormula>, AngeronaAnswer>(answers, new AngeronaAnswer(params.getQuery(), answer));
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return PLWithKnowledgeBeliefbase.class;
	}
	
	private int distnace(NicePossibleWorld a, NicePossibleWorld b){
		int y=0;
		for (int x = 0; x < a.toString().length(); x++) {
		    if (a.toString().charAt(x) != b.toString().charAt(x)) {
		        y += 1;
		    }
		}
		return y;
	}
	
	
	private void calculateModels(PLWithKnowledgeBeliefbase beliefbase){
		Set<PropositionalFormula> knowledge = new HashSet<PropositionalFormula>(beliefbase.getKnowledge());
		
		Sat4jEntailment test = new Sat4jEntailment();

		Collection<Proposition> signature = (Collection<Proposition>) beliefbase.getSignature();
		Set<NicePossibleWorld> worlds = NicePossibleWorld.getAllPossibleWorlds(signature);
		Set<NicePossibleWorld> satisfyingWorlds = new HashSet<NicePossibleWorld>();
		
		//compute the models/worlds for the nonupdatabale formulas
		for(NicePossibleWorld world: worlds){
			if(world.satisfies(knowledge)){
				satisfyingWorlds.add(world);
			}
		}
		
		//the set of formulas from context which are consistent with the nonupdatable formulas
		LinkedList<PropositionalFormula> consitent = new LinkedList<PropositionalFormula>();
		
		Set<PropositionalFormula> testFormulaSet;
		
		for(PropositionalFormula formula : beliefbase.getAssertions()){
			testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
			testFormulaSet.add(formula);
			if(test.isConsistent(testFormulaSet)){
				consitent.addLast(formula);
			}
		}
		
		Set<NicePossibleWorld> contextModels = new HashSet<NicePossibleWorld>();
		
		//Update the models/worlds of the nonupdatable formulas step by step with the consitent context formulas
		
		if(consitent.isEmpty()){
			contextModels = new HashSet<NicePossibleWorld>(satisfyingWorlds);
		}else{
			PropositionalFormula form = consitent.poll();
			//testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
			//testFormulaSet.add(form);
			for(NicePossibleWorld world: satisfyingWorlds){	
				if(world.satisfies(form)){
					contextModels.add(world);
					
				}
			}
			if(!consitent.isEmpty()){
				for(PropositionalFormula formula: consitent){
					Set<NicePossibleWorld> iterator = new HashSet<NicePossibleWorld>(contextModels);
					contextModels.clear();
					//testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
					//testFormulaSet.add(formula);
					for(NicePossibleWorld world: iterator){	
						if(world.satisfies(formula)){
							contextModels.add(world);
							
						}else{
							int nearest = Integer.MAX_VALUE;
							Set<NicePossibleWorld> possibleNewModels = new HashSet<NicePossibleWorld>();
							for(NicePossibleWorld a: satisfyingWorlds){
								if(!a.equals(world) && a.satisfies(formula)){
									int i = distnace(a, world);
									if(i == nearest){
										possibleNewModels.add(a);
									}else if(i < nearest){
										//nearer world found delete others
										nearest = distnace(a, world);
										possibleNewModels.clear();
										possibleNewModels.add(a);
									}
								}
							}
							contextModels.addAll(possibleNewModels);
						}
					}
				}
			}
		}
		
		beliefbaseModels = new ModelTupel(satisfyingWorlds, contextModels);
	}
	
	private void calculateModels(ReasonerParameter params){
		calculateModels((PLWithKnowledgeBeliefbase) params.getBeliefBase());
		
	}
	
	public ModelTupel getModels(PLWithKnowledgeBeliefbase view){
		this.calculateModels(view);
		return this.beliefbaseModels;
	}
}
