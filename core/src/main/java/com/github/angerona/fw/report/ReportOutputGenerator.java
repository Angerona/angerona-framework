package com.github.angerona.fw.report;

public interface ReportOutputGenerator<TOutput> extends ReportListener {
	TOutput getOutput();
}
