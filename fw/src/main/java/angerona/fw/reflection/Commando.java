package angerona.fw.reflection;

import angerona.fw.error.InvokeException;

public interface Commando {
	boolean execute(Context context);
	
	InvokeException getLastError();
}
