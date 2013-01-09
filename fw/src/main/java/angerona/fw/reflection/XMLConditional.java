package angerona.fw.reflection;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;
import angerona.fw.serialize.SerializeHelper;

/**
 * 
 * @author Tim Janus
 */
@Root(name="conditional")
public class XMLConditional extends XMLCommando {

	@Root
	private static class ConditionalSequence extends XMLCommandoSequence {

		@Attribute(name="condition", required=true)
		public Condition condition;
		
		@Override
		public void setContext(Context context) {
			super.setContext(context);
			condition.setContext(context);
		}
		
	}
	
	@Element(name="if", required=true)
	ConditionalSequence ifPart;
	
	@ElementList(entry="elseif", inline=true, required=false, empty=true)
	List<ConditionalSequence> elseIfPart;
	
	@Element(name="else", required=false)
	XMLCommandoSequence elseCommandos;
	
	@Override
	protected void executeInternal() throws InvokeException {
		boolean done = false;
		ifPart.setContext(getContext());
		if(ifPart.condition.evaluate()) {
			ifPart.execute(getContext());
			done = true;
		} else if(elseIfPart != null) {
			for(ConditionalSequence cs : elseIfPart) {
				cs.setContext(getContext());
				if(cs.condition.evaluate()) {
					cs.execute(getContext());
					done = true;
					break;
				}
			}
		}
		
		if(!done && elseCommandos != null) {
			elseCommandos.execute(getContext());
		}
	}
	
	public static void main(String [] args) throws ClassNotFoundException {
		XMLConditional test = new XMLConditional();
		test.ifPart = new ConditionalSequence();
		test.ifPart.condition = new BooleanExpression(new Value("1"), BooleanExpression.Operator.LESS, new Value("2"));
		test.ifPart.addCommando(new XMLAssign("1k", new Value("1000")));
		
		SerializeHelper.outputXml(test, System.out);
	}
}
