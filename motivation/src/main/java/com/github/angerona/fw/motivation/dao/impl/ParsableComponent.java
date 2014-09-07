package com.github.angerona.fw.motivation.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.angerona.fw.BaseAgentComponent;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class ParsableComponent extends BaseAgentComponent {

	public void loadFromFile(File path) throws IOException {
		InputStream fileIn = null;
		try {
			fileIn = new FileInputStream(path);
			loadFromStream(fileIn);
		} finally {
			try {
				if (fileIn != null) {
					fileIn.close();
				}
			} catch (IOException ioe) {
				// ignore
			}
		}
	}

	public abstract void loadFromStream(InputStream src) throws IOException;

	public abstract String getFileSuffix();

}
