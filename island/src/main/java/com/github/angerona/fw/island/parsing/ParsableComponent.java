package com.github.angerona.fw.island.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.BaseAgentComponent;

/**
 * {@link ParsableComponent} is an abstract base class for
 * {@link AgentComponent}s offering the possibility to parse data from file
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class ParsableComponent extends BaseAgentComponent {

	public void loadFromFile(File path) throws IOException {
		try (FileInputStream src = new FileInputStream(path)) {
			loadFromStream(src);
		}
	}

	public abstract void loadFromStream(FileInputStream src) throws IOException;

	public abstract String getFileSuffix();

}
