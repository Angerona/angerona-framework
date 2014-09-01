package com.github.angerona.fw.motivation.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.Parsable;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightRanges extends GenWeightRanges<Maslow> implements Parsable {

	@Override
	protected GenWeightRanges<Maslow> create() {
		return new WeightRanges();
	}

	@Override
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

	@Override
	public void loadFromStream(InputStream src) throws IOException {
		try {
			MotivationParser parser = new MotivationParser(src);
			this.ranges = parser.gatherRanges();
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

}
