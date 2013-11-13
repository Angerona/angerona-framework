package com.github.angerona.knowhow.situation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import net.sf.tweety.lp.asp.parser.ASPParser;
import net.sf.tweety.lp.asp.parser.ParseException;
import net.sf.tweety.lp.asp.syntax.Program;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import com.github.angerona.fw.Angerona;

/**
 * Base class for different situations that are used to generate plans.
 * They all contain a program that represents the background knowledge.
 * In the XML file the filename of the background program is given and
 * during the validate() method a check occurs that tests if the given
 * file is an existing file. In the commit() method the ASPParser is used
 * to parse the content of the file containing the background knowledge.
 * 
 * @author Tim Janus
 */
public class Situation {
	/** the goal this situation fulfills */
	@Element(name="goal")
	protected String goal;
	
	/** the name of the file containing the background knowledge */
	@Element(name="background", required=false)
	protected File filenameBackgroundProgram;
	
	/** the background knowledge about the situation as an ASP program */
	private Program backgroundKnowledge;
	
	public String getGoal() {
		return goal;
	}
	
	public Program getBackground() {
		return backgroundKnowledge;
	}
	
	@Validate
	public void validate() throws PersistenceException {
		
		if(filenameBackgroundProgram != null) {
			if(!filenameBackgroundProgram.exists()) {
				String dir = Angerona.getInstance().getActualSimulation().getDirectory();
				filenameBackgroundProgram = new File(dir + "/" + filenameBackgroundProgram.getPath());
				if(!filenameBackgroundProgram.exists())
					throw new PersistenceException("Cannot find background program in '" + filenameBackgroundProgram + "'");
			}
		}
	}
	
	@Commit
	public void build() throws PersistenceException {
		try {
			if(filenameBackgroundProgram != null) {
				backgroundKnowledge = ASPParser.parseProgram(new FileReader(filenameBackgroundProgram));
			}
		} catch (FileNotFoundException | ParseException e) {
			throw new PersistenceException("Cannot parse background program in '" + filenameBackgroundProgram + "' - " + e.getMessage());
		}
		
	}
}
