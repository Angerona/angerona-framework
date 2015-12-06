package com.github.kreaturesfw.secrecy;

import java.util.Map;

import com.github.kreaturesfw.core.operators.BeliefOperatorFamily;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;

/**
 * This data structure stores the proposed changes on a secret, such that
 * the secret is safe again. Therefore it either stores a new reasoner class
 * name or another map of settings for the reasoner.
 * 
 * It can also process the distance between the proposed secret belief operator
 * and the source belief operator, therefore it uses the belief operator family
 * as visitor.
 * 
 * @author Tim Janus
 */
public class SecretChangeProposal {
	/** the secret instance */
	private Secret secret;
	
	/** either the proposed or the old reasoner class depending on the realized flag*/
	private String reasoner;
	
	/** either the proposed or the old settings depending on the realized flag */
	private Map<String, String> settings;
	
	/** A flag indicating if the proposal is already realized */
	boolean realized = false;
	
	/**
	 * Creates a proposal that prospose to change the user reasoner
	 * to the class given in newReasoner
	 * @param secret		The secret that shall be changed
	 * @param newReasoner	The class name of the new reasoner
	 */
	public SecretChangeProposal(Secret secret, String newReasoner) {
		if(secret == null)
			throw new IllegalArgumentException();
		this.secret = secret;
		if(secret.getReasonerClassName().equals(newReasoner))
			throw new IllegalArgumentException();
		this.reasoner = newReasoner;
		this.settings = secret.getReasonerSettings();
	}
	
	/**
	 * Creates a proposal that proposes to change the settings of the
	 * reasoner ot the given settings
	 * @param secret		The secret that shall be changed
	 * @param settings		The new map of settings for the reasoner
	 */
	public SecretChangeProposal(Secret secret, Map<String, String> settings) {
		if(secret==null)
			throw new IllegalArgumentException();
		this.secret = secret;
		if(secret.getReasonerSettings().equals(settings))
			throw new IllegalArgumentException();
		this.settings = settings;
		this.reasoner = secret.getReasonerClassName();
	}
	
	/**
	 * Realizes the proposed changes by adapting the secret
	 */
	public void realize() {
		if(!realized) {
			String treasoner = this.secret.getReasonerClassName();
			Map<String,String> tsettings = this.secret.getReasonerSettings();
			this.secret.setReasonerClassName(reasoner);
			this.secret.setReasonerSettings(settings);
			reasoner = treasoner;
			settings = tsettings;
			realized = true;
		}
	}
	
	/** @return true if the proposed changes are already realized, false otherwise */
	public boolean isRealized() {
		return realized;
	}
	
	/**
	 * Processes the distance between the proposed belief operator and the prior belief operator,
	 * therefore the belief operator family is used.
	 * @param visitor	The belief operator family used to process the distance
	 * @return			The distance as a double value
	 */
	public double distance(BeliefOperatorFamily visitor) {
		OperatorCallWrapper from = visitor.getOperator(	
				realized ? secret.getReasonerClassName() : reasoner, 
				realized ? secret.getReasonerSettings() : settings);
		
		OperatorCallWrapper to = visitor.getOperator(	
				!realized ? secret.getReasonerClassName() : reasoner, 
				!realized ? secret.getReasonerSettings() : settings);
		
		return visitor.distance(from, to);
	}
}
