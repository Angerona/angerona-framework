package angerona.fw.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.util.Pair;

/** 
 *	A data-structure containing information about the result of a violation processing.
 *	The base idea was given by Daniel Dilger's SecrecyStrength-Pair.
 *	The flag alright is used to determine if a secret has to be weaken or if no secrecy violation occurs.
 *	List of Pairs is used to save the needed weakening for the secrets to perform the action which was
 *	checked for violating secrecy. Every Pair consists of an Secret and a Double where the Secret references
 *	the Secret to be weaken and the double the amount of weakening. We assume that 1.0 is the strongest secret
 *	and 0.0 the weakest secret (no secret at all anymore). 
 *	This parameter between 0 and 1 can directly be mapped to the reasoner-parameter d. Which is defined for 
 *	AnswerSets for example.
 *
 *	The ViolatesResult data-structure can be combined with each other to support saving the ViolatesResult
 *	for performing multiple actions.
 *
 *	@author Tim Janus, 
 *  @author Daniel Dilger
 */
public class ViolatesResult implements Cloneable {
	/** a list of pairs of secrets mapped to their degree of weaking */
	private List<Pair<Secret, Double>> pairs;
	
	/** flag indicating if everything is alright (no secret weakend) */
	private boolean alright;
	
	/** Default Ctor: Assumes that everything is alright (no violation occurs) */
	public ViolatesResult() {
		this(true);
	}
	
	/** Copy-Ctor */
	public ViolatesResult(ViolatesResult other) {
		this.alright = other.alright;
		this.pairs = new LinkedList<>(other.pairs);
	}
	
	/**
	 * Set the alright flag but dont save more sophisticated information about secret weakening
	 * @param alright	flag indicating if an violation occured.
	 */
	public ViolatesResult(boolean alright) {
		this.alright = alright;
		pairs = new LinkedList<>();
	}
	
	/**
	 * CTor: Fill the ViolatesResult with the given Secret Pair, will also set the alright flag.
	 * @param pair	Secret-Weakening-Pair
	 */
	public ViolatesResult(Pair<Secret, Double> pair) {
		alright = pair.second == 0;
		pairs = new LinkedList<>();
		pairs.add(pair);
	}
	
	/** 
	 * CTor: getting a list of secret-degreeOfWeakening Pairs.
	 * Alright might be true if all the pairs have 0.0 as their second component.
	 * @param pairs		list of pairs.
	 */
	public ViolatesResult(List<Pair<Secret, Double>> pairs) {
		alright = true;
		for(Pair<Secret, Double> p : pairs) {
			if(p.second > 0) {
				alright = false;
				break;
			}
		}
		this.pairs = new LinkedList<>(pairs);
	}
	
	/** @return true if no secret was weaken */
	public boolean isAlright() {
		return alright;
	}
	
	/** @return unmodifiable list of pairs */
	public List<Pair<Secret, Double>> getPairs() {
		return Collections.unmodifiableList(pairs);
	}
	
	/**
	 * combines two ViolatesResult to one. If a Pair is only
	 * in one ViolatesResult it will also be in the combined Result. 
	 * If a pair is in both ViolatesResults the degreeOfWeaking will be added and 
	 * the modified pair will be saved in the
	 * combined ViolatesResult.
	 * Two pairs will be combined if the secrets are alike.
	 * @see angerona.fw.logic.Secret
	 * @param other		Reference to the other ViolatesResult to combine.
	 * @return	The combination of this ViolatesResult with one given as parameter.
	 */
	public ViolatesResult combine(ViolatesResult other) {
		List<Pair<Secret, Double>> l1 = new LinkedList<>();
		l1.addAll(this.pairs);
		for(Pair<Secret, Double> p1 : other.pairs) {
			boolean combined = false;
			for(Pair<Secret, Double> p2 : l1) {
				if(p1.first.alike(p2.first)) {
					p2.second += p1.second;
					combined = true;
					break;
				}
			}
			if(!combined) {
				l1.add(p1);
			}
		}
		
		return new ViolatesResult(l1);
	}
	
	@Override
	public Object clone() {
		return new ViolatesResult(this);
	}
}
