package angerona.fw.aspgraph.graphs;

/**
 * Represents a literal vertex in an Explanation Graph
 * @author ella
 *
 */
public class EGLiteralVertex extends EGVertex{

	/**
	 * 
	 */
	private static final long serialVersionUID = -145703320621925012L;

	/**
	 * Defines possible annotations of vertex
	 * <ul><li>POS: + (literal is contained in answer set)</li>
	 * <li>NEG: - (literal is not contained in answer set)</li></ul>
	 * @author ella
	 *
	 */
	public enum Annotation{POS, NEG};
	
	/**
	 * Literal which is represented bei this vertex
	 */
	private String literal;
	
	/**
	 * Annotation of literal
	 */
	private Annotation annotation;
	
	/**
	 * Creates new literal vertex in Explanation Graph
	 * @param a Annotation of literal
	 * @param literal Literal which is represented by vertex
	 */
	public EGLiteralVertex(Annotation a, String literal){
		annotation = a;
		this.literal = literal;
		index = -1;
		lowlink = -1;
	}
	
	/**
	 * Returns literal which is represented by vertex
	 * @return Literalname
	 */
	public String getLiteral(){
		return literal;
	}
	
	/**
	 * Returns annotation of literal
	 * @return Annotation of literal
	 */
	public Annotation getAnnotation(){
		return annotation;
	}
	
	@Override
	public String toString(){
		switch(annotation){
		case POS: return "<html>" + literal + "<sup>+</sup></html>"; 
		case NEG: return "<html>" + literal + "<sup>-</sup></html>";
		default: return literal;
		}
	}
	
	/**
	 * Returns string of literal+annotation without any HTML-formatting
	 * @return String representation of node without any formatting
	 */
	public String pureString(){
		return literal + "+";
	}
	
	@Override
	public boolean equals(Object o){
		EGLiteralVertex v;
		if (o instanceof EGLiteralVertex) v = (EGLiteralVertex) o;
		else return false;
		if (v.getLiteral().equals(this.getLiteral()) && v.getAnnotation().equals(this.annotation)) return true;
		else return false;
	}
	
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	
}