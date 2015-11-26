package com.github.kreaturesfw.plwithknowledge.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.parser.ParseException;
import com.github.kreaturesfw.core.util.Pair;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.Sat4jEntailment;
import net.sf.tweety.logics.pl.semantics.NicePossibleWorld;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

public class Tester {
	
	public static String rawbbase = "!Mx || L1  \n !My || L2 \n (L1 && !L2) || (!L1 && L2) \n (Mx && !My) || (!Mx && My) \n !My || P \n ; \n  L1 \n Mx";
	
	private static ModelTupel beliefbaseModels;
	
	public static void main(String[] args){
		
		PLWithKnowledgeBeliefbase bbase = new PLWithKnowledgeBeliefbase();
		try {
			bbase.parse(new BufferedReader(new StringReader(rawbbase)));
		} catch (ParseException e) {
			System.out.println("ParseException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
				
		
		System.out.println("Initial beliefbase:");
		System.out.println(bbase.toString());
		
		System.out.println("Models:");
		Set<FolFormula> formulas = inferImpl(bbase);
		System.out.println(beliefbaseModels);
		System.out.println("infrerence: " + formulas);
		
		PropositionalFormula form = new Proposition("My");
		bbase.getAssertions().addLast(form);
		
		System.out.println("Models:");
		formulas = inferImpl(bbase);
		System.out.println(beliefbaseModels);
		System.out.println("infrerence: " + formulas);
		
		FolFormula query = new FOLAtom(new Predicate("Mx"));
		System.out.println("Query: " + query);
		Pair<Set<FolFormula>, AngeronaAnswer> answer = queryImpl(bbase, query);
		System.out.println("Answer: " + answer);
		
	}
	
	
	protected static Set<FolFormula> inferImpl(PLWithKnowledgeBeliefbase params) {
		//calculate the models of the beliefbase
		calculateModels(params);
		
		
		@SuppressWarnings("unchecked")
		Collection<Proposition> propositions = (Collection<Proposition>) params.getSignature();
	
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
	
	protected static Pair<Set<FolFormula>, AngeronaAnswer> queryImpl(
			PLWithKnowledgeBeliefbase params, FolFormula query) {
		//calculate the Models of the beliefbase
		calculateModels(params);
		
		AnswerValue answer = AnswerValue.AV_FALSE;
		
		FOLPropTranslator translator = new FOLPropTranslator();
		
		boolean satisfy = true;
		int i = 0;
		for(NicePossibleWorld a : beliefbaseModels.getModels()){
			if(!a.satisfies(translator.toPropositional(query))){
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
		return new Pair<Set<FolFormula>, AngeronaAnswer>(answers, new AngeronaAnswer(query, answer));
	}
	
	private static void calculateModels(PLWithKnowledgeBeliefbase beliefbase){
		Set<PropositionalFormula> knowledge = new HashSet<PropositionalFormula>(beliefbase.getKnowledge());
		
		Sat4jEntailment test = new Sat4jEntailment();

		@SuppressWarnings("unchecked")
		Collection<Proposition> signature = (Collection<Proposition>) beliefbase.getSignature();
		Set<NicePossibleWorld> worlds = NicePossibleWorld.getAllPossibleWorlds(signature);
		Set<NicePossibleWorld> satisfyingWorlds = new HashSet<NicePossibleWorld>();
		
		//compute the models/worlds for the non-updatable formulas
		for(NicePossibleWorld world: worlds){
			if(world.satisfies(knowledge)){
				satisfyingWorlds.add(world);
			}
		}
		
		//the set of formulas from context which are consistent with the non-updatable formulas
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
			testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
			testFormulaSet.add(form);
			for(NicePossibleWorld world: satisfyingWorlds){	
				if(world.satisfies(testFormulaSet)){
					contextModels.add(world);
					
				}
			}
	
			if(!consitent.isEmpty()){
				for(PropositionalFormula formula: consitent){
					Set<NicePossibleWorld> iterator = new HashSet<NicePossibleWorld>(contextModels);
					contextModels.clear();
					testFormulaSet = new HashSet<PropositionalFormula>(knowledge);
					testFormulaSet.add(formula);
					for(NicePossibleWorld world: iterator){	
						if(world.satisfies(testFormulaSet)){
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
	
	private static int distnace(NicePossibleWorld a, NicePossibleWorld b){
		int y=0;
		for (int x = 0; x < a.toString().length(); x++) {
		    if (a.toString().charAt(x) != b.toString().charAt(x)) {
		        y += 1;
		    }
		}
		return y;
	}

}
