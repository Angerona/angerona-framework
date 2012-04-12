package angerona.fw.knowhow.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import angerona.fw.gui.UIComponent;
import angerona.fw.knowhow.KnowhowBase;
import angerona.fw.knowhow.KnowhowBuilder;
import angerona.fw.knowhow.KnowhowStatement;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

/**
 * A UI-Component responsible to show the KnowhowBase of an agent.
 * @author Tim Janus
 *
 */
public class KnowhowUIComponent extends UIComponent implements ReportListener{

	/** */
	private static final long serialVersionUID = -4168568552100892570L;

	private DefaultListModel<String> stmtListModel = new DefaultListModel<String>();
	
	private KnowhowBase source;
	
	private KnowhowBase actual;
	
	@Override
	public void init() {
		super.init();
		
		this.setLayout(new BorderLayout());
		JList<String> statementList = new JList<String>();
		statementList.setModel(stmtListModel);
		this.add(statementList, BorderLayout.CENTER);
		
		actual = source;
		updateView();
	}
	
	@Override
	public String getComponentTypeName() {
		// TODO Auto-generated method stub
		return "KnowHow";
	}

	@Override
	public void setObservationObject(Object obj) {
		if(!(obj instanceof KnowhowBase)) {
			throw new IllegalArgumentException();
		}
		
		source = (KnowhowBase)obj;
	}
	
	@Override 
	public Class<?> getObservationObjectType() {
		return KnowhowBase.class;
	}

	public void reportReceived(ReportEntry entry) {
		if(entry.getAttachment() != null && 
			entry.getAttachment().getGUID().equals(source.getGUID())) {
			actual = (KnowhowBase)entry.getAttachment();
			updateView();
		}
	}
	
	private void updateView() {
		stmtListModel.clear();
		for(KnowhowStatement stmt : actual.getStatements()) {
			stmtListModel.add(0, stmt.toString());
		}
		
		Program p = KnowhowBuilder.buildExtendedLogicProgram(actual);
		stmtListModel.addElement("---");
		stmtListModel.addElement("as ELP");
		stmtListModel.addElement("---");
		
		for(Rule r : p) {
			stmtListModel.addElement(r.toString());
		}
	}
}
