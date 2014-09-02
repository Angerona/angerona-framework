package com.github.angerona.fw.motivation.basic;

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

	public String getFileExtention();

}
