package angerona.fw.aspgraph.graphs;

/**
 * Represents a bottom vertex in an Explanation Graph
 * @author ella
 *
 */
public class EGBotVertex extends EGVertex{

	/**
	 * 
	 */
	private static final long serialVersionUID = -915228076356931903L;
	
	/**
	 * Literal which should be represented as false
	 */
	private String sourceLiteral;
	
	
	/**
	 * Creates new bottom vertex
	 * @param literal Literal which should be represented as false
	 */
	public EGBotVertex(String literal){
		sourceLiteral = literal;
		index = -1;
		lowlink = -1;
	}
	
	@Override
	public String toString() {
		return "<html>&perp;</html>";
	}

	@Override
	public String pureString() {
		return "T";
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EGBotVertex){
			EGBotVertex v = (EGBotVertex) o;
			if (v.sourceLiteral.equals(sourceLiteral)) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return 5;
	}

}
