package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.gui.component.OperatorConfig;
import angerona.fw.gui.component.OperatorConfigController;
import angerona.fw.gui.component.OperatorConfigPanel;
import angerona.fw.internal.Entity;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.Beliefs;
import bibliothek.gui.dock.DefaultDockable;

/**
 * Generic ui view to show a belief base. It shows its content in a list
 * and uses the generic base class ListViewColored
 * 
 * @author Tim Janus
 */
public class BeliefbaseView extends ListViewColored<BaseBeliefbase> {
	
	private OperatorConfig opConfig;
	
	@Override
	public void init() {
		super.init();
		
		if(! (ref instanceof BaseBeliefbase) )
			throw new IllegalArgumentException("Cannot init Beliefbase View with an " +
					"Entity which is not a subclass of BaseBeliefbase: " 
					+ ref.getClass().getName());
		BaseBeliefbase refBeliefBase = (BaseBeliefbase)ref;
		opConfig = new OperatorConfig(refBeliefBase.getOperators().getOperationSetByType(BaseReasoner.OPERATION_TYPE));

		/*
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
		*/
	}
	
	@Override
	protected void update(DefaultListModel<ListElement> model) {
		BaseBeliefbase bb = (BaseBeliefbase)actual;
		opConfig = new OperatorConfig (bb.getOperators().getOperationSetByType(BaseReasoner.OPERATION_TYPE));
		
		// prepare changeset in model and so on.
		super.update(model);
		
		updateInferenceOutput(model);
	}

	/**  
	 * Helper method: Updates the inference output and adds the output to the
	 * list model.
	 * @param model	Reference to the list model.
	 * @todo ordering of the lists of FolFormula
	 */
	protected void updateInferenceOutput(DefaultListModel<ListElement> model) {
		// add a placeholder and then show the inference result:
		BaseBeliefbase bAct = (BaseBeliefbase)actual;
		BaseBeliefbase bPrev = (BaseBeliefbase)previous;
		model.addElement(new ListElement(" ", ListElement.ST_NOTCHANGED));
		model.addElement(new ListElement("--- Inference Result using: " + 
		bAct.getReasoningOperator().toString(), ListElement.ST_RESERVED));
		
		// Calculate the inference of the reasoning.
		Set<FolFormula> inferenceAct = bAct.infere();
		Set<FolFormula> inferenceOld = bPrev == null ? null : bPrev.infere();
		
		for(FolFormula f : inferenceAct) {
			if(inferenceOld != null && !inferenceOld.contains(f)) {
				model.addElement(new ListElement(f.toString(), ListElement.ST_NEW));
			} else {
				model.addElement(new ListElement(f.toString(), ListElement.ST_RESERVED));
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
	protected void onElementClicked(int index, int status) {
		if(status == ListElement.ST_RESERVED) {
			OperatorConfigController controller = new OperatorConfigController(opConfig);
			OperatorConfigPanel opPanel = new OperatorConfigPanel(controller);
			opPanel.init(opConfig);
			
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.getContentPane().add(opPanel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
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
	public void setObservedEntity(BaseBeliefbase bb) {
		this.ref = bb;
		this.actual = bb;
		this.previous = null;
	}

	@Override
	public Class<? extends BaseBeliefbase> getObservedType() {
		return BaseBeliefbase.class;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		super.decorate(dockable);
		String title = ref.getAgent().getName();
		Beliefs bel = ref.getAgent().getBeliefs();
		if(ref == bel.getWorldKnowledge()) {
			title += " - World";
		} else {
			for(String otherName : bel.getViewKnowledge().keySet()) {
				if(bel.getViewKnowledge().get(otherName) == ref) {
					title += " - View->" + otherName;
					break;
				}
			}
		}
		dockable.setTitleText(title);
	}
}
