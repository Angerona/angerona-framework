package angerona.fw.gui.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import angerona.fw.operators.BaseOperator;
import angerona.fw.util.Pair;

public class OperatorConfigPanel extends JPanel implements PropertyChangeListener {

	/** kill warning */
	private static final long serialVersionUID = 782532085136023725L;
	
	private DefaultListModel<Pair<String, String>> paramModel;
	
	private JComboBox<BaseOperator> cbOperators;
	
	private DefaultComboBoxModel<BaseOperator> cbmOperators;
	
	private JTextField txtParameterName;
	
	private JTextField txtParameterValue;
	
	private OperatorConfigController controller;
	
	public OperatorConfigPanel(OperatorConfigController controller) {
		if(controller == null)
			throw new IllegalArgumentException();
		this.controller = controller;
	}
	
	public void init(OperatorConfig model) {
		this.setLayout(new BorderLayout());
		
		JButton resetOperator = new JButton("Reset Operator");
		resetOperator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.resetOperator();
			}
		});
		
		JButton resetParameters = new JButton("Reset Parameters");
		resetParameters.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetParameters();
			}
		});
		
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
		cbOperators = new JComboBox<BaseOperator>(cbmOperators);
		cbOperators.setPreferredSize(new Dimension(300, 30));
		cbOperators.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					controller.selectOperator( ((BaseOperator)e.getItem()).toString());
				}
			}
		});
		
		operatorBoxPanel.add(cbOperators);
		parameterPanel.add(operatorBoxPanel, BorderLayout.NORTH);
		
		paramModel = new DefaultListModel<>();
		final JList<Pair<String, String>> lstParamters = new JList<>(paramModel);
		lstParamters.setMinimumSize(new Dimension(100, 100));
		parameterPanel.add(lstParamters, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JLabel lblParameterName = new JLabel("Parameter:");
		txtParameterName = new JTextField("", 20);
		JLabel lblParameterValue = new JLabel("Value:");
		txtParameterValue = new JTextField("", 20);
		JButton change = new JButton("Add/Change");
		change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeParameter(txtParameterName.getText(),
						txtParameterValue.getText());
			}
		});
		
		JButton remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(lstParamters.getSelectedValue() != null) {
					controller.removeParameter(lstParamters.getSelectedValue().first);
				} else {
					controller.removeParameter(txtParameterName.getText());
				}
			}
		});
		
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

	private void linkModel(OperatorConfig model) {
		for(BaseOperator op : model.getSelectableOperators()) {
			cbmOperators.addElement(op);
		}
		cbmOperators.setSelectedItem(model.getDefaultOperator());
		
		refillParameterList(model.getParameters());
		model.addPropertyChangeListener(this);
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
