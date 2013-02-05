package angerona.fw.report;

public interface ReportOutputGenerator<TOutput> extends ReportListener {
	TOutput getOutput();
}
