package com.github.kreaturesfw.core.report;

import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.legacy.Agent;

/**
 * Abstract base class for an output generator of Angerona reports.
 * @author Tim Janus
 *
 * @param <TOutput> The type of the output.
 */
public abstract class BaseReportOutputGenerator<TOutput> implements ReportListener {

	protected int tick = -1;
	
	protected Agent actAgent = null;
	
	@Override
	public void reportReceived(ReportEntry entry) {
		if(entry.getSimulationTick() != tick) {
			tick = entry.getSimulationTick();
			handleTickChange(tick);
		}
		
		if(entry.getScope().getAgent() != actAgent) {
			actAgent = entry.getScope().getAgent();
			handleAgentChange(actAgent);
		}
		
		handleEntry(entry);
		if(entry.getAttachment() != null) {
			handleAttachment(entry.getAttachment());
		}
	}
	
	protected abstract void handleEntry(ReportEntry entry);
	
	protected abstract void handleAttachment(Entity attachment);
	
	protected abstract void handleTickChange(int tick);
	
	protected abstract void handleAgentChange(Agent agent);
	
	public abstract TOutput getOutput();
}
