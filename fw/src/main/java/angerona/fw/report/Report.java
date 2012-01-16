package angerona.fw.report;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;

/**
 * A report encapsulates the data collect during a simulation. The Angerona main class is responsible
 * to feed in the data. The class does not use a listener concept because we want to guarantee that the
 * report data is saved before the other ReportListeners are informed.
 * @author Tim Janus
 */
public class Report  {
	private List<ReportEntry> entries = new LinkedList<ReportEntry>();
	
	private Map<ReportAttachment, List<ReportEntry>> attachmentEntriesMap = new HashMap<ReportAttachment, List<ReportEntry>>();
	
	private AngeronaEnvironment simulation;
	
	public Report(AngeronaEnvironment simulation) {
		this.simulation = simulation;
	}

	public void saveEntry(ReportEntry entry) {
		if(entry.getPoster().getSimulation() == simulation) {
			ReportEntry copy = (ReportEntry) entry.clone();
			entries.add(copy);
			
			ReportAttachment att = entry.getAttachment();
			if(att != null) {
				List<ReportEntry> entries = null;
				if(!attachmentEntriesMap.containsKey(att)) {
					entries = new LinkedList<ReportEntry>();
					attachmentEntriesMap.put(att, entries);
				} else {
					entries = attachmentEntriesMap.get(att);
				}
				entries.add(copy);
			}
		}
	}
	
	public List<ReportEntry> getAllEntries() {
		return Collections.unmodifiableList(entries);
	}
	
	/**
	 * Returns all the entries which belong to a specific attachment. The entries must really belong to this attachment
	 * no child or parent relationships are observed. The returned list is not modifiable and will throw an exception if
	 * someone tries to modify it.
	 * @param attachment	reference to the attachment which will return the list.
	 * @return				null if a list for the given attachment does not exists otherwise the list with all the report entries
	 * 						of the given attachment.
	 */
	public List<ReportEntry> getEntriesOf(ReportAttachment attachment) {
		if(attachment == null)	throw new IllegalArgumentException("attachment must not be null.");
		return Collections.unmodifiableList(attachmentEntriesMap.get(attachment));
	}
}
