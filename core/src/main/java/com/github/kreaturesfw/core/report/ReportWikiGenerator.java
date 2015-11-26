package com.github.kreaturesfw.core.report;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.legacy.Agent;

public class ReportWikiGenerator extends BaseReportOutputGenerator<String> {

	private boolean showAttachments = false;
	
	private boolean simulationForwared = false;
	
	private int number = 1;
	
	private String output = "";
	
	private String oldOutput;
	
	private String tableHead = "|| No || Poster ||= Message =||\n";
	
	private List<PropertyChangeListener> listeners = new LinkedList<PropertyChangeListener>();
	
	public void showAttachments(boolean show) {
		this.showAttachments = show;
	}
	
	@Override
	public void reportReceived(ReportEntry entry) {
		oldOutput = output;
		super.reportReceived(entry);
		onPropertyChange(oldOutput, output);
	}
	
	@Override
	protected void handleEntry(ReportEntry entry) {
		if(simulationForwared) {
			output += "\n\n=== Tick: " + String.valueOf(tick) + " - " + actAgent.getName() + " ===";
			output += "\n"+tableHead;
			simulationForwared = false;
		}
		output += "|| " + String.valueOf(number++) + " || ";
		output += entry.getPosterName() + " || ";
		output += entry.getMessage() + " ||\n";
		
	}

	@Override
	protected void handleAttachment(Entity attachment) {
		if(!showAttachments)
			return;
		output += "\n\n";
		output += attachment.toString();
		output += "\n\n" + tableHead;
	}

	@Override
	protected void handleTickChange(int tick) {
		simulationForwared = true;
	}
	
	@Override
	protected void handleAgentChange(Agent agent) {
		simulationForwared = true;
	}
	
	@Override
	public String getOutput() {
		return output.trim();
	}
	
	private void onPropertyChange(String vOld, String vNew) {
		for(PropertyChangeListener listener : listeners) {
			listener.propertyChange(new PropertyChangeEvent(
					this, "output", vOld, vNew));
		}
	}

	public void addPropertyListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public boolean removePropertyListener(PropertyChangeListener listener) {
		return listeners.remove(listener);
	}
	
	public void removeAllPropertyListeners() {
		listeners.clear();
	}
}
