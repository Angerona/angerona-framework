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
				bb += "type (married(Persons, Persons))\n";
				bb += "type (argued(Persons))\n";
				bb += "type (test(Persons))\n";
				bb += "married(john, mary)\n";
				bb += "-argued(john)\n";
				//bb += "test(wife(john))";
				
		FolParserB bp = new FolParserB(new StringReader(bb));
		bp.setForce(true);
		
		try {
			FolBeliefSet fbs = bp.KB();
			System.out.println("Signature: " + fbs.getSignature());
			System.out.println("Beliefbase: " + fbs);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

