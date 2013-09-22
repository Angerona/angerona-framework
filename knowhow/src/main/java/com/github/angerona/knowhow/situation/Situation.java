package com.github.angerona.knowhow.situation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.sf.tweety.logicprogramming.asplibrary.parser.ASPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

public class Situation {
	@Element(name="background", required=false)
	protected String filenameBackgroundProgram;
	
	private Program backgroundKnowledge;
	
	public Program getBackground() {
		return backgroundKnowledge;
	}
	
	@Validate
	public void validate() throws PersistenceException {
		if(!new File(filenameBackgroundProgram).exists()) {
			throw new PersistenceException("Cannot find background program in '" + filenameBackgroundProgram + "'");
		}
	}
	
	@Commit
	public void build() throws PersistenceException {
		try {
			backgroundKnowledge = ASPParser.parseProgram(new FileReader(new File(filenameBackgroundProgram)));
		} catch (FileNotFoundException | ParseException e) {
			throw new PersistenceException("Cannot parse background program in '" + filenameBackgroundProgram + "' - " + e.getMessage());
		}
		
	}
}
