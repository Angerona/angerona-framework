package com.github.angerona.fw.serialize;

import java.util.List;

import com.github.angerona.fw.asml.ASMLCommand;

public interface CommandSequenceSerialize {
	
	List<ASMLCommand> getCommandSequence();
	
	/**
	 * @return 	The name of the command sequence. Especially script files provide a name but this
	 * 			also might be null.
	 */
	String getName();
}
