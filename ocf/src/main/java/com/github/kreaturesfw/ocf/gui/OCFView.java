package com.github.kreaturesfw.ocf.gui;

import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.kreaturesfw.gui.base.View;

/**

 */
public interface OCFView extends View {
	AbstractButton getCalcButton();
	
	JTextArea getBeliefBaseTextArea();
	
	JTextField getFilterTextField();
	
	JTable getTable();
	

}
