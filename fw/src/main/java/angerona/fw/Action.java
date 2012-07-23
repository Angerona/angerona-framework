package angerona.fw;

import java.util.List;

import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;

/**
 * Base class for different Actions used in the Angerona framework.
 * @author Tim Janus
 */
public class Action implements Perception, ContextProvider {
	/** the unique name of the sender of the action **/
	private String sender;
	
	/** the unique name of the receiver of the action, might be null or sender in later implementations */
	private String receiver;

	/** this field is used if the action should be received by every agent in the network */
	public static final String ALL = "__ALL__";
	
	/**
	 * Ctor: generates a new Action
	 * @param sender	unique name of the sender of the action
	 * @param receiver	unique name of the receiver of the action, static member ALL means everyone receives this action
	 */
	public Action(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}
	
	/**	@return the unique name of the sender */
	public String getSenderId() {
		return sender;
	}
	
	/** @return the unique name of the receiver, the static member ALL means everyone should receive this action */
	public String getReceiverId() {
		return receiver;
	}
	
	@Override
	public String toString() {
		return "A: " + sender + " --> " + receiver;
	}
	
	/** @return true cause every subclass is an action */
	public boolean isAction() {
		return true;
	}
	
	@Override
	public Context getContext() {
		return ContextFactory.createContext(this);
	}
	//************Begin Daniel's changes*************//
	public boolean equals(Action a)
	{
		if(!this.sender.equals(a.sender))
		{
			return false;
		}
		if(!this.receiver.equals(a.receiver))
		{
			return false;
		}
		return true;
	}
	protected List<SecrecyStrengthPair> weakenings = null;
	public List<SecrecyStrengthPair> getWeakenings() {
		return this.weakenings;
	}
	public void setWeakenings(List<SecrecyStrengthPair> weaks) {
		this.weakenings = weaks;
	}
	//************End Daniel's changes*************//
}
