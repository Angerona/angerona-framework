package angerona.fw.aspgraph.graphs;

/**
 * 
 * @author ella
 *
 */
public class EGAssumptionVertex extends EGVertex{

	private static final long serialVersionUID = -4684229848256764350L;
	private String sourceLiteral;
	
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
