package com.github.kreaturesfw.secrecy.example.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.kreaturesfw.asp.component.AspMetaKnowledge;
import com.github.kreaturesfw.asp.component.AspMetaKnowledge.ConstantTriple;
import com.github.kreaturesfw.asp.logic.AspBeliefbase;
import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.basic.AgentComponent;
import com.github.kreaturesfw.core.basic.BaseAgentComponent;
import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.secrecy.Secret;
import com.github.kreaturesfw.secrecy.components.SecrecyKnowledge;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.NumberTerm;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.translators.aspfol.AspFolTranslator;
import net.sf.tweety.lp.asp.syntax.Arithmetic;
import net.sf.tweety.lp.asp.syntax.Comparative;
import net.sf.tweety.lp.asp.syntax.DLPAtom;
import net.sf.tweety.lp.asp.syntax.DLPNot;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;

/**
 * This component models the attacker as described in MATES13 of Krümpelmann.
 * That means it depends on the secrecy agent model and also on ASP as knowledge
 * representation mechanism. 
 * 
 * It  depends on the components: 
 * - {@link AspMetaKnowledge}
 * - {@link SecrecyKnowledge}
 * 
 * @author Tim Janus
 */
public class MatesAttackerModelling extends BaseAgentComponent {

	/** the secrecy knowledge of the agent */
	SecrecyKnowledge secrecyKnowledge;
	
	/** the meta knowledge of the agent */
	AspMetaKnowledge metaKnowledge;
	
	/** a map of agent names to programs representing the current attacker modeling for that agent view */
	private Map<String, Program> attackerModels = new HashMap<>();
	
	/** Default Ctor */
	public MatesAttackerModelling() {}
	
	/** Copy-Ctor */
	public MatesAttackerModelling(MatesAttackerModelling other) {}
	
	@Override
	public void init(Map<String, String> additionalData) { 
		secrecyKnowledge = getAgent().getComponent(SecrecyKnowledge.class);
		metaKnowledge = getAgent().getComponent(AspMetaKnowledge.class);
	
		onInit();
		super.init(additionalData);
		
		onSecrecyChanged();
	}
	
	@Override
	public void componentInitialized(AgentComponent comp) {
		if(comp == secrecyKnowledge || comp == metaKnowledge)
			onSecrecyChanged();
	}
	
	/**
	 * 	This method adds the MATES attacker modeling to the world knowledge and
	 * 	the views of the component's agent. As the name indicates it only adds the
	 * 	Initialization specific rules like 'mi_refused' and 'mi_sensetive'.
	 * 	This method gets called when the component is initialized.
	 */
	private void onInit() {
		// Prepare a set of all mi_agent(*) facts:
		Set<DLPAtom> agentFacts = new HashSet<>();
		for(Agent ag : getAgent().getEnvironment().getAgents()) {
			DLPAtom agentFact = new DLPAtom("mi_agent", new Constant("a_" + ag.getName()));
			agentFacts.add(agentFact);
		}
		
		
		// Add the attacker modeling to the world knowlede
		BaseBeliefbase world = getAgent().getBeliefs().getWorldKnowledge();
		if(world instanceof AspBeliefbase) {
			Program attackerModel = new Program();
			String name = getAgent().getName();
			attackerModels.put(name, attackerModel);
			
			// add information about other agents:
			for(DLPAtom fact : agentFacts) {
				attackerModel.addFact(fact);
			}
			
			// add attacker modeling
			addAttackerModelling((AspBeliefbase)world, name);
			
		}
		
		// Add information about agents and attacker modeling to every view:
		for(Entry<String, BaseBeliefbase> entry : getAgent().getBeliefs().getViewKnowledge().entrySet()) {
			if(! (entry.getValue() instanceof AspBeliefbase))
				continue;
			AspBeliefbase view = (AspBeliefbase)entry.getValue();
			
			Program attackerModel = new Program();
			attackerModels.put(entry.getKey(), attackerModel);
			
			for(DLPAtom agentFact : agentFacts) {
				attackerModel.addFact(agentFact);
			}
			
			// add attacker modeling
			addAttackerModelling(view, entry.getKey());
		}	
	}
	
