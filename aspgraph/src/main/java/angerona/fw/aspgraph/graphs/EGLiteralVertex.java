package angerona.fw.aspgraph.graphs;

public class EGLiteralVertex extends EGVertex{

	public enum Annotation{POS, NEG};
	private String literal;
	private Annotation annotation;
	
	public EGLiteralVertex(Annotation a, String literal){
		annotation = a;
		this.literal = literal;
	}
	
	public String getLiteral(){
		return literal;
	}
	
	public Annotation getAnnotation(){
		return annotation;
	}
	
	public String toString(){
		switch(annotation){
		case POS: return "<html>" + literal + "<sup>+</sup></html>"; 
		case NEG: return "<html>" + literal + "<sup>-</sup></html>";
		default: return literal;
		}
	}
	
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