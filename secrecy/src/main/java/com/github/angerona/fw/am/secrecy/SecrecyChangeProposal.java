package com.github.angerona.fw.am.secrecy;

import java.util.ArrayList;
import java.util.List;

import com.github.angerona.fw.operators.BeliefOperatorFamily;

/**
 * This is a data structure that contains the change-operations necessary to
 * get a consistent secrecy knowledge, that means that the secrets belief operator 
 * cannot conclude the secret piece of information on the view on the attackers
 * agent's beliefs.
 * 
 * This class is a simple Collection of {@link SecretChangeProposal} that describe
 * atomic changes on secrets, such that it delegates the most methods like realize()
 * and distance() to the {@link SecretChangeProposal} instances. The distance method
 * uses the sum of the distances of the {@link SecretChangeProposal}.
 *  
 * @author Tim Janus
 */
public class SecrecyChangeProposal {
	/** a list containing all the atomic secret change proposals */
	private List<SecretChangeProposal> changes = new ArrayList<>();
	
	/** a flag storing if the realize operation on the proposal is done */
	private boolean realized;
	
	/**
	 * Adds a {@link SecretChangeProposal} to the list of changes 
	 * @param scp	The {@link SecretChangeProposal} instance
	 * @return		true
	 */
	public boolean add(SecretChangeProposal scp) {
		return changes.add(scp);
	}
	
	/**
	 * @return true if the proposal is already realized and false otherwise
	 */
	public boolean isRealized() {
		if(realized)
			return true;
		for(SecretChangeProposal scp : changes) {
			if(!scp.isRealized())	return false;
		}
		return realized = true;
	}
	
	/**
	 * Realizes the change proposed in this structure by calling all the realize()
	 * method of all the atomic {@link SecretChangeProposal}s.
	 */
	public void realize() {
		for(SecretChangeProposal scp : changes) {
			scp.realize();
		}
		realized = true;
	}
	
	/**
	 * Processes the distance between the original values of the secrecy knowledge
	 * and the one proposed in this data-structure.
	 * @param bof	The belief-operator-family used for the secrets
	 * @return		A double representing the distance between the original and the
	 * 				proposed secrets.
	 */
	public double distance(BeliefOperatorFamily bof) {
		double reval = 0;
		for(SecretChangeProposal scp : changes)
			reval += scp.distance(bof);
		return reval;
	}
}
