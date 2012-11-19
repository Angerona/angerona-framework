package angerona.fw.gui.view;

import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.internal.Entity;
import angerona.fw.internal.IdGenerator;

/**
 * Generic ui view to show a belief base. It shows its content in a list
 * and uses the generic base class ListViewColored
 * 
 * @author Tim Janus
 */
public class BeliefbaseView extends ListViewColored<BaseBeliefbase> {
	
	/** kill warning */
	private static final long serialVersionUID = -3706152280500718930L;
	
	
	@Override
	public void init() {
		super.init();
		
		if(! (ref instanceof BaseBeliefbase) )
			throw new IllegalArgumentException("Cannot init Beliefbase View with an " +
					"Entity which is not a subclass of BaseBeliefbase: " 
					+ ref.getClass().getName());
		
		Agent ag = (Agent)IdGenerator.getEntityWithId(this.ref.getParent());
		String postfix = "";
		if(ag.getBeliefs().getWorldKnowledge() == ref) {
			postfix = "World";
		} else {
			for(String key : ag.getBeliefs().getViewKnowledge().keySet()) {
				BaseBeliefbase bb = ag.getBeliefs().getViewKnowledge().get(key);
				if(bb == ref) {
					postfix = "View->"+key;
					break;
				}
			}
		}
		setTitle(ag.getName() + " - " + postfix);
	}
	
	@Override
	protected void update(DefaultListModel<ListElement> model) {
		// prepare changeset in model and so on.
		super.update(model);
		
		updateInferenceOutput(model);
	}

	/**  
	 * Helper method: Updates the inference output and adds the output to the
	 * list model.
	 * @param model	Reference to the list model.
	 */
	protected void updateInferenceOutput(DefaultListModel<ListElement> model) {
		// add a placeholder and then show the inference result:
		BaseBeliefbase bAct = (BaseBeliefbase)actual;
		BaseBeliefbase bPrev = (BaseBeliefbase)previous;
		model.addElement(new ListElement(" ", ListElement.ST_NOTCHANGED));
		model.addElement(new ListElement("--- Inference Result using: " + 
		bAct.getReasoningOperator().getNameAndParameters(), ListElement.ST_NOTCHANGED));
		
		// Calculate the inference of the reasoning.
		Set<FolFormula> inferenceAct = bAct.infere();
		Set<FolFormula> inferenceOld = bPrev == null ? null : bPrev.infere();
		
		// TODO: Ordering
		for(FolFormula f : inferenceAct) {
			if(inferenceOld != null && !inferenceOld.contains(f)) {
				model.addElement(new ListElement(f.toString(), ListElement.ST_NEW));
			} else {
				model.addElement(new ListElement(f.toString(), ListElement.ST_NOTCHANGED));
			}
		}
		
		if(inferenceOld != null) {
			for(FolFormula f : inferenceOld) {
				if(!inferenceAct.contains(f)) {
					model.addElement(new ListElement(f.toString(), ListElement.ST_DELETED));
				}
			}
		}
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return BaseBeliefbase.class;
	}

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof BaseBeliefbase) {
			BaseBeliefbase bb = (BaseBeliefbase)obj;
			return bb.getAtoms();
		}
		
		return null;
	}

	@Override
	public void setObservationObject(Object obj) {
		if(! (obj instanceof BaseBeliefbase)) {
			throw new IllegalArgumentException("Observation Object must be of type '" +  BaseBeliefbase.class.getSimpleName() + "'");
		}
		this.ref = (BaseBeliefbase)obj;
		this.actual = this.ref;
	}
	
}
