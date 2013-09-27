package com.github.angerona.knowhow.graph;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.util.Utility;
import com.github.angerona.knowhow.graph.parameter.Parameter;

public class ActionAdapter extends Action {

	private static Logger LOG = LoggerFactory.getLogger(ActionAdapter.class);
	
	private String actionName;
	
	private List<Parameter> parameters;
	
	private Perception context;
	
	public ActionAdapter(Agent sender, String actionName, List<Parameter> parameters, Perception context) {
		super(sender, Action.ALL);
		this.actionName = actionName;
		this.parameters = parameters;
		this.context = context;
	}

	public Action evaluateAction() {
		Action reval = null;
		if(actionName.equals("Inform")) {
			reval = createInform(parameters);
		} else if(actionName.equals("Query")) {
			reval = createQuery(parameters);
		} else if(actionName.equals("QueryAnswer")) {
			if(! (context instanceof Query))
				throw new IllegalStateException("Context of Answer is no 'Query' but '" + context.getClass().getSimpleName() + "'");
			reval = createAnswer(parameters, (Query)context);
		} else {
			throw new NotImplementedException("Generation of Action '" + actionName + "' not implemented yet");
		}
		reval.setParent(getParent());
		return reval;
	}
	
	@Override
	public String toString() {
		return "<ActionAdapter(" + actionName +") - if shown in UI an error occured.>";
	}
	
	private Answer createAnswer(List<Parameter> parameters, Query context) {
		if(parameters.size() != 1) {
			throw new IllegalStateException("The parameter count for an Answer must be '1' not '" + parameters.size() + "'");
		}
		
		// TODO this bunch of code is duplo, create a factory somewhere:
		if(context.getQuestion().isGround()) {
			AnswerValue simpleAnswer = agent.getBeliefs().getWorldKnowledge().reason(context.getQuestion()).getAnswerValue();		
			Parameter param = parameters.get(0);
			if(param.getIdentifier().equals("p_honest")) {
				// do nothing
			} else if(param.getIdentifier().equals("p_lie")) {
				simpleAnswer = Utility.lie(simpleAnswer);
			} else {
				throw new NotImplementedException("The honesty type: '" + param.getIdentifier().substring(2) + "' is not implemented yet.");
			}
			
			return new Answer(this.agent, context.getSenderId(), context.getQuestion(), 
					new AngeronaAnswer(context.getQuestion(), simpleAnswer));
		}
		
		throw new NotImplementedException("Cannot handle the answer on open queries yet.");
	}
	
	private Query createQuery(List<Parameter> parameters) {
		if(parameters.size() != 2) {
			throw new IllegalStateException("A Query has '2' Parameters not '" + parameters.size() + "'");
		}
		
		Parameter recvP = parameters.get(0);
		Parameter questionP = parameters.get(1);
		
		Query query = new Query(agent, mapAgent(recvP), mapFormula(questionP));
		LOG.info("Created Action: '" + query.toString() + "'");
		return query;
	}
	
	private Inform createInform(List<Parameter> parameters) {
		if(parameters.size() != 2) {
			throw new IllegalStateException("An Inform has '2' Parameters not '" + parameters.size() + "'");
		}
		
		Parameter recvP = parameters.get(0);
		Parameter formulaP = parameters.get(1);
		
		Set<FolFormula> formulas = new HashSet<>();
		formulas.add(createFormula(formulaP));
		Inform inform = new Inform(agent.getName(), 
				mapAgent(recvP), formulas);
		

		return inform;
	}
	
	private FolFormula createFormula(Parameter param) {
		FolParserB parser = new FolParserB(new StringReader(param.getIdentifier()));
		FolFormula reval = null;
		try {
			reval = parser.formula(new FolSignature());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reval;
	}
	
	private FolFormula mapFormula(Parameter param) {
		if(param.getIdentifier().startsWith("q_")) {
			FolFormula question = createFormula(new Parameter(param.getIdentifier().substring(2)));
			if(question.isClosed()) {
				throw new IllegalStateException("The query prefix 'q_' for formulas only allows open queries but" +
						" '" + question.toString() + "' is not open.");
			}
			
			AngeronaAnswer answer = this.agent.getBeliefs().getWorldKnowledge().reason(question);
			if(!answer.getAnswers().isEmpty()) {
				FolFormula reval = answer.getAnswers().iterator().next();
				return reval;
			}
			
			LOG.warn("There was no match found for the query '" + question.toString() + "' using question for query");
			return question;
		} else {
			return createFormula(param);
		}
	}
	
	private String mapAgent(Parameter param) {
		String reval = param.getIdentifier().substring(2);
		return reval.equals("SELF") ? agent.getName() : reval;
	}
}
