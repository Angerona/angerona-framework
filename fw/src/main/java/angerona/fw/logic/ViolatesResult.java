package angerona.fw.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.util.Pair;

/** A mapping between Secrets and doubles representing a secret's in strength
 * @author Tim Janus, Daniel Dilger
 */
public class ViolatesResult {
	/** a list of pairs of secrets mapped to their degree of weaking */
	private List<Pair<Secret, Double>> pairs;
	
	/** flag indicating if everything is alright (no secret weakend) */
	private boolean alright;
	
	/** Default Ctor: Everything is allright */
	public ViolatesResult() {
		this(true);
	}
	
	public ViolatesResult(boolean alright) {
		this.alright = alright;
		pairs = new LinkedList<>();
	}
	
	public ViolatesResult(Pair<Secret, Double> pair) {
		alright = pair.second == 0;
		pairs = new LinkedList<>();
		pairs.add(pair);
	}
	
	/** 
	 * CTor getting a list of secret-degreeOfWeakening Pairs.
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
	 * @see Secret.alike
	 * @param other		Reference to the other ViolatesResult to combine.
	 * @return
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
}
