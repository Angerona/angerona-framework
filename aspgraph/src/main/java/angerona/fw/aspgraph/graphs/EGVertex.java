package angerona.fw.aspgraph.graphs;

import java.io.Serializable;

/**
 * Represents a vertex in an Explanation Graph
 * @author ella
 *
 */
public abstract class EGVertex implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8466661122828417539L;
	
	/**
	 * Index needed by Tarjan algorithm
	 */
	protected int index;
	
	/**
	 * Lowlink needed by Tarjan algorithm
	 */
	protected int lowlink;
	
	@Override
	public abstract String toString();
	
	/**
	 * Returns string representation of vertex without any formatting
	 * @return
	 */
	public abstract String pureString();
	
	/**
	 * Returns index
	 * @return Index needed by Tarjan algorithm
	 */
	public int getIndex(){
		return index;
	}
	
	/**
	 * Sets index
	 * @param index Intex
	 */
	public void setIndex(int index){
		this.index = index;
	}
	
	/**
	 * Returns lowlink needed by Trajan algorithm
	 * @return Lowlink
	 */
	public int getLowlink(){
		return lowlink;
	}
	
	/**
	 * Sets lowlink
	 * @param lowlink Lowlink
	 */
	public void setLowlink(int lowlink){
		this.lowlink = lowlink;
	}
}
