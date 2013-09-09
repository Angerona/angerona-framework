package com.github.angerona.fw.report;

import java.util.Date;
import java.util.Stack;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.OperatorStack;

/**
 * A ReportEntry contains informations about a report, like the message, the poster or the attachment.
 * It also saves the simulation tick and the real time when the report was create.
 * 
 * It implements the cloneable interface and also overrides equal for comparing itself with other report entries.
 * 
 * @author Tim Janus
 */
public class ReportEntry implements Cloneable {
	/** simulation tick */
	private int 	simulationTick;
	
	/** real time at the cration of the entry */
	private Date 	realTime;
	
	/** a short message describing what the report entry is all about */
	private String 	message;
	
	/** the attachment of the report entry, might be null if no attachment is attached. */
	private Entity attachment;

	/** the scope in which the entry is generated (agent or belief base). */
	private Scope scope;
	
	/** reference to the poster of the report (an operator, a belief base or an agent) */
	private ReportPoster poster;
	
	/** reference to the entire simulation */
	private AngeronaEnvironment simulation;
	
	/** a stack of strings representing the operators callstack which create this entry */
	private Stack<String> operatorCallstack = new Stack<String>();
	
	/**
	 * Ctor: Creates a new report entry with the given message, poster and attachment.
	 * @param message		A string representing the message saved in the report entry
	 * @param attachment	An optional attachment like a belief base or a data component like 
	 * 						Secrecy Knowledge.
	 * @param scope			The scope of in which the report entry is generated as OperatorStack.
	 * @param poster		A reference to the poster of the entry, in most cases an operator.
	 * @param simulation	A reference to the current simulation.
	 */
	public ReportEntry(String message, Entity attachment, 
			OperatorStack scope, ReportPoster poster, AngeronaEnvironment simulation) {
		// check for valid parameters
		if(poster == null)
			throw new IllegalArgumentException("poster must not be null");
		if(simulation == null)
			throw new IllegalArgumentException("poster must have a refernce to a simulation.");
		
		// set the parameters
		this.message = message;
		this.attachment = attachment;
		this.poster = poster;
		this.scope = new Scope(scope);
		this.simulation = simulation;
		this.simulationTick = simulation.getSimulationTick();
		this.realTime = new Date();
		
		for(BaseOperator op : scope.getOperatorStack()) {
			operatorCallstack.add(op.getClass().getName());
		}
	}

	public Scope getScope() {
		return scope;
	}
	
	public int getSimulationTick() {
		return simulationTick;
	}

	public Date getRealTime() {
		return realTime;
	}

	public String getMessage() {
		return message;
	}

	public Entity getAttachment() {
		return attachment;
	}
	
	public AngeronaEnvironment getSimulation() {
		return simulation;
	}
	
	public String getPosterName() {
		return poster.getPosterName();
	}
	
	public ReportPoster getPoster() {
		return poster;
	}
	
	public Stack<String> getStack() {
		return operatorCallstack;
	}
	
	/** Default Ctor: is private and does nothing cause it is used by objects clone method */
	private ReportEntry() {}
	
	@Override
	public Object clone() {
		ReportEntry reval = new ReportEntry();
		reval.message = this.message;
		reval.simulationTick = this.simulationTick;
		reval.poster = poster;
		reval.realTime = this.realTime;
		reval.operatorCallstack = new Stack<String>();
		reval.operatorCallstack.addAll(operatorCallstack);
		reval.simulation = this.simulation;
		if(this.attachment != null) {
			if(this.attachment instanceof AgentComponent) {
				AgentComponent atomic = (AgentComponent) this.attachment;
				reval.attachment = (AgentComponent) atomic.clone();
			} else {
				reval.attachment = this.attachment;
			}
		}
		else 
			reval.attachment = null;
		return reval;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == this)						return true;
		if(! (other instanceof ReportEntry))	return false;
		
		ReportEntry cast = (ReportEntry)other;
		if(	cast.simulationTick == this.simulationTick && 
			cast.realTime == this.realTime) {
			if(this.attachment == null && cast.attachment == null)	return true;
			else if(this.attachment != null && cast.attachment != null) {
				return this.attachment.getGUID().equals(cast.attachment.getGUID());
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return simulationTick + realTime.hashCode() + 
				(attachment == null ? 0 : attachment.hashCode()) * 53;
	}
	
	public static class Scope {
		private OperatorStack visitor;
		
		private Agent agent;
		
		public Scope(OperatorStack visitor) {
			this.visitor = visitor;
			if(visitor instanceof Agent) {
				agent = (Agent)visitor;
			} else if(visitor instanceof AgentComponent) {
				agent = ((AgentComponent)visitor).getAgent();
			}
		}
		
		public OperatorStack getScope() {
			return visitor;
		}
		
		public Agent getAgent() {
			return agent;
		}
	}
}
