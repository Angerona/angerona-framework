package com.github.angerona.fw.motivation.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.BaseAgentComponent;

/**
 * {@link ParsableComponent} is an abstract base class for {@link AgentComponent}s offering the possibility to parse data from file
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class ParsableComponent extends BaseAgentComponent {

	@SuppressWarnings("resource")
	public void loadFromFile(File path) throws IOException {
		try (ReadableByteChannel src = new FileInputStream(path).getChannel()) {
			loadFromChannel(src);
		}
	}

	public abstract void loadFromChannel(ReadableByteChannel src) throws IOException;

	public abstract String getFileSuffix();

}
