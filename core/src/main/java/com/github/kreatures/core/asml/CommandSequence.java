package com.github.kreatures.core.asml;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import com.github.kreatures.core.error.InvokeException;
import com.github.kreatures.core.reflection.Context;
import com.github.kreatures.core.serialize.CommandSequenceSerialize;

/**
 * A command sequence acts as base for several sub concepts like conditionals
 * or loops. Every ASML script file represents a command sequence. This class
 * represents a command sequence by providing an orderer list of commands
 * 
 * @author Tim Janus
 */
@Root(name="asml-script")
public class CommandSequence extends ASMLCommand implements CommandSequenceSerialize{

	/** the orderer list of commands in this sequence */
	@ElementListUnion({
		@ElementList(entry="assign", inline=true, type=Assign.class),
		@ElementList(entry="while", inline=true, type=While.class),
		@ElementList(entry="operation", inline=true, type=InvokeOperation.class),
		@ElementList(entry="conditional", inline=true, type=Conditional.class),
		@ElementList(entry="execute", inline=true, type=Execute.class)
	})
	private List<ASMLCommand> commands = new LinkedList<>();
	
	/** the optional name for the command sequence */
	@Attribute(name="name", required=false)
	private String name;
	
	public CommandSequence() {}
	
	public CommandSequence(
			@ElementListUnion({
				@ElementList(entry="assign", inline=true, type=Assign.class),
				@ElementList(entry="while", inline=true, type=While.class),
				@ElementList(entry="operation", inline=true, type=InvokeOperation.class),
				@ElementList(entry="conditional", inline=true, type=Conditional.class),
				@ElementList(entry="execute", inline=true, type=Execute.class)
			})
			List<ASMLCommand> sequence,
			@Attribute(name="name", required=false) String name) {
		this.name = name;
		this.commands = sequence;
	}
	
	@Override
	public List<ASMLCommand> getCommandSequence() {
		return Collections.unmodifiableList(commands);
	}
	
	/**
	 * @return 	The name of the command sequence. Especially script files provide a name but this
	 * 			also might be null.
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Adds the given command to the list of commands.
	 * @param commando	Reference to the command which is added.
	 */
	public void addCommando(ASMLCommand commando) {
		this.commands.add(commando);
	}
	
	/**
	 * Sets the context for all commands contained in the command sequence 
	 * and for the sequence itself.
	 */
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		for(ASMLCommand cmd : commands) {
			cmd.setContext(context);
		}
	}
	
	/**
	 * Executes the commands in the sequence in the correct order.
	 */
	@Override
	protected void executeInternal() throws InvokeException {
		for(ASMLCommand cmd : commands) {
			cmd.executeInternal();
		}
	}
}
