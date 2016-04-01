package com.github.kreatures.core.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parsable provides methods to parse content of agent-components at initialization
 * 
 * @author Manuel Barbi
 *
 */
public interface Parsable {

	default void loadFromFile(File path) throws IOException {
		try (InputStream src = new FileInputStream(path)) {
			loadFromStream(src);
		}
	}

	void loadFromStream(InputStream src) throws IOException;

	String getFileExtention();

}
