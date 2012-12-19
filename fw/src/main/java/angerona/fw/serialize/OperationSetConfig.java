package angerona.fw.serialize;

import java.util.Set;

public interface OperationSetConfig {
	public String getOperationType();
	
	public String getDefaultClassName();
	
	public Set<String> getOperatorClassNames();
}
