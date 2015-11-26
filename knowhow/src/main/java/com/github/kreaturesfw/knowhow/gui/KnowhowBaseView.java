package com.github.kreaturesfw.knowhow.gui;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.github.kreaturesfw.core.report.ReportEntry;
import com.github.kreaturesfw.core.report.ReportListener;
import com.github.kreaturesfw.core.util.Pair;
import com.github.kreaturesfw.gui.base.EntityViewComponent;
import com.github.kreaturesfw.knowhow.KnowhowBase;
import com.github.kreaturesfw.knowhow.KnowhowStatement;
import com.github.kreaturesfw.knowhow.asp.DLPBuilder;
import com.github.kreaturesfw.knowhow.parameter.SkillParameter;

import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;

/**
 * A UI-Component responsible to show the KnowhowBase of an agent.
 * @author Tim Janus
 *
 */
public class KnowhowBaseView extends EntityViewComponent 
	implements ReportListener {
	
	/** kick warning */
	private static final long serialVersionUID = -6905217402039226493L;

	private DefaultListModel<String> stmtListModel = new DefaultListModel<String>();

	private KnowhowBase actual;
	
	@Override
	public void init() {
		this.setLayout(new BorderLayout());
		JList<String> statementList = new JList<String>();
		statementList.setModel(stmtListModel);
		this.add(new JScrollPane(statementList), BorderLayout.NORTH);

		actual = (KnowhowBase)ref;
		updateView();
	}
	
	public void reportReceived(ReportEntry entry) {
		if(entry.getAttachment() != null && 
			entry.getAttachment().getGUID().equals(ref.getGUID())) {
			actual = (KnowhowBase)entry.getAttachment();
			updateView();
		}
	}
	
	private void updateView() {
		stmtListModel.clear();
		for(KnowhowStatement stmt : actual.getStatements()) {
			stmtListModel.add(0, stmt.toString());
		}
		
		Pair<Program, LinkedList<SkillParameter>> pair = 
				DLPBuilder.buildKnowhowbaseProgram(actual);
		stmtListModel.addElement("---");
		stmtListModel.addElement("parameters");
		stmtListModel.addElement("---");
		for(SkillParameter sp : pair.second) {
			stmtListModel.addElement(sp.toString());
		}
		
		stmtListModel.addElement("---");
		stmtListModel.addElement("and ELP");
		stmtListModel.addElement("---");
		
		for(Rule r : pair.first) {
			stmtListModel.addElement(r.toString());
		}
	}

	@Override
	public void cleanup() {
	}

	@Override
	public Class<? extends KnowhowBase> getObservedType() {
		return KnowhowBase.class;
	}

}
