package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models a comment, which is a rule that
 * is completely ignored by the dlv and clingo interface.
 * 
 * it allows to add some human readable text to auto-generated
 * logic programs for debugging purposes.
 * 
 * @author Thomas Vengels
 *
 */
public class Comment extends Rule {

	protected String[] lines = null;
	
	public Comment(String ...lines) {
		this.lines = lines;
	}
	
	@Override
	public String toString() {
		String ret = "";
		int ll = lines.length;
		for (int i = 0; i < ll; i++) {
			ret += lines[i];
			if ((i+1) < ll)
				ret += "\r\n";
		}
		return ret;
	}
	
	@Override
	public boolean isComment() {
		return true;
	}
}
