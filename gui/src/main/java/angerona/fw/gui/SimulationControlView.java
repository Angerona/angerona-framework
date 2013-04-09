package angerona.fw.gui;

import java.beans.PropertyChangeListener;

import javax.swing.JButton;

public interface SimulationControlView extends PropertyChangeListener {
	JButton getSimStateButton();
	
	JButton getLoadButton();
}
