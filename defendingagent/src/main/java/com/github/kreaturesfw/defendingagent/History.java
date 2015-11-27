package com.github.kreaturesfw.defendingagent;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.comm.Update;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.reflection.FolFormulaVariable;

public class History implements GeneralHistory{

	class Tupel<T, U>
	{
	   T action;
	   U answer;

	   Tupel(T a, U b)
	   {
		action = a;
	    answer = b;
	   }

	   T getAction(){ return action;}
	   U getAnswer(){ return answer;}
	}
	
	private LinkedList<Tupel<Perception, AnswerValue>> history = new LinkedList<Tupel<Perception, AnswerValue>>();
	
	public History() {};
	
	/** Copy Ctor */
	public History(History other) {
		Iterator<Tupel<Perception, AnswerValue>> iterator = other.history.iterator();
		while(iterator.hasNext()) {
			Tupel<Perception, AnswerValue> tupel = iterator.next();
			Perception perception = tupel.getAction();
			if(perception instanceof Query){
				history.add(new Tupel<Perception, AnswerValue>(new Query(((Query)perception).getSenderId(), ((Query)perception).getReceiverId(), new FolFormulaVariable(((Query)perception).getQuestion())), tupel.getAnswer()));
			}else if(perception instanceof Update){
				history.add(new Tupel<Perception, AnswerValue>(new Update(((Update)perception).getSenderId(), ((Update)perception).getReceiverId(), ((Update)perception).getProposition()), tupel.getAnswer()));
			}
		}
	}
	
	public void putAction(Perception perception, AnswerValue answer) {
		history.add(new Tupel<Perception, AnswerValue>(perception, answer));
	}
	
	public LinkedList<Tupel<Perception, AnswerValue>> getHistory(){
		return history;
	}
	
	public String toString(){
		String reval = "History:";
		for(Tupel<Perception, AnswerValue> entry : history){
			reval += "<" + entry.getAction() + ", " + entry.getAnswer() + ">";
		}
		return reval;
	}
}
