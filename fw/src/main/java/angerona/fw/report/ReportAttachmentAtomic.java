package angerona.fw.report;

/**
 * Marks a report-attachment as atomic. This means that this report attachment
 * is a child in the report-attachment hierarchy. Children are beliefbases, desires, intentions.
 * Parents are Agents (containing belief bases and so on).
 * @author Tim Janus
 */
public interface ReportAttachmentAtomic extends ReportAttachment, Cloneable {
	public Object clone();
}
