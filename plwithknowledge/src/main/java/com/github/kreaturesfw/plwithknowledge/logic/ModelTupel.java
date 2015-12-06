package com.github.kreaturesfw.plwithknowledge.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.pl.semantics.NicePossibleWorld;

/**
 * The models for a propositional beliefbase containing knowledge and assertions 
 * 
 * @author Pia Wierzoch
 *
 */
public class ModelTupel {
	
	
	private Set<NicePossibleWorld> modelsOfKnowledge = new HashSet<NicePossibleWorld>();

	private Set<NicePossibleWorld> models = new HashSet<NicePossibleWorld>();
	
	public ModelTupel(){
		
	}
	
	public ModelTupel(Set<NicePossibleWorld> modelsOfNonUpdatableFormulas,Set<NicePossibleWorld> models){
		this.modelsOfKnowledge = modelsOfNonUpdatableFormulas;
		this.models = models;
	}
	
	public Set<NicePossibleWorld> getModelsOfKnowledge(){
		return modelsOfKnowledge;
	}
	
	public void setModeksOfKnowledge(Set<NicePossibleWorld> models){
		this.modelsOfKnowledge = models;
	}
	
	public Set<NicePossibleWorld> getModels(){
		return models;
	}
	
	public void setModels(Set<NicePossibleWorld> models){
		this.models = models;
	}
	
	@Override
	public String toString(){
		String reval= "Models of Knowledge: \n";
		for(NicePossibleWorld world: modelsOfKnowledge){
			reval+= world.toString() + "\n";
		}
		reval+="Models of Assertions: \n";
		for(NicePossibleWorld world: models){
			reval+= world.toString() + "\n";
		}
		return reval;
	}
}