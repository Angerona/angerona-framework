package com.github.kreaturesfw.core.report;

public interface ReportOutputGenerator<TOutput> extends ReportListener {
	TOutput getOutput();
}
