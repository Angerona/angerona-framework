package net.sf.tweety.logics.conditionallogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.propositionallogic.semantics.PossibleWorld;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class models a brute force c-reasoner for conditional logic. Reasoning is performed
 * by computing a minimal c-representation for the given knowledge base.<br>
 * 
 * A c-representation for a conditional knowledge base R={r1,...,rn} is a ranking function k such that
 * k accepts every conditional in R (k |= R) and if there are numbers k0,k1+,k1-,...,kn+,kn- with<br>
 * 
 * k(w)=k0 + \sum_{w verifies ri} ki+ + \sum_{w falsifies ri} kj-
 * 
 * for every w. A c-representation is minimal if k0+...+kn- is minimal.<br>
 * 
 * The c-representation is computed using a brute force approach.
 * 
 * <br><br>See Gabriele Kern-Isberner. Conditionals in nonmonotonic reasoning and belief revision.
 * Lecture Notes in Computer Science, Volume 2087. 2001.
 * @author Matthias Thimm
 */
public class BruteForceCReasoner extends Reasoner {

	/** Logger. */
	static private Logger log = LoggerFactory.getLogger(BruteForceCReasoner.class);	
	
	/**
	 * The c-representation for the given knowledge base. Once this
	 * ranking function has been computed it is used for
	 * subsequent queries in order to avoid unnecessary
	 * computations.
	 */
	private RankingFunction crepresentation = null;
	
	/**
	 * The current vectors of kappa values.
	 */
	private List<Integer[]> kappa;
	
	/**
	 * The number of conditionals in the given knowledge base.
	 */
	private int numConditionals;
	
	/**
	 * Maps the indices of the kappa vector to their corresponding
	 * conditionals. 
	 */
	private Map<Integer,Conditional> indexToConditional;
	
	/**
	 * indicates whether the computed c-representation is simple.
	 */
	private boolean simple = false;
	
	/**
	 * Creates a new c-representation reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.	
	 * @param simple whether the computed c-representation is simple. 
	 */
	public BruteForceCReasoner(BeliefBase beliefBase, boolean simple){
		super(beliefBase);		
		if(!(beliefBase instanceof ClBeliefSet))
			throw new IllegalArgumentException("Knowledge base of class ClBeliefSet expected.");
		this.simple = simple;
	}
	
	/**
	 * Creates a new simple c-representation reasoner for the given knowledge base.
	 * @param beliefBase  a knowledge base.	
	 */
	public BruteForceCReasoner(BeliefBase beliefBase){
		this(beliefBase,false);
	}
	
	/**
	 * Returns the c-representation this reasoner bases on.
	 * @return the c-representation this reasoner bases on.
	 */
	public RankingFunction getCRepresentation(){
		if(this.crepresentation == null)
			this.crepresentation = this.computeCRepresentation();
		return this.crepresentation;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Reasoner#query(net.sf.tweety.kr.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof Conditional) && !(query instanceof PropositionalFormula))
			throw new IllegalArgumentException("Reasoning in conditional logic is only defined for conditional and propositional queries.");
		RankingFunction crepresentation = this.getCRepresentation();
		if(query instanceof Conditional){
			Answer answer = new Answer(this.getKnowledgBase(),query);
			boolean bAnswer = crepresentation.satisfies(query);
			answer.setAnswer(bAnswer);
			answer.appendText("The answer is: " + bAnswer);
			return answer;			
		}
		if(query instanceof PropositionalFormula){
			int rank = crepresentation.rank((PropositionalFormula)query);
			Answer answer = new Answer(this.getKnowledgBase(),query);			
			answer.setAnswer(new Double(rank));
			answer.appendText("The rank of the query is " + rank + " (the query is " + ((rank==0)?(""):("not ")) + "believed)");
			return answer;
		}			
		return null;
	}
	
