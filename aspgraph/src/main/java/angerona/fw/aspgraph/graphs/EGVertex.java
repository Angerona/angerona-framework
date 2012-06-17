package angerona.fw.aspgraph.graphs;

import java.io.Serializable;

public abstract class EGVertex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8466661122828417539L;
	public abstract String toString();
	public abstract String pureString();
}
