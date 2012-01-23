package angerona.fw.gui;
import angerona.fw.report.ReportAttachment;
import angerona.fw.report.ReportEntry;


public interface NavigationUser {
	ReportAttachment getAttachment();
	
	ReportEntry getActualEntry();
	
	void setActualEntry(ReportEntry entry);
}
