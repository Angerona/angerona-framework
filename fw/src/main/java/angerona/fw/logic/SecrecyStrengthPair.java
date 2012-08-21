package angerona.fw.logic;

import angerona.fw.logic.Secret;

/** A mapping between Secrets and doubles representing a secret's in strength
 * @author Daniel Dilger
 */
public class SecrecyStrengthPair {
	private Secret secret;
	private double degreeOfWeakening;
	public SecrecyStrengthPair()
	{
		
	}
	public SecrecyStrengthPair(Secret s, double d)
	{
		this.secret = s;
		this.degreeOfWeakening = d;
	}
	public void defineSecret(Secret secret)
	{
		this.secret = secret;
	}
	public void defineDegreeOfWeakening(double degreeOfWeakening)
	{
		this.degreeOfWeakening = degreeOfWeakening;
	}
	public Secret getSecret()
	{
		return this.secret;
	}
	public double getDegreeOfWeakening()
	{
		return this.degreeOfWeakening;
	}
}
