package angerona.fw;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.listener.BeliefbaseChangeListener;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.parameter.BeliefUpdateParameter;
import angerona.fw.parser.ParseException;
import angerona.fw.serialize.BeliefbaseConfig;

/**
 * Base class for every belief base used in Angerona.
 * @author Tim Janus
 */
public abstract class BaseBeliefbase extends BaseAgentComponent implements BeliefBase {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(BaseBeliefbase.class);
	
	/** default error string if a formula is no FOL formula */
	protected static String RES_NO_FOL = "formula is no FOL formula.";
	
	/** default error string if a formula uses quantifiers but the beliefbase does not support them */
	protected static String RES_HAS_QUANTIFIERS = "formula has quantifiers, they are not supported.";
	
	/** default error string if a formula uses variables but the beliefbase does not support them */
	protected static String RES_HAS_VARIABLES = "formula has variables, they are not supported.";
	
	/** a list of listener which are interested in beliefbase changes */
	private List<BeliefbaseChangeListener> listeners = new LinkedList<BeliefbaseChangeListener>();
	
	/** flag indicating if this type of beliefbase supports quantified formulas */
	private boolean supportsQuantifiers;
	
	/** flag indicating if this type of beliefbase supports formulas containing variables */
	private boolean supportsVariables;
	
	/** 
	 *	Empty until isQueryValid returns false the first time. Then it contains the reason why the last
	 * 	query was rejected.
	 */
	protected String reason = "";
	
	private OperatorSet<BaseChangeBeliefs> changeOperators = new OperatorSet<BaseChangeBeliefs>();
	
	private OperatorSet<BaseReasoner> reasoningOperators = new OperatorSet<BaseReasoner>();
	
	private OperatorSet<BaseTranslator> translators = new OperatorSet<BaseTranslator>();
	
	/** Default Ctor: Generates an empty belief base which does not supports quantifiers or variables in its formulas */
	public BaseBeliefbase() {
		this.supportsQuantifiers = false;
		this.supportsVariables = false;
	}

	/**
	 * Ctor: Generates an empty belief base if its supports quantifiers or variables is given by the parameter flags.
	 * @param supportsQuantifiers	if this is true the belief base supports formulas using quantifiers.
	 * @param supportVariables		if this is true the belief base supports formulas using variables.
	 */
	public BaseBeliefbase(boolean supportsQuantifiers, boolean supportVariables) {
		this.supportsQuantifiers = supportsQuantifiers;;
		this.supportsVariables = supportVariables;
	}
	
	/**
	 * Copy-Ctor: Does not create a new ID for the Object but shares
	 * the ID of the parameter object.
	 * @param other another Beliefbase which should be copied.
	 */
	public BaseBeliefbase(BaseBeliefbase other) {
		super(other);
		
		changeOperators = new OperatorSet<BaseChangeBeliefs>(other.changeOperators);
		reasoningOperators = new OperatorSet<BaseReasoner>(other.reasoningOperators);
		translators = new OperatorSet<BaseTranslator>(other.translators);
	}
	
	/**
	 * Helper method: Instantiates the used operators for performing operations on this belief base.
	 * THis is called at by PluginInstatiator when creating new belief bases.
	 * @param bbc	Data-structure with information about the classes which will be used for the different
	 * 				operations.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void generateOperators(BeliefbaseConfig bbc) throws InstantiationException, IllegalAccessException {		
		changeOperators.set(bbc.getChangeOperators());
		reasoningOperators.set(bbc.getReasoners());
		translators.set(bbc.getTranslators());
		updateOwner();
	}

	public BaseChangeBeliefs getRevisionOperator() {
		return changeOperators.def();
	}

	public BaseReasoner getReasoningOperator() {
		return reasoningOperators.def();
	}

	public BaseTranslator getTranslator() {
		return translators.def();
	}
	
	public OperatorSet<BaseChangeBeliefs> getChangeOperators() {
		return changeOperators;
	}

	public OperatorSet<BaseReasoner> getReasoningOperators() {
		return reasoningOperators;
	}

	public OperatorSet<BaseTranslator> getTranslators() {
		return translators;
	}

	/**
	 * Generates the content of this beliefbase by parsing a file. The real work is done
	 * by a subclass implementing the parseInt method.
	 * @param filepath	path to the file containing the representation of the belief base.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parse(String filepath) 
			throws FileNotFoundException, IOException, ParseException {
		File f = new File(filepath);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
		parseInt(br);
	}
	
	/**
	 * Parses the content given in the BufferedReader using the implementation of
	 * the parseInt method.
	 * @param br	Reference to the BufferedReader containing the content to parse.
	 * @throws ParseException
	 * @throws IOException
	 */
	public void parse(BufferedReader br) throws ParseException, IOException {
		parseInt(br);
	}
	
	/**
	 * The internal parse function for the belief base.
	 * Sub classes must implement this method to parse a string representation into a 
	 * belief base living in memory.
	 * Because the belief base files of Angerona contain multiple belief bases the content
	 * of the file is split for each belief base by a parser which is called pior parseInt.
	 * Therefore the content in the BufferedReader is the content of one belief base in such
	 * a belief base file which is defined between '{' '}'. 
	 * Example Beliefbase File:
	 * World {
	 *   a. b.
	 * } view->other {
	 *   b. c.
	 * }
	 * Such a file would cause two calls on parseInt methods on different instances of belief
	 * bases. The first would get the String '  a. b.' the second '  b. c.' as content of its
	 * BufferedReader.
	 * @param br	The content representing the belief base. It is encapsulated by
	 * 				a BufferedReader to read from a file for example.
	 * @throws IOException 
	 * @throws ParserException 
	 */
	protected abstract void parseInt(BufferedReader br) throws ParseException, IOException;
	
