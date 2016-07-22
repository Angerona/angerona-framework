package com.github.angerona.fw.bargainingAgent.operators;

import java.util.HashSet;
import java.util.TreeMap;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.bargainingAgent.Goals;
import com.github.angerona.fw.bargainingAgent.HistoryComponent;
import com.github.angerona.fw.bargainingAgent.Options;
import com.github.angerona.fw.bargainingAgent.comm.Offer;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.BaseUpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

/**
 * Defensive Update Belief operator only handles proactive Answer and RevisionAnswer and UpdateAnswer speech acts
 * send by the defending agent. Whenever the answer is not a rejection, this operator
 * updates the view and belief base according to the revision/request result calculated by the censor
 * component.
 * 
 * @author Pia Wierzoch
 */
public class UpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	
	@Override
	protected Beliefs processImpl(EvaluateParameter param) {
		LOG.info("Run Bargaining-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs)param.getBeliefs().clone();
		String id = param.getAgent().getName();
		SpeechAct act = (SpeechAct)param.getAtom();
		
		if(act == null) {
			return beliefs;
		}
		
		Agent ag = Angerona.getInstance().getActualSimulation().getAgentByName(act.getReceiverId());
		HistoryComponent history = ag.getComponent(HistoryComponent.class);
		
		String out = "Received perception: " + act.toString();
		// Update History with incomming Offer
		if(!id.equals(act.getSenderId())) {
			if(act instanceof Offer) {
				Offer offer = (Offer) act;
				history.putEntry(offer, new TreeMap<Integer, FolFormula>());
			}
			param.report(out);
			return beliefs;
		}
		if(act instanceof Offer) {
			// counter offer is a response to a offer
			Offer counterOffer = (Offer) act;
			 
			// test if agreement is achieved
			HashSet<FolFormula> demands_this = counterOffer.getDemandsAndPromises().getFirst();
			HashSet<FolFormula> promises_this = counterOffer.getDemandsAndPromises().getLast();
			
			Offer offer = history.getHistory().getLast().getFirst();
			
			HashSet<FolFormula> demands_opponent = offer.getDemandsAndPromises().getFirst();
			HashSet<FolFormula> promises_opponent = offer.getDemandsAndPromises().getLast();

			if(promises_this.containsAll(demands_opponent) && 
			   demands_opponent.containsAll(promises_this) && 
			   promises_opponent.containsAll(demands_this) && 
			   demands_this.containsAll(promises_opponent)){
				//agreement achievd
				//TODO update belief und goals anpassen oder aufhören?
				return beliefs;
			}
			
			Goals goals = ag.getComponent(Goals.class);
			
			if(promises_opponent.isEmpty() && promises_this.isEmpty()){
				Options options = ag.getComponent(Options.class);
				options.getNextOptions();
				if(options.getOptions().isEmpty()){
					FolFormula newGoal = goals.deletBestGoal();
					options.initialize(newGoal, ag.getBeliefs().getWorldKnowledge());
				}
			}
			
			history.putEntry(counterOffer, goals.getGoalList());
		}
		
		// Inform agent listeners about update invocation
		if(beliefs.getCopyDepth() == 0) {
			param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
		}
		
		return beliefs;
	}
}
