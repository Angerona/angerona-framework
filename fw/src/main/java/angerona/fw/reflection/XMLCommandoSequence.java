package angerona.fw.reflection;

import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import angerona.fw.error.InvokeException;

@Root(name="asml-script")
public class XMLCommandoSequence extends XMLCommando {

	/** the orderer list of commands in this collection */
	@ElementListUnion({
		@ElementList(entry="assign", inline=true, type=XMLAssign.class),
		@ElementList(entry="while", inline=true, type=XMLWhile.class),
		@ElementList(entry="operation", inline=true, type=XMLOperation.class),
		@ElementList(entry="conditional", inline=true, type=XMLConditional.class),
		@ElementList(entry="execute", inline=true, type=XMLExecute.class)
	})
	private List<XMLCommando> commandos = new LinkedList<>();
	
	@Attribute(name="name", required=false)
	private String name;
	
	public XMLCommandoSequence() {}
	
	public XMLCommandoSequence(
			@ElementListUnion({
				@ElementList(entry="assign", inline=true, type=XMLAssign.class),
				@ElementList(entry="while", inline=true, type=XMLWhile.class),
				@ElementList(entry="operation", inline=true, type=XMLOperation.class),
				@ElementList(entry="conditional", inline=true, type=XMLConditional.class),
				@ElementList(entry="execute", inline=true, type=XMLExecute.class)
			})
			List<XMLCommando> sequence,
			@Attribute(name="name", required=false) String name) {
		this.name = name;
		this.commandos = sequence;
	}
	
	public String getName() {
		return name;
	}
	
	public void addCommando(XMLCommando commando) {
		this.commandos.add(commando);
	}
	
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		for(XMLCommando cmd : commandos) {
			cmd.setContext(context);
		}
	}
	
	@Override
	protected void executeInternal() throws InvokeException {
		for(XMLCommando cmd : commandos) {
			cmd.executeInternal();
		}
	}
}