	/**
	 * Computes a minimal c-representation for this reasoner's knowledge base. 
	 * @return a minimal c-representation for this reasoner's knowledge base.
	 */
	private RankingFunction computeCRepresentation(){		
		this.numConditionals = ((ClBeliefSet)this.getKnowledgBase()).size();
		int i = 0;
		this.indexToConditional = new HashMap<Integer,Conditional>();
		for(Formula f: ((ClBeliefSet)this.getKnowledgBase())){
			this.indexToConditional.put(i++, (Conditional) f);
			if(!this.simple)
				this.indexToConditional.put(i++, (Conditional) f);
		}
		Integer[] kappa = null;		
		RankingFunction candidate = this.constructRankingFunction(kappa);
		while(!candidate.satisfies(this.getKnowledgBase())){
			kappa = this.increment(kappa);			
			candidate = this.constructRankingFunction(kappa);
//			String debugMessage = "["+kappa[0];
//			for(int j=1; j< kappa.length;j++)
//				debugMessage += "," + kappa[j];
//			debugMessage += "]";
//			BruteForceCReasoner.log.debug(debugMessage);
		}		
		candidate.normalize();
		return candidate;
	}
	
	/**
	 * Constructs a ranking function with the given kappa values [k1+,k1-,...,kn+,kn-], i.e.
	 * for every possible world w set<br>
	 * k(w)=k0 + \sum_{w verifies ri} ki+ + \sum_{w falsifies ri} kj-
	 * @param kappa
	 * @return
	 */
	private RankingFunction constructRankingFunction(Integer[] kappa){
		RankingFunction candidate = new RankingFunction((PropositionalSignature)this.getKnowledgBase().getSignature());
		if(kappa == null) 
			return candidate;
		for(PossibleWorld w: candidate.getPossibleWorlds()){
			int sum = 0;
			if(this.simple){
				for(int idx = 0; idx < kappa.length; idx++){
					if(RankingFunction.falsifies(w, this.indexToConditional.get(idx))){
						sum +=kappa[idx];
					}
				}					
			}else{
				for(int idx = 0; idx < kappa.length; idx+=2){
					if(RankingFunction.verifies(w, this.indexToConditional.get(idx))){
						sum +=kappa[idx];
					}
				}
				for(int idx = 1; idx < kappa.length; idx+=2){
					if(RankingFunction.falsifies(w, this.indexToConditional.get(idx))){
						sum +=kappa[idx];
					}
				}
			}
			candidate.setRank(w, sum);
		}
		return candidate;
	}
	
	/**
	 * This method increments the given array by one value.
	 * @param kappa an array of integers.
	 * @return an array of integers.
	 */
	private Integer[] increment(Integer[] kappa){
		if(this.kappa == null){
			Integer[] first;
			if(this.simple)
				first = new Integer[this.numConditionals];
			else
				first = new Integer[2*this.numConditionals];
			first[0] = 1;
			for(int i = 1; i < first.length; i++)
				first[i] = 0;
			this.kappa = new ArrayList<Integer[]>();
			this.kappa.add(first);			
		}else{
			boolean overflow = true;
			int j = 0;
			while(overflow && j < this.kappa.size()){
				overflow = this.incrementStep(this.kappa.get(j));
				j++;
			}
			if(overflow){
				//add new vector
				Integer[] newVec;
				if(this.simple)
					newVec= new Integer[this.numConditionals];
				else
					newVec= new Integer[2*this.numConditionals];
				newVec[0] = 1;
				for(int i = 1; i < newVec.length; i++)
					newVec[i] = 0;
				this.kappa.add(newVec);	
			}
		}
		//compute the actual kappa values
		Integer[] newKappa;
		if(this.simple)
			newKappa = new Integer[this.numConditionals];
		else
			newKappa = new Integer[2*this.numConditionals];
		for(int i = 0; i < newKappa.length; i++){
			newKappa[i] = 0;
			for(Integer[] v: this.kappa)
				newKappa[i] += v[i];
		}
		return newKappa;
	}
	
	/**
	 * This method increments the given vector (which composes of exactly
	 * one "1" entry and zeros otherwise), e.g. [0,0,1,0] -> [0,0,0,1]
	 * and [0,0,0,1] -> [1,0,0,0] 
	 * @param kappaRow a vector of zeros and one "1"
	 * @return "true" if there is an overflow, i.e. when [0,0,0,1] -> [1,0,0,0], otherwise
	 *  "false". 
	 */
	private boolean incrementStep(Integer[] kappaRow){
		int length = kappaRow.length;
		if(kappaRow[length-1] == 1){
			kappaRow[length-1] = 0;
			kappaRow[0] = 1;
			return true;
		}else{
			for(int i = 0; i< length-1; i++){
				if(kappaRow[i] == 1){
					kappaRow[i] = 0;
					kappaRow[i+1] = 1;
					return false;
				}
			}
		}
		throw new IllegalArgumentException("Argument must contain at least one value \"1\"");
	}
}
