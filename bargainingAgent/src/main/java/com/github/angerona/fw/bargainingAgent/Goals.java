package com.github.angerona.fw.bargainingAgent;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;

/**
 * Stores the Goals of the agent sorted by priority
 * 
 * @author Pia Wierzoch
 *
 */
public class Goals extends BaseAgentComponent{
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(Goals.class);
	
	private TreeMap<Integer, FolFormula> goals = new TreeMap<Integer, FolFormula>();
	
	int actualGoal, lastGoal;
	
	/** Default Ctor: Used for dynamic creation, creates empty goal list */
	public Goals() {}
	
	/** Copy Ctor */
	public Goals(Goals other) {
		super(other);
	}

	/**
	 * Deletes the best goal in the list and returns it
	 * @return best goal
	 */
	public FolFormula deletBestGoal() {
		lastGoal = actualGoal;
		actualGoal = goals.lowerKey(actualGoal);
		return goals.get(actualGoal);
	}
	
	/**
	 * Adds the goal to the list of goals with priority key and returns the best goal of the list after the modification
	 * @param key
	 * @param goal
	 */
	public FolFormula addToGoals(Integer key, FolFormula goal){		
		if(key > actualGoal){
			lastGoal = actualGoal;
			actualGoal = key;
			return goals.get(actualGoal);
		}
		return (goals.get(actualGoal));
	}
	
	/**
	 * Return the best goal
	 */
	public FolFormula getBestGoal(){
		return goals.get(actualGoal);
	}
	
	/**
	 * Return the priority of the best goal in the list
	 */
	public int getBestGoalPrio(){
		return actualGoal;
	}
	
	/**
	 * If goal is in the list make it the best goal if it has the highest priority
	 * @param goal
	 * @return
	 */
	public boolean setBestGoal(FolFormula goal){
		for(Entry<Integer, FolFormula> entry: goals.entrySet()){
			if(entry.getValue().equals(goal)){
				if(actualGoal <= entry.getKey()){
					actualGoal = entry.getKey();
					return true;
				}else{
					break;
				}
			}
		}
		return false;
	}
	
	@Override
	public BaseAgentComponent clone() {
		return new Goals(this);
	}
	
	public Collection<FolFormula> getGoals(){
		return goals.values();
	}
	
	public TreeMap<Integer, FolFormula> getGoalList(){
		return goals;
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		if(additionalData.containsKey("Goals")) {
			String g = additionalData.get("Goals");
			String[] goalList, priorityList, temp = g.split(";");
			goalList = temp[0].split(",");
			priorityList = temp[1].split(",");
			for(int i = 0; i< goalList.length;i++){
				FolFormula f = new  FOLAtom(new Predicate(goalList[i])); 
				Integer p = new Integer(priorityList[i]);
				goals.put(p, f);
				LOG.info("Added Goal " + f + " with priority " + p + " to the list of goals of Agent " + this.getAgent().getName() + ".");
			}
			actualGoal = goals.lastKey();
			lastGoal = actualGoal;
		}
	}

}
