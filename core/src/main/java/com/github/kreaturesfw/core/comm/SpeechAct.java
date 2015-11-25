package com.github.kreaturesfw.core.comm;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.kreaturesfw.core.Action;
import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.Perception;

/**
 * Base class for every SpeechAct implemented by the Angerona framework.
 * 
 * To support meta-inferences the speech act has to implement a getType()
 * method that marks a speech act instance either as requesting or as 
 * informative, as proposed by Krümpelmann in mates2013.
 * 
 * @author Tim Janus
 */
public abstract class SpeechAct 
	extends Action 
	implements Perception {

	/**
	 * An enum representing the type of speech act, that is either requesting (query, justify)
	 * or informative (inform, answer, justification).
	 * 
	 * @author Tim Janus
	 */
	public enum SpeechActType {
		SAT_INFORMATIVE,
		SAT_REQUESTING
	}
	
	/** the unique name of the receiver of the action, might be null or sender in later implementations */
	@Element(name="receiver")
	private String receiver;

	/** this field is used if the action should be received by every agent in the network */
	public static final String ALL = "__ALL__";
	
	
	/** Ctor used by deserilization */
	public SpeechAct(	@Element(name="sender") String sender, 
						@Element(name="receiver") String receiver) {
		super(sender);
		this.receiver = receiver;
	}
	
	/**
	 * Ctor: creates a speech act with the given sender and receiver.
	 * @param sender	reference to the sending agent.
	 * @param receiver	unique name of the receiver agent.
	 */
	public SpeechAct(Agent sender, String receiver) {
		super(sender);
		this.receiver = receiver;
	}
	
	/** @return the unique name of the receiver, the static member ALL means everyone should receive this action */
	public String getReceiverId() {
		return receiver;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract SpeechActType getType();
	
	public abstract Set<FolFormula> getContent();
}
