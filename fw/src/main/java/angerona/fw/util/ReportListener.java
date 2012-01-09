package angerona.fw.util;

/**
 * Informs about reports received from a simulation of the Angerona 
 * framework.
 * @author Tim Janus
 */
public interface ReportListener {
	void reportReceived(String msg, ReportPoster sender, Object attachment);
}
