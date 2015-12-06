package com.github.kreaturesfw.gui.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NavigationControl extends JPanel implements ActionListener {
	
	/** kill warning */
	private static final long serialVersionUID = -3092618593282534358L;

	private int min = 1;
	
	private int max;
	
	private int index;
	
	private JLabel desc;
		
	private JLabel number;
		
	private JButton forward;
	
	private JButton backward;
	
	private List<ActionListener> listeners = new LinkedList<ActionListener>();
	
	public NavigationControl() {
		desc = new JLabel("Description:");
		backward = new JButton("<-");
		backward.setActionCommand("backward");
		backward.addActionListener(this);
		
		number = new JLabel("0/0");
		forward = new JButton("->");
		forward.setActionCommand("forward");
		forward.addActionListener(this);
		
		add(desc);
		add(backward);
		add(number);
		add(forward);
	}
	
	public NavigationControl(String description) {
		this();
		setDescription(description);
	}
	
	public void setDescription(String text) {
		desc.setText(text);
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(ActionListener listener) {
		return listeners.remove(listener);
	}
	
	public void removeAllListeners() {
		listeners.clear();
	}
	
	public void setIndex (int index) {
		if(index < min || index > max)
			throw new IllegalArgumentException("index must be between 1 and max");
		this.index = index;
		updateControls();
	}
	
	public void setMin(int min) {
		if(min > index) {
			throw new IllegalArgumentException("min must be <= index");
		}
		this.min = min;
	}
	
	public void setMax(int max) {
		if(max < index) {
			throw new IllegalArgumentException("max must be >= index");
		}
		this.max = max;
		updateControls();
	}
	
	public void setNumbers(int index, int max) {
		if(index < min || index > max)
			throw new IllegalArgumentException("index must be between min="+min+" and max="+max);
		
		this.index = index;
		this.max = max;
		updateControls();
	}
	
	public void set(int min, int index, int max) {
		this.min = min;
		setNumbers(index, max);
	}
	
	private void updateControls() {
		number.setText(index + "/" + max);
		forward.setEnabled(index != max);
		backward.setEnabled(index != min);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// just delegate
		for(ActionListener listener : listeners) {
			listener.actionPerformed(e);
		}
	}
}