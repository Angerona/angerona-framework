package angerona.fw.conditional.gui;

import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import angerona.fw.gui.base.View;

/**

 */
public interface OCFView extends View {
	AbstractButton getCalcButton();
	
	JTextArea getBeliefBaseTextArea();
	
	JTextField getFilterTextField();
	
	JTable getTable();
	

}
