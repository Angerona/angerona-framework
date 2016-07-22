package com.github.angerona.fw.defendingagent;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.commons.LogicalSymbols;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.semantics.NicePossibleWorld;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.defendingagent.Prover.Prover;
import com.github.angerona.fw.defendingagent.Prover.SICStusException;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.plwithknowledge.logic.ModelTupel;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeReasoner;
import com.github.angerona.fw.util.LogicTranslator;

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
public class CensorComponent extends BaseAgentComponent {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(CensorComponent.class);
	
	private Prover.InferenceSystem inferenceSystem = Prover.InferenceSystem.RATIONAL;
	
	public CensorComponent() {
		super();
	}
	
	// for testing purposes only. 
	public static void main(String[] args) {
		LogicalSymbols.setContradictionSymbol("!");
		LogicalSymbols.setClassicalNegationSymbol("-");
		
//		CensorComponent cexec = new CensorComponent();
		File directory = new File (".");
		 try {
		 System.out.println ("Current directory's canonical path: " 
		  + directory.getCanonicalPath()); 
		   System.out.println ("Current directory's absolute  path: " 
		  + directory.getAbsolutePath());
		 }catch(Exception e) {
		 System.out.println("Exceptione is ="+e.getMessage());
		  }
		// Logistics example - test klm solver
		PlBeliefSet beliefs = new PlBeliefSet();
		beliefs.add(new Proposition("i11_22"));
		
		
		// Build View
		View v = new View(beliefs);
		
//		FolFormula s22 = new FOLAtom(new Predicate("s22"));
		FolFormula s21 = new FOLAtom(new Predicate("s21"));
		FolFormula not_s21 = new net.sf.tweety.logics.fol.syntax.Negation(s21);
		FolFormula r = new FOLAtom(new Predicate("r"));
		FolFormula s21_r = new net.sf.tweety.logics.fol.syntax.Disjunction(not_s21, r); 
		
		v = v.RefineViewByQuery(s21, AnswerValue.AV_UNKNOWN);
		v = v.RefineViewByQuery(s21_r, AnswerValue.AV_TRUE);
		v = v.RefineViewByRevision(s21, AnswerValue.AV_TRUE);
		System.out.println("View:" +v);
		
//		List<String> CL_V = cexec.makeBeliefBase(v);
//		PropositionalFormula plprove = new Disjunction(new Negation(new Tautology()), new Contradiction());
//		String toProve = cexec.translate(r);
//		toProve = "(true => false)";
		
		Prover prover = Prover.getInstance();
		
		List<String> manual = new LinkedList<String>();
		manual.add("i11_22 => neg s21 or r ");
		manual.add("neg ( i11_22 and s21 => false )");
		manual.add("neg ( i11_22 => neg s21 )");
		manual.add("neg ( i11_22 => s21 )");
		manual.add("neg ( i11_22 and s21  => false)");
		
		try {
			System.out.println("Prover: "+prover.prove(manual, "i11_22 and s21 => r", Prover.InferenceSystem.RATIONAL));
		} catch (SICStusException e) {
			e.printStackTrace();
		}
	}
	
	public CensorComponent(Prover.InferenceSystem inferenceSystem) {
		super();
		this.inferenceSystem = inferenceSystem;
	}	
	
	/**
	 *  the censor uses the klmlean framework to prove sentences in
	 *  one of five possible inference systems. The default is Rational.
	 */
	public void setInferenceSystem(Prover.InferenceSystem inferenceSystem) {
		this.inferenceSystem = inferenceSystem;
	}

	/**
	 * Calculate a skeptical inference of a given formula under a specific view.
	 * Returns true iff formula follows skeptically from the view and false otherwise 
	 * @param view
	 * @param formula
	 * @return
	 */
	public boolean scepticalInference(View view, FolFormula formula) {
		List<String> knowledgeBase = this.makeBeliefBase(view);
		Prover p = Prover.getInstance();
		boolean reval = false;
		try {
			reval = p.prove(knowledgeBase, translate(view.getBeliefSet()) + " => " + translate(formula), inferenceSystem);
		} catch (SICStusException e) {
			LOG.error("error on invocation of SICStus prolog engine");
			JOptionPane.showMessageDialog(null, "Error on invocation of SICStus engine ", "SICStus error", JOptionPane.ERROR_MESSAGE);
		}
		report("Calculate sceptical inference of '" + translate(formula) + "' using bbase: " + knowledgeBase + ". Result: " + Boolean.toString(reval));
		return reval;
	}
	
	/**
	 * Calculate all possible literals that can be sceptically infered from a given view.
	 * 
	 * @param view a view.
	 * @return a list of positive and negated literals that can be infered from the given view.
	 */
	public List<FolFormula> scepticalInferences(View view) {
		List<FolFormula> result = new LinkedList<FolFormula>();
		PropositionalSignature signature = view.getSignature();
		
		for(Proposition p : signature) {
			if(scepticalInference(view, LogicTranslator.PlToFo(p))) {
				result.add(LogicTranslator.PlToFo(p));
			}
		}
		
		
		for(Proposition p : signature) {
			Negation n = new Negation(p);
			if(scepticalInference(view, LogicTranslator.PlToFo(n))) {
				result.add(LogicTranslator.PlToFo(n));
			}
		}
		
		return result;
	}
	
