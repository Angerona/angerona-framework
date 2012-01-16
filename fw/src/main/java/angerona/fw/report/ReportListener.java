package angerona.fw.report;


/**
 * Informs about reports received from a simulation of the Angerona 
 * framework.
 * @author Tim Janus
 */
public interface ReportListener {
	void reportReceived(ReportEntry entry);
}
