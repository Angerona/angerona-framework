package com.github.angerona.fw.motivation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface Parsable {

	public void loadFromFile(File path) throws IOException;

	public void loadFromStream(InputStream src) throws IOException;

}
