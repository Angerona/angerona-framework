package com.github.angerona.fw.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.util.ModelAdapter;

/**
 * A report encapsulates the data collect during a simulation. The Angerona main class is responsible
 * to feed in the data. The class does not use a listener concept because we want to guarantee that the
 * report data is saved before the other ReportListeners are informed.
 * 
 * @author Tim Janus
 */
public class Report extends ModelAdapter  {
	/** list containing all the report entries */
	private List<ReportEntry> entries = new LinkedList<ReportEntry>();
	
	/** mapping from an entity to a list of report entries containing the key entity as attachment */
	private Map<Long, List<ReportEntry>> attachmentEntriesMap = new HashMap<Long, List<ReportEntry>>();
	
	/** reference to the simulation of the report */
	private AngeronaEnvironment simulation;
	
	/** a list of listeners that are informed if the report listeners change */
	private List<ReportListener> listeners = new ArrayList<>();
	
	public Report(AngeronaEnvironment simulation) {
		this.simulation = simulation;
	}
	
	public void addReportListener(ReportListener listener) {
		this.listeners.add(listener);
	}

	public void removeReportListener(ReportListener listener) {
		this.listeners.remove(listener);
	}
	
	public void removeAllReportListeners() {
		this.listeners.clear();
	}
	
	/** 
	 * saves the given entry in the data structures of the report. That means its  put in the complete list and
	 * in the entity -> Report entries map if an attachment is added to the report.
	 * The entry and its attachment is copied before saving.
	 * @param entry	reference to the entry which will be saved.
	 */
	public void saveEntry(ReportEntry entry) {
		if(entry.getSimulation() == simulation) {
			ReportEntry copy = (ReportEntry) entry.clone();
			entries.add(copy);
			
			Entity att = entry.getAttachment();
			if(att != null) {
				Long id = att.getGUID();
				
				if(id == null) {
					throw new IllegalArgumentException("The id of attached entity must not be null.");
				}
				
				// Adapt the list of report-entries that stores this attachment
				// by adding this report-entry.
				List<ReportEntry> entries = null;
				if(!attachmentEntriesMap.containsKey(id)) {
					entries = new LinkedList<ReportEntry>();
					attachmentEntriesMap.put(id, entries);
				} else {
					entries = attachmentEntriesMap.get(id);
				}
				entries.add(copy);
			}
			
			for(ReportListener listener : listeners) {
				listener.reportReceived(copy);
			}
		}
	}
	
	/**
	 * @return complete list of report entries so far. The list is not modifiable.
	 */
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
	public List<ReportEntry> getEntriesOf(Entity attachment) {
		if(attachment == null)	throw new IllegalArgumentException("attachment must not be null.");
		return getEntriesOf(attachment.getGUID());
	}
	
	/**
	 * Returns all the entries which belong to a specific id. The entries must really belong to this id
	 * no child or parent relationships are observed. The returned list is not modifiable and will throw an exception if
	 * someone tries to modify it.
	 * @param id			long representing the id of the object mentioned in the attachments.
	 * @return				null if a list for the given attachment does not exists otherwise the list with all the report entries
	 * 						of the given attachment.
	 */
	public List<ReportEntry> getEntriesOf(Long id) {
		if(attachmentEntriesMap.containsKey(id)) {
			return Collections.unmodifiableList(attachmentEntriesMap.get(id));
		} else {
			return null;
		}
	}
}
