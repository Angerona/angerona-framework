package angerona.fw.logic.asp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Simply adds the new rule to the belief base, can make it inconsistent and
 * so on.
 * @author Tim Janus
 */
public class AspExpansion extends BaseChangeBeliefs {

	/** The logger used for output in the angerona Framework */
	static private Logger LOG = LoggerFactory.getLogger(AspExpansion.class);
	
	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return AspBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		LOG.info("Perform ASPExpansion as change.");
		AspBeliefbase abb = (AspBeliefbase)param.getBeliefBase();
		Program p = abb.getProgram();
		
		Iterator<FolFormula> it = param.getNewKnowledge().iterator();
		while(it.hasNext()) {
			FolFormula act = it.next();
			List<Literal> head = new LinkedList<Literal>();
			List<Literal> body = new LinkedList<Literal>();
			
			if(act instanceof Atom) {
				Atom atom = (Atom)act;
				String name = atom.getPredicate().getName();
				head.add(new net.sf.tweety.logicprogramming.asplibrary.syntax.Atom(name));
			} else if(act instanceof Negation) {
				Negation n = (Negation)act;
				Atom atom = (Atom)n.getFormula();
				String name = atom.getPredicate().getName();
				head.add(new net.sf.tweety.logicprogramming.asplibrary.syntax.Neg(
						new net.sf.tweety.logicprogramming.asplibrary.syntax.Atom(name)));
			}
			p.add(new Rule(head, body));
		}
		
		return param.getBeliefBase();
	}

}
