package angerona.fw.serialize;

import java.util.Set;

public interface OperatorSetConfig {
	public String getDefaultClassName();
	
	public Set<String> getOperatorClassNames();
}
