package angerona.fw.gui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import angerona.fw.BaseOperator;
import angerona.fw.util.Pair;

public class OperatorConfigPanel<T extends BaseOperator> extends JPanel implements PropertyChangeListener {

	/** kill warning */
	private static final long serialVersionUID = 782532085136023725L;
	
	private DefaultListModel<Pair<String, String>> paramModel;
	
	private JComboBox<T> cbOperators;
	
	private DefaultComboBoxModel<T> cbmOperators;
	
	private JTextField txtParameterName;
	
	private JTextField txtParameterValue;
	
	private OperatorConfigController<T> controller;
	
	public OperatorConfigPanel(OperatorConfigController<T> controller) {
		if(controller == null)
			throw new IllegalArgumentException();
		this.controller = controller;
	}
	
	public void init(OperatorConfig<T> model) {
		this.setLayout(new BorderLayout());
		
		JButton resetOperator = new JButton("Reset Operator");
		resetOperator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.toString();
			}
		});
		
		JButton resetParameters = new JButton("Reset Parameters");
		
		// Top line containing reset buttons
		JPanel defaultPanel = new JPanel();
		defaultPanel.add(resetOperator);
		defaultPanel.add(resetParameters);
		add(defaultPanel, BorderLayout.NORTH);
		
		// the center contains a panel with a checkbox for selecting the operator
		// Below the checkbox is a list of parameters and below that a check
		JPanel parameterPanel = new JPanel();
		parameterPanel.setLayout(new BorderLayout());
		
		JPanel operatorBoxPanel = new JPanel();
		JLabel lblOp = new JLabel("Operator:");
		operatorBoxPanel.add(lblOp);
		
		cbmOperators = new DefaultComboBoxModel<>();
		cbOperators = new JComboBox<T>(cbmOperators);
		cbOperators.setPreferredSize(new Dimension(300, 30));
		operatorBoxPanel.add(cbOperators);
		parameterPanel.add(operatorBoxPanel, BorderLayout.NORTH);
		
		paramModel = new DefaultListModel<>();
		JList<Pair<String, String>> lstParamters = new JList<>(paramModel);
		lstParamters.setMinimumSize(new Dimension(100, 100));
		parameterPanel.add(lstParamters, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JLabel lblParameterName = new JLabel("Parameter:");
		txtParameterName = new JTextField("", 20);
		JLabel lblParameterValue = new JLabel("Value:");
		txtParameterValue = new JTextField("", 20);
		JButton change = new JButton("Add/Change");
		JButton remove = new JButton("Remove");
		
		bottomPanel.add(lblParameterName);
		bottomPanel.add(txtParameterName);
		bottomPanel.add(lblParameterValue);
		bottomPanel.add(txtParameterValue);
		bottomPanel.add(change);
		bottomPanel.add(remove);
		
		parameterPanel.add(bottomPanel, BorderLayout.SOUTH);
		add(parameterPanel, BorderLayout.CENTER);
		
		linkModel(model);
	}

	private void linkModel(OperatorConfig<T> model) {
		for(T op : model.getSelectableOperators()) {
			cbmOperators.addElement(op);
		}
		cbmOperators.setSelectedItem(model.getDefaultOperator());
		
		refillParameterList(model.getParameters());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object newValue = evt.getNewValue();
		if(evt.getPropertyName().equals("parameters")) {
			Map<String, String> params = (Map<String, String>)newValue;
			refillParameterList(params);
			
		} else if(evt.getPropertyName().equals("selectedOperator")) {
			
		}
	}

	private void refillParameterList(Map<String, String> params) {
		paramModel.clear();
		for(String key : params.keySet()) {
			paramModel.addElement(new Pair<>(key, params.get(key)));
		}
	}
}
