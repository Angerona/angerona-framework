package com.github.angerona.fw.motivation.island;

import static com.github.angerona.fw.motivation.island.enums.Weather.RAIN;
import static com.github.angerona.fw.motivation.island.enums.Weather.STORM;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.logic.asp.AspBeliefbase;
import com.github.angerona.fw.logic.asp.AspTranslator;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandTranslator extends AspTranslator {

	@Override
	protected AspBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception p) {
		AspBeliefbase reval = new AspBeliefbase();
		Set<FolFormula> formulas = new HashSet<FolFormula>();

		if (p instanceof IslandPerception) {
			IslandPerception ip = (IslandPerception) p;

			formulas.add(create(ip.getCurrentLocation().toString()));
			formulas.add(create(ip.getEnergyLevel().toString()));

			if (ip.getCurrentWeather() == STORM || ip.getCurrentWeather() == RAIN) {
				formulas.add(create(STORM + "_OR_" + RAIN));
			} else {
				formulas.add(create(ip.getCurrentWeather().toString()));
			}

			if (ip.getWeatherPrediction() == STORM || ip.getWeatherPrediction() == RAIN) {
				formulas.add(create("PRE_" + STORM + "_OR_" + RAIN));
			} else {
				formulas.add(create("PRE_" + ip.getWeatherPrediction()));
			}

			//reval.addKnowledge(formulas);
		}

		return (AspBeliefbase)translateFOL(caller, formulas);

	}

	private FolFormula create(String str) {
		return new FOLAtom(new Predicate(str.toLowerCase()));
	}

}
