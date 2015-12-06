package com.github.kreaturesfw.gui.interactive;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.github.kreaturesfw.gui.base.ObservingPanel;

/**
 * An interactive View where the user can type actions for the agents. 
 * The user can choose the sender, the receiver and the actiontype for his action.
 * @author Pia Wierzoch
 */
public class InteractiveBar extends ObservingPanel {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	
	private JFrame frame;
	private JButton btnAction, btnFin;	
	private JSplitPane spnSplitPane;
	private JComboBox<String> actionType, receiver;
	private JTextField field;
	private JPanel pnLeft, pnRight, pnRightTop, typeTop, typeBottom, receiverTop, receiverBottom, fieldTop, fieldBottom;
	private JLabel type, rec, act;
	
	/** Default Ctor: Creating the Widget hierarchy */
	public InteractiveBar(String[] receiv, String[] actTypes) {
		setLayout(new BorderLayout());
		spnSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.add(spnSplitPane, BorderLayout.CENTER);
		
		pnLeft = new JPanel();
		pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.PAGE_AXIS));
		
		JPanel buttonPanel = new JPanel();
		btnAction = new JButton();
		btnAction.setText("Action");
		btnAction.setMinimumSize(new Dimension(100, 30));
		btnFin = new JButton();
		btnFin.setText("Finish");
		btnFin.setMinimumSize(new Dimension(100, 30));
		buttonPanel.add(btnAction);
		buttonPanel.add(btnFin);
		
		pnLeft.add(btnAction);
		pnLeft.add(btnFin);
		
		spnSplitPane.setLeftComponent(pnLeft);
		
		
		pnRight = new JPanel();
		pnRight.setLayout(new BoxLayout(pnRight, BoxLayout.PAGE_AXIS));
		pnRightTop = new JPanel();
		pnRightTop.setLayout(new BoxLayout(pnRightTop, BoxLayout.LINE_AXIS));
		
		actionType = new JComboBox<String>();
		actionType.setModel(new DefaultComboBoxModel<String>(actTypes));
		actionType.setName("actionType");
		
		type = new JLabel();
		type.setText("Actiontype");
		
		typeBottom = new JPanel();
		typeBottom.setLayout(new BoxLayout(typeBottom, BoxLayout.LINE_AXIS));
		typeTop = new JPanel();
		typeTop.setLayout(new BoxLayout(typeTop, BoxLayout.PAGE_AXIS));
		
		typeTop.add(type);
		typeBottom.add(typeTop);
		typeTop.add(actionType);
		
		pnRightTop.add(typeTop);
		
		receiver = new JComboBox<String>();
		receiver.setModel(new DefaultComboBoxModel<String>(receiv));
		receiver.setName("receiver");
		
		rec = new JLabel();
		rec.setText("Receiver");
		
		receiverBottom = new JPanel();
		receiverBottom.setLayout(new BoxLayout(receiverBottom, BoxLayout.LINE_AXIS));
		receiverTop = new JPanel();
		receiverTop.setLayout(new BoxLayout(receiverTop, BoxLayout.PAGE_AXIS));
		
		receiverTop.add(rec);
		receiverBottom.add(receiverTop);
		receiverTop.add(receiver);

		pnRightTop.add(receiverTop);
		
//		sender = new JComboBox<String>();
//		sender.setModel(new DefaultComboBoxModel<String>(send));
//		sender.setName("sender");
//
//		sen = new JLabel();
//		sen.setText("Sender");
//		
//		senderBottom = new JPanel();
//		senderBottom.setLayout(new BoxLayout(senderBottom, BoxLayout.LINE_AXIS));
//		senderTop = new JPanel();
//		senderTop.setLayout(new BoxLayout(senderTop, BoxLayout.PAGE_AXIS));
//		
//		senderTop.add(sen);
//		senderBottom.add(senderTop);
//		senderTop.add(sender);
//		
//		pnRightTop.add(senderTop);
		
		pnRightTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, receiver.getPreferredSize().height + rec.getPreferredSize().height));
		pnRight.add(pnRightTop);
		
		pnRight.add(Box.createHorizontalGlue());
		
		act = new JLabel();
		act.setText("Action: ");
		pnRight.add(act);
		field = new JTextField();
		field.setText("");
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
		
		fieldBottom = new JPanel();
		fieldBottom.setLayout(new BoxLayout(fieldBottom, BoxLayout.LINE_AXIS));
		fieldTop = new JPanel();
		fieldTop.setLayout(new BoxLayout(fieldTop, BoxLayout.PAGE_AXIS));

		fieldTop.add(act);
		fieldBottom.add(fieldTop);
		fieldBottom.add(field);
		
		pnRight.add(fieldBottom);
		
		spnSplitPane.setRightComponent(pnRight);
		
		frame = new JFrame("Interactive Agent");
	    frame.setSize(500, 300);
		frame.add(spnSplitPane);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public JButton getActionButton() {
		return btnAction;
	}
	
	public JButton getFinButton() {
		return btnFin;
	}
	
	public JTextField getTextField() {
		return field;
	}
	
	public JComboBox<String> getReceiver() {
		return receiver;
	}
	
	public JComboBox<String> getActionType() {
		return actionType;
	}
	
	public JFrame getFrame(){
		return frame;
	}

}
