package angerona.fw.report;

import java.util.Date;
import java.util.Stack;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.BaseOperator;
import angerona.fw.internal.Entity;
import angerona.fw.internal.EntityAtomic;
import angerona.fw.internal.IdGenerator;
import angerona.fw.operators.OperatorVisitor;

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

	/** reference to the poster of the report. We assume that the name and the simulation is
	 * 	never changed during runtime. But the tick changes of course and is therefore saved in
	 * 	a separate attribute.
	 */
	private ReportPoster poster;
	
	/** a stack of strings representing the operators callstack which create this entry */
	private Stack<String> operatorCallstack = new Stack<String>();
	
	/**
	 * Ctor: Creates a new report entry with the given message, poster and attachment.
	 * @param message		A string representing the message saved in the report entry
	 * @param poster		A reference to the poster of the entry, in most cases an operator.
	 * @param attachment	An optional attachment like a belief base or a data component like 
	 * 						Secrecy Knowledge.
	 */
	public ReportEntry(String message, ReportPoster poster, Entity attachment) {
		// check for valid parameters
		if(poster == null)
			throw new IllegalArgumentException("poster must not be null");
		if(poster.getSimulation() == null)
			throw new IllegalArgumentException("poster must have a refernce to a simulation.");
		
		// set the parameters
		this.message = message;
		this.attachment = attachment;
		this.poster = poster;
		this.simulationTick = poster.getSimulation().getSimulationTick();
		this.realTime = new Date();
		
		// search for the parent in attachment hierarchy (an agent in most cases)
		Entity temp = attachment;
		if(temp != null) {
			while(temp.getParent() != null) {
				temp = IdGenerator.getEntityWithId(temp.getParent());
			}
		}
		
		// if the parent implements OperatorVisitor interface, then copy the
		// callstack
		if(temp instanceof OperatorVisitor) {
			OperatorVisitor ov = (OperatorVisitor)temp;
			for(BaseOperator op : ov.getStack()) {
				operatorCallstack.add(op.getClass().getName());
			}
		}
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
		return poster.getSimulation();
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
		if(this.attachment != null) {
			if(this.attachment instanceof EntityAtomic) {
				EntityAtomic atomic = (EntityAtomic) this.attachment;
				reval.attachment = (EntityAtomic) atomic.clone();
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
}
