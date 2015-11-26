package com.github.kreaturesfw.core.legacy;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.RelationalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.core.operators.BaseOperator;
import com.github.kreaturesfw.core.operators.BeliefOperatorFamily;
import com.github.kreaturesfw.core.operators.BeliefOperatorFamilyFactory;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;
import com.github.kreaturesfw.core.operators.OperatorCaller;
import com.github.kreaturesfw.core.operators.OperatorProvider;
import com.github.kreaturesfw.core.operators.OperatorStack;
import com.github.kreaturesfw.core.operators.parameter.ChangeBeliefbaseParameter;
import com.github.kreaturesfw.core.operators.parameter.ReasonerParameter;
import com.github.kreaturesfw.core.operators.parameter.TranslatorParameter;
import com.github.kreaturesfw.core.parser.ParseException;
import com.github.kreaturesfw.core.report.Reporter;
import com.github.kreaturesfw.core.serialize.BeliefbaseConfig;
import com.github.kreaturesfw.core.util.Pair;

/**
 * An BaseBeliefbase implements the agent data component functionality but it is no special
 * extension to the agent model but a base class for different implementations of belief base
 * representations like logic programs or ordinal conditional functions for example.
 * 
 * A belief base contains several operators which define the functionality of the belief base.
 * The BaseBeliefbase provides an interface to use the operators in a homogeneous way.
 * 
 * The belief base uses the PropertyChangeListener to inform the system about its changes. In this
 * base class is no information about the properties needed in sub classes to express the knowledge.
 * Therefore a reserved property name BELIEFBASE_CHANGE_PROPERTY_NAME is used to inform the listeners
 * about changes of the belief base. This event is fired after a change operator has altered the 
 * belief base.
 * 
 * @author Tim Janus
 */
