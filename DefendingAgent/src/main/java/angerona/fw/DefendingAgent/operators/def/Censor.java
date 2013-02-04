package angerona.fw.DefendingAgent.operators.def;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.AnswerValue;

/**
 * Implementation of the censor component of a defending censor agent.
 * Simulates the results of all possible answers to a query/revision request by an attacker.
 * If there is at least one possible answer, which would reveal a secret to the attacker,
 * the request is refused. This approach denies any form of meta-inference. See [1] for details and
 * proofs.
 * 
 * @author Sebastian Homann, Pia Wierzoch
 * @see [1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
 */
public class Censor {

	public Action processQuery(Agent ag, Query q) {

		return new Answer(ag, q.getReceiverId(), q.getQuestion(), AnswerValue.AV_REJECT);
	}
	
	public Action processRevision(Agent ag, Revision rev) {
		return new Answer(ag, rev.getReceiverId(), rev.getProposition(), AnswerValue.AV_REJECT);

	}
}
