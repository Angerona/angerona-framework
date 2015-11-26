package com.github.kreaturesfw.gui.view;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.gui.component.OperatorConfig;
import com.github.kreaturesfw.gui.component.OperatorConfigController;
import com.github.kreaturesfw.gui.component.OperatorConfigPanel;

import bibliothek.gui.dock.DefaultDockable;
import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * Generic ui view to show a belief base. It shows its content in a list
 * and uses the generic base class ListViewColored
 * 
 * @author Tim Janus
 */
@SuppressWarnings("deprecation")
public class BeliefbaseView extends ListViewColored {
	
	private static final long serialVersionUID = 6749306899450503411L;
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
			return bb.getAtomsAsStringList();
		}
		
		return null;
	}

	@Override
	public void setObservedEntity(Entity bb) {
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
		BaseBeliefbase bb = (BaseBeliefbase)ref;
		String title = bb.getAgent().getName();
		Beliefs bel = bb.getAgent().getBeliefs();
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