public abstract class BaseBeliefbase 
	extends 	BaseAgentComponent 
	implements 	BeliefBase,
				OperatorCaller {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(BaseBeliefbase.class);
	
	/** default error string if a formula is no FOL formula */
	protected static String RES_NO_FOL = "formula is no FOL formula.";
	
	/** default error string if a formula uses quantifiers but the beliefbase does not support them */
	protected static String RES_HAS_QUANTIFIERS = "formula has quantifiers, they are not supported.";
	
	/** default error string if a formula uses variables but the beliefbase does not support them */
	protected static String RES_HAS_VARIABLES = "formula has variables, they are not supported.";
	
	public static final String BELIEFBASE_CHANGE_PROPERTY_NAME = "_beliefBaseContent_";
	
	/** flag indicating if this type of beliefbase supports quantified formulas */
	private boolean supportsQuantifiers;
	
	/** flag indicating if this type of beliefbase supports formulas containing variables */
	private boolean supportsVariables;
	
	private BeliefOperatorFamily family;
	
	/** 
	 *	Empty until isQueryValid returns false the first time. Then it contains the reason why the last
	 * 	query was rejected.
	 */
	protected String reason = "";
	
	protected OperatorProvider operators = new OperatorProvider();
	
	public OperatorProvider getOperators() {
		return operators;
	}
	
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
		
		operators = new OperatorProvider(other.operators);
		family = other.family;
	}
	
	/**
	 * Helper method: Instantiates the used operators for performing operations on this belief base.
	 * THis is called at by PluginInstatiator when creating new belief bases.
	 * @param bbc	Data-structure with information about the classes which will be used for the different
	 * 				operations.
	 * @throws InstantiationException
	 * @throws ParseException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException
	 */
	public void generateOperators(BeliefbaseConfig bbc) 
			throws InstantiationException, ClassNotFoundException, ParseException {		
		operators.addOperationSet(bbc.getChangeOperators());
		operators.addOperationSet(bbc.getReasoners());
		operators.addOperationSet(bbc.getTranslators());
		family = BeliefOperatorFamilyFactory.create(bbc.getBeliefOperatorFamily(), 
				operators.getOperationSetByType(BaseReasoner.OPERATION_TYPE));
	}
	
	public BeliefOperatorFamily getBeliefOperatorFamily() {
		return family;
	}

	/** @return the default change operator */
	public OperatorCallWrapper getChangeOperator() {
		return operators.getPreferedByType(BaseChangeBeliefs.OPERATION_TYPE);
	}

	/** @return the default reasoning operator */
	public OperatorCallWrapper getReasoningOperator() {
		return operators.getPreferedByType(BaseReasoner.OPERATION_TYPE);
	}

	/** @return the default translator */
	public OperatorCallWrapper getTranslator() {
		return operators.getPreferedByType(BaseTranslator.OPERATION_TYPE);
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
		LOG.info("Parsing file: '{}'", filepath);
		File f = new File(filepath);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
		parseImpl(br);
	}
	
	/**
	 * Parses the content given in the BufferedReader using the implementation of
	 * the parseInt method.
	 * @param br	Reference to the BufferedReader containing the content to parse.
	 * @throws ParseException
	 * @throws IOException
	 */
	public void parse(BufferedReader br) throws ParseException, IOException {
		parseImpl(br);
	}
	
	/**
	 * The internal parse function for the belief base.
	 * Sub classes must implement this method to parse a string representation into a 
	 * belief base living in memory.
	 * 
	 * @param br	The content representing the belief base. It is encapsulated by
	 * 				a BufferedReader to read from a file for example.
	 * @throws IOException 
	 * @throws ParserException 
	 */
	protected abstract void parseImpl(BufferedReader br) throws ParseException, IOException;
	
	/**
	 * Adds the given set of FOL formulas to the knowledge of the belief base.
	 * It uses the default change operator and the default translator.
	 * @param formulas	A set of FOL formulas which are added as knowledge to the belief base
	 */
	public void addKnowledge(Set<FolFormula> formulas) {
		addKnowledge(formulas, null, null);
	}
	
	/**
	 * Adds the given FOL formula to the knowledge of the belief base.
	 * It uses the default change operator and the default translator.
	 * @param formula	Reference to the FOL formula which is added to the belief base
	 */
	public void addKnowledge(FolFormula formula)
	{
		Set<FolFormula> formulas = new HashSet<FolFormula>();
		formulas.add(formula);
		addKnowledge(formulas);
	}
	
	/**
	 * Adds the given perception to the knowledge of the belief base.
	 * The default change operator and the default translator is used.
	 * @param perception	Reference to the perception which is added to the belief base.
	 */
	public void addKnowledge(Perception perception) {
		addKnowledge(perception, null, null);
	}
	
	/**
	 * Adds the given perception to the knowledge of the belief base.
	 * Uses the given translator for the translation process and the
	 * given change operator to perform the change operation
	 * @param perception		Reference to the perceptin which is added to the belief base
	 * @param translator		Reference to the translator to use, if null the default translator is used.
	 * @param changeOperator	Reference to the change operator to use, if null the default change operator is used.
	 */
	public void addKnowledge(Perception perception, OperatorCallWrapper translator, 
			OperatorCallWrapper changeOperator) {
		if(translator == null)
			translator = getTranslator();
		
		BaseBeliefbase newK = (BaseBeliefbase) translator.process(new TranslatorParameter(this, perception));
		addKnowledge(newK, changeOperator);
	}
	
	/**
	 * Adds the given set of FOL formulas to the knowledge of the belief base.
	 * Uses the given translator for the translation process and the
	 * given change operator to perform the change operation
	 * @param formulas			Set containing FOL formulas with new knowledge.
	 * @param translator		Reference to the translator to use, if null the default translator is used.
	 * @param changeOperator	Reference to the change operator to use, if null the default change operator is used.
	 */
	public void addKnowledge(Set<FolFormula> formulas, OperatorCallWrapper translator, 
			OperatorCallWrapper changeOperator) {
		if(translator == null)
			translator = getTranslator();
		
		BaseBeliefbase newK = (BaseBeliefbase) translator.process(new TranslatorParameter(this, formulas));
		addKnowledge(newK, changeOperator);
	}
	
	/**
	 * Adds the knowledge of the given belief base to this belief base. Uses
	 * the given change operator.
	 * @param newKnowledge		A belief base having more information
	 * @param changeOperator	Reference to the change operator to use, if null the default change operator is used.
	 */
	public void addKnowledge(BaseBeliefbase newKnowledge, OperatorCallWrapper changeOperator) {
		if(changeOperator == null)
			changeOperator = getChangeOperator();
		
		ChangeBeliefbaseParameter cbp = new ChangeBeliefbaseParameter(this, newKnowledge);
		changeOperator.process(cbp);
		firePropertyChangeListener(BELIEFBASE_CHANGE_PROPERTY_NAME, null, null);
	}
	
	/**
	 * reasons the given query.
	 * @param query
	 * @return An instance of angerona Answer containing the answer.
	 */
	public AngeronaAnswer reason(FolFormula query) {
		if(!isFormulaValid(query)) 
			throw new RuntimeException("Can't reason: " + query + " - because: " + reason);
		@SuppressWarnings("unchecked")
		Pair<Set<FolFormula>, AngeronaAnswer> reval = (Pair<Set<FolFormula>, AngeronaAnswer>) getReasoningOperator().process(new ReasonerParameter(this,  query));
		return reval.second;
	}
	

	public Set<FolFormula> infere() {
		@SuppressWarnings("unchecked")
		Pair<Set<FolFormula>, AngeronaAnswer> reval = (Pair<Set<FolFormula>, AngeronaAnswer>) getReasoningOperator().process(new ReasonerParameter(this));
		return reval.first;
	}
	
	public Set<FolFormula> infere(String reasonerCls, Map<String, String> parameters) {
		Map<String, String> oldSettings = null;
		OperatorCallWrapper reasoner = operators.getOperationSetByType(BaseReasoner.OPERATION_TYPE).getOperator(reasonerCls);
		
		if(parameters == null)
			parameters = reasoner.getSettings();
		oldSettings = reasoner.getSettings();
		reasoner.setSettings(parameters);
		@SuppressWarnings("unchecked")
		Pair<Set<FolFormula>, AngeronaAnswer> reval = (Pair<Set<FolFormula>, AngeronaAnswer>) reasoner.process(new ReasonerParameter(this));
		reasoner.setSettings(oldSettings);
		return reval.first;
	}
	
	@SuppressWarnings("unchecked")
	public Set<FolFormula> infere(OperatorCallWrapper ocw) {
		ReasonerParameter param = new ReasonerParameter(this);
		param.setSettings(new HashMap<String, String>(ocw.getSettings()));
		return ((Pair<Set<FolFormula>, AngeronaAnswer>) ocw.process(param)).first;
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
	 * gets the content of the belief base in string representation: Useful for viewing them in UIs for example.
	 * Subclasses have to implement the method. For every entry in the belief base like a fact, a rule or a conditional
	 * a string has to be added to the list of strings which is returned.
	 * 
	 * @return a list of strings representing the content of the belief base.
	 * @todo Think about a more complex object which could map to the belief base for example.
	 */
	public abstract List<String> getAtomsAsStringList();
	
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
	public abstract BaseBeliefbase clone();
	
	/** @return  a signature containing the atomic language constructs used by this beliefbase. */
	public abstract Signature getSignature();
	
	@Override
	public void setParent(Long id) {
		super.setParent(id);
	}
	
	@Override
	public abstract boolean equals(Object other);
	
	@Override
	public abstract int hashCode();

	@Override
	public Reporter getReporter() {
		return getAgent();
	}

	@Override
	public OperatorStack getStack() {
		// only provide the operator stack functionality when attached to an agent
		// for tests use the EmptyStackImplementation
		return getAgent() != null ? getAgent() : new EmptyStackImplementation();
	}
	
	/**
	 * This implementation does nothing it can be used as mock object for tests etc.
	 * @author Tim Janus
	 */
	private static class EmptyStackImplementation implements OperatorStack {

		@Override
		public void pushOperator(BaseOperator op) {
		}

		@Override
		public void popOperator() {
		}

		@Override
		public Stack<BaseOperator> getOperatorStack() {
			return new Stack<>();
		}

	}
}
