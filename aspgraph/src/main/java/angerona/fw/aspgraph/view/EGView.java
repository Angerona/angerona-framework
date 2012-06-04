package angerona.fw.aspgraph.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;


public class EGView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142473502907874961L;

	private JPanel graphPanel;
	public EGView(){
		initComponents();
	}
	
	private void initComponents(){
		JLabel aspLabel = new JLabel("Answer Set:");
		JComboBox<AnswerSet> answerSetsBox = new JComboBox<AnswerSet>();
		JLabel assumptionLabel = new JLabel("Assumption:");
		JComboBox<Atom> assumptionsBox = new JComboBox<Atom>();
		JLabel literalLabel = new JLabel("Literal:");
		JComboBox<Atom> literalsBox = new JComboBox<Atom>();
		graphPanel = new JPanel();
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
		selectionPanel.add(aspLabel);
		selectionPanel.add(answerSetsBox);
		selectionPanel.add(assumptionLabel);
		selectionPanel.add(assumptionsBox);
		selectionPanel.add(literalLabel);
		selectionPanel.add(literalsBox);

		add(graphPanel, BorderLayout.CENTER);
		add(selectionPanel, BorderLayout.LINE_END);
	}
}
