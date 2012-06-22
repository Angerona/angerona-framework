package angerona.fw.aspgraph.graphs;

public class EGTopVertex extends EGVertex{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7344243387453486485L;
	private String sourceLiteral;
	
	public EGTopVertex(String literal){
		sourceLiteral = literal;
		index = -1;
		lowlink = -1;
	}
	
	@Override
	public String toString() {
		return "<html>T</html>";
	}

	@Override
	public String pureString() {
		return "T";
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof EGTopVertex){
			EGTopVertex v = (EGTopVertex) o;
			if (v.sourceLiteral.equals(sourceLiteral)) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return 3;
	}
}
