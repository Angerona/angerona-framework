package com.github.kreaturesfw.core.serialize;

import java.util.List;

import com.github.kreaturesfw.core.legacy.asml.ASMLCommand;

public interface CommandSequenceSerialize {
	
	List<ASMLCommand> getCommandSequence();
	
	/**
	 * @return 	The name of the command sequence. Especially script files provide a name but this
	 * 			also might be null.
	 */
	String getName();
}
