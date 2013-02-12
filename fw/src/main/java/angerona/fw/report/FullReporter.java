package angerona.fw.report;

import angerona.fw.internal.Entity;

public interface FullReporter extends Reporter {
	void report(String message, ReportPoster poster);
	
	void report(String message, Entity attachment, ReportPoster poster);
	
	void setDefaultPoster(ReportPoster defaultPoster);
}
