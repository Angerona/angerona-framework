package angerona.fw.gui.asp;

import java.util.List;

import javax.swing.DefaultListModel;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import angerona.fw.gui.view.BeliefbaseView;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspReasoner;

public class AspBeliefbaseView extends BeliefbaseView {

	/** kill warning */
	private static final long serialVersionUID = -9197437532805094834L;

	@Override
	public Class<?> getObservationObjectType() {
		return AspBeliefbase.class;
	}
	
	@Override
	protected void update(DefaultListModel<ListElement> model) {
		if(ref == null)	return;
		
		
		updateBeliefbaseOutput(model);
		
		AspBeliefbase bAct = (AspBeliefbase)actual;
		if(! (bAct.getReasoningOperator() instanceof AspReasoner)) {
			return;
		}
		AspReasoner reasoner = (AspReasoner)bAct.getReasoningOperator();

		model.addElement(new ListElement(" ", ListElement.ST_NOTCHANGED));
		model.addElement(new ListElement("--- Answerset Result using: " + 
		reasoner.getNameAndParameters(), ListElement.ST_NOTCHANGED));
		
		List<AnswerSet> answerSets = reasoner.processAnswerSets(bAct);
		
		int counter = 1;
		for(AnswerSet as : answerSets) {
			model.addElement(new ListElement("Answer Set " + counter + "/" + answerSets.size(), 
				ListElement.ST_NOTCHANGED));
			
			String output = "";
			for(Literal l : as.getLiterals()) {
				output += ", " + l;
				if(output.length() > 100) {
					output = output.substring(2);
					model.addElement(new ListElement(output, ListElement.ST_NOTCHANGED));
				}
			}
			if(output.length() != 0) {
				output = output.substring(2);
				model.addElement(new ListElement(output, ListElement.ST_NOTCHANGED));
			}
			
			counter += 1;
			if(counter <= answerSets.size()) {
				model.addElement(new ListElement(" ", ListElement.ST_NOTCHANGED));
			}
		}
		
		
		updateInferenceOutput(model);
	}
}
