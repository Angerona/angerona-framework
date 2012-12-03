package angerona.fw.reflection;

import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;

import angerona.fw.error.InvokeException;

public class XMLCommandoSequence extends XMLCommando {

	/** the orderer list of commandos in this collection */
	@ElementListUnion({
		@ElementList(entry="assign", inline=true, type=XMLAssign.class),
		@ElementList(entry="while", inline=true, type=XMLWhile.class)
	})
	private List<XMLCommando> commandos = new LinkedList<>();
	
	public XMLCommandoSequence() {}
	
	public XMLCommandoSequence(
			@ElementListUnion({
				@ElementList(entry="assign", inline=true, type=XMLAssign.class),
				@ElementList(entry="while", inline=true, type=XMLWhile.class)
			})
			List<XMLCommando> sequence) {
		this.commandos = sequence;
	}
	
	public void addCommando(XMLCommando commando) {
		this.commandos.add(commando);
	}
	
	@Override 
	public void execute(Context context) {
		for(XMLCommando cmd : commandos) {
			cmd.setContext(context);
		}
		super.execute(context);
	}
	
	@Override
	protected void executeInternal() throws InvokeException {
		for(XMLCommando cmd : commandos) {
			cmd.executeInternal();
		}
	}
}