	/**
	 * Check if there exists a consistent consequence relation that is compatible with the given view 
	 * @param view
	 */
	public boolean poss(View view) {
		// there is a possible consequence relation for a view V iff "true -> false" does not
		// follow from CL(V)
		List<String> knowledgeBase = this.makeBeliefBase(view);
//		PropositionalFormula contradiction = new Disjunction(new Negation(new Tautology()), new Contradiction());
		Prover p = Prover.getInstance();
		try {
			return ! p.prove(knowledgeBase, "(true => false)", inferenceSystem);
		} catch (SICStusException e) {
			e.printStackTrace();
			LOG.error("error on invocation of SICStus engine", e);
			JOptionPane.showMessageDialog(null, "Error on invocation of SICStus engine ", "SICStus error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Calculate a skeptical inference of a given formula under a specific view.
	 * Returns true iff formula follows skeptically from the view and false otherwise 
	 * @param view
	 * @param formula
	 * @return
	 */
	public boolean scepticalInference(ViewWithHistory view, FolFormula formula) {
		boolean satisfy = true;
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) view.getView().getReasoningOperator().getImplementation();
		ModelTupel models = reasoner.getModels(view.getView());
		Iterator<NicePossibleWorld> iterator = models.getModels().iterator();
		FOLPropTranslator translator = new FOLPropTranslator();
		while(iterator.hasNext()){
			if(!iterator.next().satisfies(translator.toPropositional(formula))){
				satisfy = false;
				break;
			}
		}
		return satisfy;
	}
	
	/**
	 * Calculate all possible literals that can be sceptically infered from a given view.
	 * 
	 * @param view a view.
	 * @return a list of positive and negated literals that can be infered from the given view.
	 */
	public List<FolFormula> scepticalInferences(ViewWithHistory view) {
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) view.getView().getReasoningOperator().getImplementation();
		return new LinkedList<FolFormula>(reasoner.infer(view.getView()));
	}
	
	
	/**
	 * Calculate a skeptical inference of a given formula under a specific view.
	 * Returns true iff formula follows skeptically from the view and false otherwise 
	 * @param view
	 * @param formula
	 * @return
	 */
	public boolean scepticalInference(ViewWithCompressedHistory view, FolFormula formula, CompressedHistory history) {
		List<FolFormula> list = this.scepticalInferences(view, history);
		return list.contains(formula);
	}
	
	/**
	 * Calculate all possible literals that can be sceptically infered from a given view with a history.
	 * 
	 * @param view a view.
	 * @param history a history
	 * @return a list of positive and negated literals that can be infered from the given view.
	 */
	public List<FolFormula> scepticalInferences(ViewWithCompressedHistory view, CompressedHistory history) {
		
		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) this.getAgent().getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
		LinkedList<Set<FolFormula>> list = new LinkedList<Set<FolFormula>>();
		LinkedList<FolFormula> retval = new LinkedList<FolFormula>();
		Set<PLWithKnowledgeBeliefbase> b = view.calculatePossibleBeliefbases(history);
		if(!b.isEmpty()){
			for(PLWithKnowledgeBeliefbase bbase : b){
				list.add(reasoner.infer(bbase));
			}
		}
		Set<FolFormula> compareList = list.poll();
		if(!list.isEmpty()){
			for(FolFormula formula: compareList){
				boolean inAllLists = true;
				for(Set<FolFormula> l: list){
					if(!l.contains(formula)){
						inAllLists = false;
						break;
					}
				}
				if(inAllLists){
					retval.add(formula);
				}
			}
		}else{
			if(compareList != null){
				retval = new LinkedList<FolFormula>(compareList);
			}else{
				retval = new LinkedList<FolFormula>();
			}
		}
		
		return retval;
	}
	
	/**
	 * Calculate a skeptical inference of a given formula under a specific view.
	 * Returns true iff formula follows skeptically from the view and false otherwise 
	 * @param view
	 * @param formula
	 * @return
	 */
	public boolean scepticalInference(BetterView view, FolFormula formula) {
		boolean satisfy = true;
//		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) view.getAgent().getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
//		PLWithKnowledgeBeliefbase bbase = new PLWithKnowledgeBeliefbase();
//		bbase.setKnowledge(view.getView().getKnowledge());
//		bbase.setAssertions(new LinkedList<>(new Conjunction(view.getView().getAssertions())));

		ModelTupel models = view.getView().getAssertions();
		
		
		//ModelTupel models = reasoner.getModels(bbase);
		Iterator<NicePossibleWorld> iterator = models.getModels().iterator();
		FOLPropTranslator translator = new FOLPropTranslator();
		while(iterator.hasNext()){
			if(!iterator.next().satisfies(translator.toPropositional(formula))){
				satisfy = false;
				break;
			}
		}
		return satisfy;
	}
	
