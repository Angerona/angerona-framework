package angerona.fw.knowhow;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import angerona.fw.logic.asp.AspBeliefbase;

/**
 * Extends a generic ASP Beliefbase with know-how.
 * @author Tim Janus
 */
public class KnowHowBeliefbase extends AspBeliefbase {
	
	private Program nextAction;
	
	private Program initTree;
	
	public KnowHowBeliefbase() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("programs/InitTree");
		initTree = Program.loadFrom(new InputStreamReader(is));
		
		is = this.getClass().getClassLoader().getResourceAsStream("programs/NextAction");
		nextAction = Program.loadFrom(new InputStreamReader(is));
	}
	
	public String getFileEnding() {
		return "aspkh";
	}
}
