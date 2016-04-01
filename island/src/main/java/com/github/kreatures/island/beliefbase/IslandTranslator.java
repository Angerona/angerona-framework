package com.github.kreatures.island.beliefbase;

import static com.github.kreatures.island.beliefbase.FormulaUtils.createFormula;
import static com.github.kreatures.island.beliefbase.FormulaUtils.intToBoolAra;
import static com.github.kreatures.island.enums.Location.AT_HQ;
import static com.github.kreatures.island.enums.Location.AT_SITE;
import static com.github.kreatures.island.enums.Location.IN_CAVE;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.island.data.IslandPerception;
import com.github.kreatures.core.logic.asp.AspBeliefbase;
import com.github.kreatures.core.logic.asp.AspTranslator;

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
			if (ip.getLocation() == AT_HQ || ip.getLocation() == AT_SITE || ip.getLocation() == IN_CAVE) {
				formulas.add(createFormula(ip.getLocation().toString()));
			} else {
				formulas.add(createFormula("ON_THE_WAY"));
				FolFormula step_2 = createFormula("step_2");
				FolFormula step_1 = createFormula("step_1");

				switch (ip.getLocation()) {
				case ON_THE_WAY_1:
					formulas.add((FolFormula) step_2.complement());
					formulas.add(step_1);
					break;
				case ON_THE_WAY_2:
					formulas.add(step_2);
					formulas.add((FolFormula) step_1.complement());
					break;
				case ON_THE_WAY_3:
					formulas.add(step_2);
					formulas.add(step_1);
					break;
				default: // ignore
				}
			}

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
			formulas.add(createFormula(ip.getWeather().toString()));

			// evaluate prediction
			formulas.add(createFormula("PRE_" + ip.getPrediction()));

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