	/**
	 * This methods adds the general attacker modeling to the given belief base, therefore the 
	 * @param beliefbase
	 * @param programKey
	 * @param defender
	 */
	private void addAttackerModelling(AspBeliefbase beliefbase, String programKey) {
		// remove attacker modeling first:
		Program program = attackerModels.get(programKey);
		beliefbase.getProgram().removeAll(program);
		
		// generate sensitive rule:
		Rule sensetive = new Rule() ;
		sensetive.setConclusion(new DLPAtom("mi_sensetive", new Variable("D"), new Variable("V")));
		sensetive.addPremise(new DLPAtom("mi_refused", new Variable("D"), new Variable("V")));
		program.add(sensetive);
		
		// generate the agent terms:
		Variable attacker = new Variable("A");
		Variable defender = new Variable("D");
		
		// the refuse rule:
		Rule refuse = new Rule();
		refuse.setConclusion(new DLPAtom("mi_refused", defender, new Variable("V")));
		refuse.addPremise(new DLPAtom("mi_sact",
				new Constant("t_" + Query.class.getSimpleName()), 
				attacker,			// sender
				new Variable("V"),		// question
				new Variable("T1")));	// point in time of question
		
		refuse.addPremise(new DLPNot(new DLPAtom("mi_sact",
				new Constant("t_" + Answer.class.getSimpleName()),
				defender,			// sender
				new Variable("W"),		// information
				new Variable("T2")		// the the point in time for the answer
				)));
		
		// use variable response time for the rule.
		int tResponse = 1;
		refuse.addPremise(new DLPAtom("mi_related", new Variable("V"), new Variable("W")));
		refuse.addPremise(new DLPAtom("mi_time", new Variable("T2")));
		refuse.addPremise(new DLPAtom("mi_agent", attacker));
		refuse.addPremise(new DLPAtom("mi_agent", defender));
		refuse.addPremise(new Arithmetic("+", new Variable("T1"), new NumberTerm(tResponse), new Variable("T2")));
		refuse.addPremise(new Comparative("!=", new Variable("A"), new Variable("D")));
		program.add(refuse);

		// the holds rule:
		Rule holds = new Rule();
		holds.setConclusion(new DLPAtom("mi_holds", new Variable("S")));
		holds.addPremise(new DLPAtom("mi_has_secret", new Variable("D"), new Variable("S")));
		holds.addPremise(new DLPAtom("mi_refused", new Variable("D"), new Variable("S")));
		program.add(holds);

		beliefbase.getProgram().add(program);
	}
	
	/**
	 * This method shall be called every time the secrecy knowledge changes
	 * @todo register listeners
	 */
	private void onSecrecyChanged() {
		if(!secrecyKnowledge.isInitialized() || !metaKnowledge.isInitialized() || !isInitialized()) 
			return;
		
		for(Secret s : secrecyKnowledge.getSecrets()) {
			Constant symbol = null;
			ConstantTriple tripel = null;
			Constant agentName = new Constant("a_" + this.getAgent().getName());
			
			// the attackers belief base:
			BaseBeliefbase viewAttacker = getAgent().getBeliefs().getViewKnowledge().get(s.getSubjectName());
			if(! (viewAttacker instanceof AspBeliefbase)) {
				continue;
			}
			
			// process the symbol used in meta knowledge for the secret piece of information:
			AspFolTranslator translator = new AspFolTranslator();
			if(s.getInformation() instanceof FOLAtom) {
				DLPAtom atom = translator.toASP((FOLAtom)s.getInformation());
				tripel = metaKnowledge.getOrCreateTriple(atom);
				symbol = tripel.posConst;
			} else if(s.getInformation() instanceof Negation) {
				FOLAtom fAtom = (FOLAtom)((Negation)s.getInformation()).getFormula();
				DLPAtom dlpAtom = translator.toASP(fAtom);
				tripel = metaKnowledge.getOrCreateTriple(dlpAtom);
				symbol = tripel.negConst;
			}
			
			// continue with next secret if no symbol can be found
			if(symbol == null || tripel == null) {
				continue;
			}

			Program attackerModel = new Program();
			
			// the has_secret fact:
			attackerModel.addFact(new DLPAtom("mi_has_secret", agentName, symbol));
			
			
			/*
			// the holds rule:
			Rule holds = new Rule();
			holds.setConclusion(new DLPAtom("mi_holds", symbol));
			holds.addPremise(new DLPAtom("mi_has_secret", agentName, symbol));
			holds.addPremise(new DLPAtom("mi_refused", agentName, symbol));
			attackerModel.add(holds);
			*/
			
			((AspBeliefbase)viewAttacker).getProgram().add(attackerModel);
			this.report("Extends attacker modelling for Secret: '" + s.toString() + "'", viewAttacker);
		}
		
		if(!isInitialized())
			initalized = true;
	}
	
	@Override
	public MatesAttackerModelling clone() {
		return new MatesAttackerModelling(this);
	}

}
