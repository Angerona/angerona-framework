package angerona.fw.asp;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Not;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.internal.BeliefbasePlugin;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspExpansion;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.logic.asp.AspRevision;
import angerona.fw.logic.asp.AspTranslator;

@PluginImplementation
public class AspPlugin implements BeliefbasePlugin {

	@Override
	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(AspBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getSupportedReasoners() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(AspReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getSupportedChangeOperations() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(AspRevision.class);
		reval.add(AspExpansion.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends BaseTranslator>> getSupportedTranslators() {
		List<Class<? extends BaseTranslator>> reval = new LinkedList<Class<? extends BaseTranslator>>();
		reval.add(AspTranslator.class);
		return reval;
	}

	public static void main(String [] args) {
		Program p = new Program();
		
		Rule r = new Rule();
		r.addHead(new Atom("excused"));
		r.addBody(new Atom("attend_burial"));
		p.add(r);
		
		r = new Rule();
		r.addHead(new Atom("excused"));
		r.addBody(new Atom("is_ill"));
		p.add(r);
		
		r = new Rule();
		r.addHead(new Atom("fired"));
		r.addBody(new Neg( new Atom("attend_work")));
		r.addBody(new Not( new Atom("excused")));
		p.add(r);
		
		r = new Rule();
		r.addHead(new Neg(new Atom("fired")));
		r.addBody(new Atom("excused"));
		p.add(r);
		
		r = new Rule();
		r.addHead(new Neg(new Atom("attend_work")));
		p.add(r);
		
		AspBeliefbase bb = new AspBeliefbase();
		bb.setProgram(p);
		AspReasoner reasoner = new AspReasoner();
		
		System.out.println(p.toStringFlat());
		System.out.println();
		
		net.sf.tweety.logics.firstorderlogic.syntax.Atom a =
			new net.sf.tweety.logics.firstorderlogic.syntax.Atom(
				new net.sf.tweety.logics.firstorderlogic.syntax.Predicate("fired"));
		reasoner.query(bb, a);
		
		r = new Rule();
		r.addHead(new Atom("attend_burial"));
		p.add(r);
		
		reasoner.query(a);
		
		// Tutorial:
		/*
		number(1..4).
		orderedLists([X]) :- number(X).
		orderedLists([X|[Y|L]]) :- orderedLists([Y|L]), number(X), X < Y.
		*/
		
	}
}
