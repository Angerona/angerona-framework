package net.sf.tweety.logics.firstorderlogic.parser;

import java.io.StringReader;

import net.sf.tweety.logics.firstorderlogic.FolBeliefSet;

/**
 * Functional test for the new javacc parser 'FolParserB'
 * 
 * @author Tim Janus
 */
public class FunctionalTest {
	public static void main(String [] args) {
		String 	bb	= "married(john, mary)\n";
				bb += "-argued(john)";
		
				bb  = "Persons={john,mary}\n";
				bb += "married(john, mary)\n";
				bb += "-argued(john)";
				
		FolParserB bp = new FolParserB(new StringReader(bb));
		
		try {
			FolBeliefSet fbs = bp.KB();
			System.out.println(fbs);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

