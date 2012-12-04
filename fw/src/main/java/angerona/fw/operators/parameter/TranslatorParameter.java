package angerona.fw.operators.parameter;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;

public class TranslatorParameter extends BeliefbasePluginParameter {
	private Perception percept;
	
	private Set<FolFormula> information;
	
	public TranslatorParameter() {}
	
	public TranslatorParameter(BaseBeliefbase bb, Perception p) {
		super(bb);
		this.percept = p;
	}
	
	public TranslatorParameter(BaseBeliefbase bb, Set<FolFormula> information) {
		super(bb);
		this.information = information;
	}
	
	public Perception getPerception() {
		return percept;
	}
	
	public Set<FolFormula> getInformation() {
		return information;
	}
}
