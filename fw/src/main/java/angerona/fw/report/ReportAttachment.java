package angerona.fw.report;

import java.util.List;


public interface ReportAttachment extends Cloneable {
	/** @return the id of the attachment */
	Long getGUID();
	
	/** @return the id of the parent of this report attachment (might be null that means no parent) */
	Long getParent();
	
	/** @return a list of ids of all objects which are children of this instance. */
	List<Long> getChilds();
	
	public Object clone();
}