	/**
	 * Calculate all possible literals that can be sceptically infered from a given view.
	 * 
	 * @param view a view.
	 * @return a list of positive and negated literals that can be infered from the given view.
	 */
	public List<FolFormula> scepticalInferences(BetterView view) {
//		PLWithKnowledgeReasoner reasoner = (PLWithKnowledgeReasoner) view.getAgent().getBeliefs().getWorldKnowledge().getReasoningOperator().getImplementation();
//		PLWithKnowledgeBeliefbase bbase = new PLWithKnowledgeBeliefbase();
//		bbase.setKnowledge(view.getView().getKnowledge());
//		bbase.setAssertions(new LinkedList<>(new Conjunction(view.getView().getAssertions())));
//		return new LinkedList<FolFormula>(reasoner.infer(bbase));
		return null;
	}
	
	/**
	 * Create a BeliefBase out of the View on the attacking agent
	 * @param v
	 * 	View on the attacking agent
	 * @return a String[] that represent the approximation of the knowledge of the attacking agent
	 * @see[1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
	 */
	public List<String> makeBeliefBase(View v){
		Set<Conditional> positiveConditionalBeliefs = v.getPositiveConditionalBeliefs();
		Set<Conditional> negativeConditionalBeliefs = v.getNegativeConditionalBeliefs();
		PlBeliefSet beliefSet = v.getBeliefSet();
//		int n = 0, i=0;
//		n += positiveConditionalBeliefs.size() + negativeConditionalBeliefs.size() + (beliefSet.isEmpty() ? 0 : 1);
//		String[] beliefBase = new String[n];
		List<String> result = new LinkedList<String>();
		for(Conditional a: positiveConditionalBeliefs){
//			beliefBase[i] = translate(a.getPremise()) + "=>" + translate(a.getConclusion());
			result.add(translate(a.getPremise()) + "=>" + translate(a.getConclusion()));
//			i++;
		}
		for(Conditional a: negativeConditionalBeliefs){
			result.add("neg ("+ translate(a.getPremise()) + "=>" + translate(a.getConclusion()) +")");
//			beliefBase[i] = "neg ("+ translate(a.getPremise()) + "=>" + translate(a.getConclusion()) +")";
//			i++;
		}
		if(!beliefSet.isEmpty()) {
			String belset = "neg ((";
			for(PropositionalFormula a: beliefSet){
				belset += translate(a) + " and ";
			}
			belset = belset.substring(0, belset.length()-5) + ") => false)";
			result.add(belset);
		}
		return result;
	}
	
	/**
	 * Translate a Collection of PropositionalFormulas by using the 
	 * method translate for PropositionalFormulas and connect the Formulas with "and"
	 * @param formulas
	 * 	Collection of PropositionalFormulas to translate
	 * @return a String where the operators are changed to "or" "and" "neg" and the formulas are 
	 * Connected with "and"
	 */
	public String translate(Collection<PropositionalFormula> formulas){
		if(formulas.isEmpty()) {
			// empty conjunction is always true
			return "true";
		}
		String result = "";
		for(PropositionalFormula a : formulas){
			if(result.equalsIgnoreCase("")){
				result = translate(a); 
			}else{
				result = result + " and " + translate(a); 
			}
		}
		return result;
	}
	
	/**
	 * Translate PropositionalForumla to a String that the TheoremSolver 
	 * can understand by changing "||" "&&" "-" to "or" "and" "neg"
	 * @param formula
	 * 	the PropositionalFormula to translate
	 * @return a String where the operators are changed to "or" "and" "neg"
	 */
	public String translate(PropositionalFormula formula){
		String result = formula.toString();

		result = result.replaceAll("\\|\\|", " or ");
		result = result.replaceAll("\\&\\&", " and ");
		result = result.replaceAll("-", " neg ");
		result = result.replaceAll("\\+", " true ");
		result = result.replaceAll("!", " false ");
		
		return "( "+ result +" )";
	}
	
	/**
	 * Translate FolForumla to a String that the TheoremSolver 
	 * can understand by changing "||" "&&" "-" to "or" "and" "neg"
	 * @param formula
	 * 	the FolFormula to translate
	 * @return a String where the operators are changed to "or" "and" "neg"
	 */
	public String translate(FolFormula formula){
		String result = formula.toString();
		//change || to or
		result = result.replaceAll("\\|\\|", " or ");
		result = result.replaceAll("\\&\\&", " and ");
		result = result.replaceAll("-", " neg ");
		result = result.replaceAll("\\+", " true ");
		result = result.replaceAll("!", " false ");
		
		return "( "+ result +" )";
	}

	@Override
	public void init(Map<String, String> additionalData) {
		if(additionalData.containsKey("KLMSemantics")) {
			String str = additionalData.get("KLMSemantics");
			try {
				inferenceSystem = Prover.InferenceSystem.valueOf(str.trim());
			} catch(Exception e) {
				LOG.error("Illegal argument for additionalData field 'KLMSemantics' in simulation description: " + str + ". Using default solver: RATIONAL.");
				
			}
		}
	}
	
	@Override
	public CensorComponent clone() {
		return new CensorComponent(this.inferenceSystem);
	}
}
