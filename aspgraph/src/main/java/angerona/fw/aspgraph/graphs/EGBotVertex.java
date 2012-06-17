package angerona.fw.aspgraph.graphs;

public class EGBotVertex extends EGVertex{

	private String sourceLiteral;
	
	public EGBotVertex(String literal){
		sourceLiteral = literal;
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
