package angerona.fw.gui;
import angerona.fw.report.ReportAttachment;
import angerona.fw.report.ReportEntry;

/**
 * The NavigationUser interface is responsible for the communication
 * between a NavigationPanel and its parent component.
 * @author Tim Janus
 *
 */
public interface NavigationUser {
	/** @return a reference to the report-attachment which is observed by the parent component */
	ReportAttachment getAttachment();
	
	/** @return the entry which is watch currently */
	ReportEntry getCurrentEntry();
	
	/** sets the current watched report entry to the given reference */
	void setCurrentEntry(ReportEntry entry);
}
