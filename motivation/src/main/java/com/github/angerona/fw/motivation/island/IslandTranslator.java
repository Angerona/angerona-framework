package com.github.angerona.fw.motivation.island;

import static com.github.angerona.fw.motivation.island.enums.Weather.RAIN;
import static com.github.angerona.fw.motivation.island.enums.Weather.STORM;
import static com.github.angerona.fw.motivation.utils.FormulaUtils.createFormula;
import static com.github.angerona.fw.motivation.utils.FormulaUtils.intToBoolAra;

import java.util.HashSet;
import java.util.Set;

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
		Set<FolFormula> formulas = new HashSet<FolFormula>();

		if (p instanceof IslandPerception) {
			IslandPerception ip = (IslandPerception) p;

			// evaluate location
			formulas.add(createFormula(ip.getLocation().toString()));

			// evaluate energy_value
			boolean[] energy = intToBoolAra(ip.getEnergyValue() - 1, 4);
			FolFormula energy_8 = createFormula("energy_8");
			FolFormula energy_4 = createFormula("energy_4");
			FolFormula energy_2 = createFormula("energy_2");
			FolFormula energy_1 = createFormula("energy_1");

			formulas.add(energy[0] ? energy_8 : (FolFormula) energy_8.complement());
			formulas.add(energy[1] ? energy_4 : (FolFormula) energy_4.complement());
			formulas.add(energy[2] ? energy_2 : (FolFormula) energy_2.complement());
			formulas.add(energy[3] ? energy_1 : (FolFormula) energy_1.complement());

			// evaluate weather
			if (ip.getWeather() == STORM || ip.getWeather() == RAIN) {
				formulas.add(createFormula(STORM + "_OR_" + RAIN));
			} else {
				formulas.add(createFormula(ip.getWeather().toString()));
			}

			// evaluate prediction
			if (ip.getPrediction() == STORM || ip.getPrediction() == RAIN) {
				formulas.add(createFormula("PRE_" + STORM + "_OR_" + RAIN));
			} else {
				formulas.add(createFormula("PRE_" + ip.getPrediction()));
			}

			// evaluate remaining
			boolean[] remaining = intToBoolAra(ip.getRemaining(), 2);
			FolFormula remaining_2 = createFormula("remaining_2");
			FolFormula remaining_1 = createFormula("remaining_1");

			formulas.add(remaining[0] ? remaining_2 : (FolFormula) remaining_2.complement());
			formulas.add(remaining[1] ? remaining_1 : (FolFormula) remaining_1.complement());

			// evaluate secured
			FolFormula secured = createFormula("secured");
			formulas.add(ip.isSecured() ? secured : (FolFormula) secured.complement());
		}

		return (AspBeliefbase) translateFOL(caller, formulas);

	}

}
