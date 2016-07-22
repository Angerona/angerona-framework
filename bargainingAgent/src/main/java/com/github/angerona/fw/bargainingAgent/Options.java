package com.github.angerona.fw.bargainingAgent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.Tautology;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.bargainingAgent.util.Tupel;
import com.github.angerona.fw.logic.asp.AspBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalBeliefbase;
import com.github.angerona.fw.logic.conditional.ConditionalRevision;
import com.github.angerona.fw.operators.OperatorCallWrapper;

/**
 * Stores the options of the agent
 * @author Pia Wierzoch
 */
public class Options extends BaseAgentComponent{

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(Options.class);
	
	private LinkedList<Tupel<HashSet<FolFormula>,HashSet<FolFormula>>> options = new LinkedList<>();
	
	
	/** Default Ctor: Used for dynamic creation, creates empty goal list */
	public Options() {}
	
	/** Copy Ctor */
	public Options(Options other) {
		super(other);
	}

	/**
	 * Initialize the list of options
	 * @param bestGoal
	 * @param bbase
	 */
	public void initialize(FolFormula bestGoal, BaseBeliefbase bbase){
		Goals goals = bbase.getAgent().getComponent(Goals.class);
		FolFormula bGoal = goals.getBestGoal();
		
		if(bbase instanceof AspBeliefbase){
			//TODO ASP
			
			
		}else if(bbase instanceof ConditionalBeliefbase){	
			ConditionalBeliefbase conditional = (ConditionalBeliefbase) bbase;
			List<HashSet<FolFormula>> supportSet = new LinkedList<HashSet<FolFormula>>();
			
			OperatorCallWrapper changeOp = conditional.getChangeOperator();
			ConditionalRevision revisionOp = null;
			if(changeOp.getImplementation() instanceof ConditionalRevision) {
				revisionOp = (ConditionalRevision) changeOp.getImplementation();
			}
			
			RuleBasedCReasoner reasoner = new RuleBasedCReasoner(conditional.getConditionalBeliefs());
			FOLPropTranslator translator = new FOLPropTranslator();
			
			reasoner.prepare();
			reasoner.process();
			RankingFunction rank = reasoner.getSemantic();
			LOG.info(rank.toString());
			rank.forceStrictness(conditional.getPropositions());
			LOG.info(rank.toString());
			ArrayList<Proposition> propositions = new ArrayList<>();
			
			Iterator<Proposition> sigIterator = rank.getSignature().iterator(); 
			while(sigIterator.hasNext()){
				 propositions.add(sigIterator.next());
			}
			
			LOG.info("Alle propositionen " + propositions.toString());
			
			if(rank.rank(translator.toPropositional(bGoal))== 0 && rank.rank(translator.toPropositional(new Negation(bGoal)))>0){
				HashSet<FolFormula> support = new HashSet<FolFormula>();
				supportSet.add(support);
			}else{
				int j = 0;
				label:
				while(j<propositions.size()){
					ArrayList<Proposition> props = new ArrayList<Proposition>();
					Iterator<Proposition> iterator = propositions.iterator();
					for(int i = 0; i<j;i++){	
						props.add(iterator.next());
					}
					
					Conjunction conjunction;
					if(props.isEmpty()){
						conjunction = new Conjunction(new Tautology(), new Tautology());
					}else{
						conjunction = new Conjunction(props);
					}
					
					iterator = propositions.iterator();
					for(int i = 0;i<j;i++){
						iterator.next();
					}
					while(iterator.hasNext()){
						Proposition prop = iterator.next();
						Conjunction con = conjunction.combineWithAnd(prop);
					
						rank = revisionOp.simulateOCFRevisionByFormulas(conditional, con);
						LOG.info("Rang von "+ bGoal + rank.rank(translator.toPropositional(bGoal)) + " und Rang von con: " + rank.rank(con));
						if(rank.rank(translator.toPropositional(bGoal)) == 0 && (rank.rank(translator.toPropositional(new Negation(bGoal)))>0)){
							//add found minimal support
							HashSet<FolFormula> support = new HashSet<FolFormula>();
							props.add(prop);
							for(Proposition p : props){
								support.add(translator.toFOL(p));
							}
							supportSet.add(support);
							
							//delete used propositions
							for(Proposition p : props){
								propositions.remove(p);
							}
							break label;
						}
					}
					j++;
				}
			}			
			
			for(HashSet<FolFormula> support: supportSet){
				addOption(support, new HashSet<FolFormula>());
			}
		}
	}
	
	public void addOption(HashSet<FolFormula> demands ,HashSet<FolFormula> promises){
		Tupel<HashSet<FolFormula>, HashSet<FolFormula>> option = new Tupel<HashSet<FolFormula>, HashSet<FolFormula>>(demands, promises);
		options.addFirst(option);
	}
	
	public LinkedList<Tupel<HashSet<FolFormula>,HashSet<FolFormula>>> getOptions(){
		return options;
	}
	
	public Tupel<HashSet<FolFormula>,HashSet<FolFormula>> getNextOptions(){
		return options.pollFirst();
	}
	
	public Tupel<HashSet<FolFormula>,HashSet<FolFormula>> getNextOptionsWithoutRemoving(){
		return options.peek();
	}
	
	
	@Override
	public BaseAgentComponent clone() {
		return new Options(this);
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		
	}
}
