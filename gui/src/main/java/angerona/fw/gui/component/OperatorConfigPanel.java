package angerona.fw.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.Angerona;
import angerona.fw.BaseOperator;
import angerona.fw.OperatorSet;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.serialize.GlobalConfiguration;

public class OperatorConfigPanel<T extends BaseOperator> extends JPanel implements PropertyChangeListener {

	/** kill warning */
	private static final long serialVersionUID = 782532085136023725L;
	
	private JList lstParamters;
	
	private JTextField txtParameterName;
	
	private JTextField txtParameterValue;
	
	private OperatorConfigController<T> controller;
	
	public OperatorConfigPanel(OperatorConfigController<T> controller) {
		if(controller == null)
			throw new IllegalArgumentException();
		this.controller = controller;
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		
		JButton resetOperator = new JButton("Reset Operator");
		resetOperator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.toString();
			}
		});
		
		JButton resetParameters = new JButton("Reset Parameters");
		
		// Top line containining reset buttons
		JPanel defaultPanel = new JPanel();
		defaultPanel.add(resetOperator);
		defaultPanel.add(resetParameters);
		add(defaultPanel, BorderLayout.NORTH);
		
		// the center contains a panel with a checkbox for selecting the operator
		// Below the checkbox is a list of parameters and below that a check
		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new BorderLayout());
		
		JPanel buttonPanel = new JPanel();
		
		JLabel lblParameterName = new JLabel("Parameter:");
		txtParameterName = new JTextField();
		JLabel lblParameterValue = new JLabel("Value:");
		txtParameterValue = new JTextField();
		JButton add = new JButton("add");
		JButton edit = new JButton("edit");
		JButton remove = new JButton("Remove");
		
		
		buttonPanel.add(lblParameterName);
		buttonPanel.add(txtParameterName);
		buttonPanel.add(lblParameterValue);
		buttonPanel.add(txtParameterValue);
		buttonPanel.add(add);
		buttonPanel.add(edit);
		buttonPanel.add(remove);
		
		parameterPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("parameters")) {
			
		} else if(evt.getPropertyName().equals("selectedOperator")) {
			
		}
	}
}
