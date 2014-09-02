package com.github.angerona.fw.motivation.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.Parsable;
import com.github.angerona.fw.motivation.model.MotiveCoupling;
import com.github.angerona.fw.motivation.parser.MotivationParser;
import com.github.angerona.fw.motivation.parser.ParseException;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotiveCouplings extends GenMotiveCouplings<Maslow, FolFormula> implements Parsable {

	protected MotiveCouplings() {}

	protected MotiveCouplings(Set<MotiveCoupling<Maslow, FolFormula>> couplings) {
		super(couplings);
	}

	@Override
	protected GenMotiveCouplings<Maslow, FolFormula> create() {
		return new MotiveCouplings();
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
			couplings = parser.gatherCouplings();
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

}
