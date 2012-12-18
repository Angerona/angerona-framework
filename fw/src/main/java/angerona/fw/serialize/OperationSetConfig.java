package angerona.fw.serialize;

import java.util.Set;

public interface OperationSetConfig {
	public String getDefaultClassName();
	
	public Set<String> getOperatorClassNames();
}
