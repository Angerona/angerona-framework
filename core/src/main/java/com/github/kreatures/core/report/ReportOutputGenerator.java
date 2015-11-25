package com.github.kreatures.core.report;

public interface ReportOutputGenerator<TOutput> extends ReportListener {
	TOutput getOutput();
}
