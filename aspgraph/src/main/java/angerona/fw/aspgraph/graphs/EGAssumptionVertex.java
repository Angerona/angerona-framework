package angerona.fw.aspgraph.graphs;

/**
 * Represents an assumption vertex in an Explanation Graph
 * @author ella
 *
 */
public class EGAssumptionVertex extends EGVertex{

	private static final long serialVersionUID = -4684229848256764350L;
	
	/**
	 * Literal which should be represented as an assumption
	 */
	private String sourceLiteral;
	
	/**
	 * Creates a new assumption vertex
	 * @param literal Literal, which shold be represented as an assumption
	 */
	public EGAssumptionVertex(String literal){
		sourceLiteral = literal;
		index = -1;
		lowlink = -1;
	}
	
	@Override
	public String toString() {
		return "<html>assume</html>";
	}

	@Override
	public String pureString() {
		return "assume";
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EGAssumptionVertex){
			EGAssumptionVertex v = (EGAssumptionVertex) o;
			if (v.sourceLiteral.equals(sourceLiteral)) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return 7;
	}

}
