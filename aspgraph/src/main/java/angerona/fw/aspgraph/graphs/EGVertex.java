package angerona.fw.aspgraph.graphs;

import java.io.Serializable;

public abstract class EGVertex implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8466661122828417539L;
	protected int index;
	protected int lowlink;
	
	public abstract String toString();
	public abstract String pureString();
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public int getLowlink(){
		return lowlink;
	}
	
	public void setLowlink(int lowlink){
		this.lowlink = lowlink;
	}
}
