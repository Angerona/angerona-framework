package angerona.fw.report;

import angerona.fw.internal.Entity;

public interface Reporter {
	void report(String message);
	
	void report(String message, Entity attachment);

	void report(String message, ReportPoster poster);
	
	void report(String message, Entity attachment, ReportPoster poster);
}
