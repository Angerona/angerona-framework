package angerona.fw;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Commit;

import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;

/**
 * Base class for different Actions used in the Angerona framework.
 * @author Tim Janus
 */
public class Action 
	extends Intention 
	implements Perception, ContextProvider {

	/** the unique name of the sender of the action **/
	@Element(name="sender")
	private String sender;
	
	/** the unique name of the receiver of the action, might be null or sender in later implementations */
	@Element(name="receiver")
	private String receiver;

	/** this field is used if the action should be received by every agent in the network */
	public static final String ALL = "__ALL__";
	
	/** Ctor used for deserialization */
	public Action(	@Element(name="sender") String senderId, 
					@Element(name="receiver") String receiverId ) {
		super((Agent) null);
		this.sender = senderId;
		this.receiver = receiverId;
	}
	
	/**
	 * Ctor: generates a new Action
	 * @param sender	unique name of the sender of the action
	 * @param receiver	unique name of the receiver of the action, static member ALL means everyone receives this action
	 */
	public Action(Agent sender, String receiver) {
		super(sender);
		this.sender = sender.getName();
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
	
	@Override
	public Context getContext() {
		return ContextFactory.createContext(this);
	}
	//************Begin Daniel's changes*************//
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Action)) 
			return false;
		
		Action a = (Action)obj;
		if(!this.sender.equals(a.sender)) {
			return false;
		}
		if(!this.receiver.equals(a.receiver))
		{
			return false;
		}
		return true;
	}
	//************End Daniel's changes*************//

	@Override
	public int hashCode() {
		return (sender.hashCode() + receiver.hashCode()) * 29;
	}
	
	@Override
	public void onSubgoalFinished(Intention subgoal) {
		if(super.parent != null) {
			super.parent.onSubgoalFinished(this);
		}
	}

	@Override
	public boolean isAtomic() {
		return true;
	}

	@Override
	public boolean isSubPlan() {
		return false;
	}
	
	@Commit
	public void onDeserialization() {
		AngeronaEnvironment sim = Angerona.getInstance().getActualSimulation();
		if(sim != null) {
			setAgent(sim.getAgentByName(sender));
		}
	}
}