	public void addKnowledge(Set<FolFormula> formulas) {
		addKnowledge(formulas, null, null);
	}
	
	public void addKnowledge(FolFormula formula)
	{
		Set<FolFormula> formulas = new HashSet<FolFormula>();
		formulas.add(formula);
		addKnowledge(formulas);
	}
	
	public void addKnowledge(Perception perception) {
		addKnowledge(perception, null, null);
	}
	
	public void addKnowledge(Perception perception, BaseTranslator translator, 
			BaseChangeBeliefs changeOperator) {
		if(translator == null)
			translator = translators.def();
		
		BaseBeliefbase newK = translator.translatePerception(perception);
		addKnowledge(newK, changeOperator);
	}
	
	public void addKnowledge(Set<FolFormula> formulas, BaseTranslator translator, 
			BaseChangeBeliefs changeOperator) {
		if(translator == null)
			translator = translators.def();
		
		BaseBeliefbase newK = translator.translateFOL(formulas);
		addKnowledge(newK, changeOperator);
	}
	
	public void addKnowledge(BaseBeliefbase newKnowledge, BaseChangeBeliefs changeOperator) {
		if(changeOperator == null)
			changeOperator = changeOperators.def();
		
		Agent agent = getAgent();
		BeliefUpdateParameter param = new BeliefUpdateParameter(this, newKnowledge, agent);
		changeOperator.process(param);
		onChange();
	}
	
	public void addListener(BeliefbaseChangeListener listener) {
		this.listeners.add(listener);
	}
	
	public boolean removeListener(BeliefbaseChangeListener listener) {
		return this.listeners.remove(listener);
	}
	
	public void removeAllListeners() {
		this.listeners.clear();
	}
	
	/**
	 * Helper method: is called when the content of the beliefbase is changed
	 * the basic implementation informs the listeners. Subclasses could implement
	 * their own reactions.
	 */
	protected void onChange() {
		for(BeliefbaseChangeListener l : listeners) {
			l.changed(this);
		}
	}
	
	/**
	 * reasons the given query.
	 * @param query
	 * @return An instance of angerona Answer containing the answer.
	 */
	public AngeronaAnswer reason(FolFormula query) {
		if(!isFormulaValid(query)) 
			throw new RuntimeException("Can't reason: " + query + " - because: " + reason);
		AngeronaAnswer answer = (AngeronaAnswer)reasoningOperators.def().query(this, query);
		return answer;
	}
	

	public Set<FolFormula> infere() {
		return reasoningOperators.def().infer(this);
	}
	
	public Set<FolFormula> infere(String reasonerCls, Map<String, String> parameters) {
		Map<String, String> oldParams = null;
		BaseReasoner reasoner = this.reasoningOperators.get(reasonerCls);
		if(parameters == null)
			parameters = reasoner.getParameters();
		oldParams = reasoner.getParameters();
		reasoner.setParameters(parameters);
		Set<FolFormula> reval = reasoner.infer(this);
		reasoner.setParameters(oldParams);
		return reval;
	}
	
	/**
	 * This method checks if the given formula can be translate into the knowledge base langauge.
	 * Angerona uses a subset of FOL to communicate between different plugins.
	 * At the moment no quantified formulas or formulas with variables are allowed.
	 * @param query
	 * @return	true if the given formula is valid for the beliefbase false otherwise.
	 */
	public boolean isFormulaValid(Formula query) {
		if(!(query instanceof FolFormula)) {
			reason = RES_NO_FOL;
			return false;
		}
		RelationalFormula fol = (RelationalFormula)query;
		if(!supportsQuantifiers && fol.containsQuantifier()) {
			reason = RES_HAS_QUANTIFIERS;
			return false;
		}
		
		return true;
	}
	
	/** 
	 * gets the content of the belief base in string representation: Usefull for viewing them in UIs for example.
	 * Subclasses have to implement the method. For every entry in the belief base like a fact, a rule or a conditional
	 * a string has to be added to the list of strings which is returned.
	 * @return a list of strings representing the content of the belief base.
	 * // TODO: Think about a more complex object which could map to the belief base for example.
	 */
	public abstract List<String> getAtoms();
	
	/** 
	 * @return 	An empty String until isQueryValid returns false the first time. Then it contains the reason why the last
	 * 			query was rejected.
	 */
	public String getReason() {
		return reason;
	}

	/** @return flag indicating if this type of beliefbase supports quantified formulas */
	public boolean supportsQuantifiers() {
		return supportsQuantifiers;
	}
	
	/** @return flag indicating if this type of beliefbase supports formulas containing variables */
	public boolean supportsVariables() {
		return supportsVariables;
	}
	
	/** @return the ending of a file handled by this type of beliefbase. In many cases three characters like: "dum" for the dummy plugin */
	public abstract String getFileEnding();

	/**
	 * @return a deep copy of this belief-base.
	 */
	public abstract Object clone();
	
	/** @return  a signature containing the atomic language constructs used by this beliefbase. */
	public abstract Signature getSignature();
	
	@Override
	public void setParent(Long id) {
		super.setParent(id);
		updateOwner();
	}

	private void updateOwner() {
		Agent agent = getAgent();
		if(agent != null) {
			changeOperators.setOwner(agent);
			reasoningOperators.setOwner(agent);
			translators.setOwner(agent);
			LOG.info("Set owner '{}' for operators of beliefbase.", agent.getName());
		} else {
			LOG.warn("Cannot set the owners for operators.");
		}
	}
}
