package com.github.angerona.knowhow.gui;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

import com.github.angerona.fw.gui.base.EntityViewComponent;
import com.github.angerona.fw.report.ReportEntry;
import com.github.angerona.fw.report.ReportListener;
import com.github.angerona.fw.util.Pair;
import com.github.angerona.knowhow.KnowhowBase;
import com.github.angerona.knowhow.KnowhowBuilder;
import com.github.angerona.knowhow.KnowhowStatement;
import com.github.angerona.knowhow.SkillParameter;

/**
 * A UI-Component responsible to show the KnowhowBase of an agent.
 * @author Tim Janus
 *
 */
public class KnowhowView extends EntityViewComponent 
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
		this.add(statementList, BorderLayout.CENTER);
		
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
				KnowhowBuilder.buildKnowhowbaseProgram(actual);
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
