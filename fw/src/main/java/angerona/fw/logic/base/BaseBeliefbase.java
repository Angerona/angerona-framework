package angerona.fw.logic.base;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.ParserException;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.PluginInstantiator;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.operators.parameter.BeliefUpdateParameter;
import angerona.fw.operators.parameter.BeliefbaseParameter;
import angerona.fw.report.ReportAttachment;
import angerona.fw.serialize.BeliefbaseConfiguration;

/**
 * Base class for every belief base used in Angerona.
 * @author Tim Janus
 */
public abstract class BaseBeliefbase extends BeliefBase implements ReportAttachment {
	
	/** default error string if a formula is no FOL formula */
	protected static String RES_NO_FOL = "formula is no FOL formula.";
	
	/** default error string if a formula uses quantifiers but the beliefbase does not support them */
	protected static String RES_HAS_QUANTIFIERS = "formula has quantifiers, they are not supported.";
	
	/** default error string if a formula uses variables but the beliefbase does not support them */
	protected static String RES_HAS_VARIABLES = "formula has variables, they are not supported.";
	
	private AngeronaEnvironment env;
	
	/**
	 * Enumeration with different operation types for updating the belief base.
	 * @author Tim Janus
	 */
	public enum UpdateType {
		/** the belief base will be expanded*/
		U_EXPANSION,
		
		/** the belief base will be expanded and then the consolidation will be applied */
		U_EXPANSION_AND_CONSOLIDATION,
		
		/** the belief base will be updated by the revision operator */
		U_REVISION
	}
	
	/** update behavior used when not explicitly defined by caller */
	private UpdateType defaultUpdateBehavior = UpdateType.U_EXPANSION;
	
	/** flag indicating if this type of beliefbase supports quantified formulas */
	private boolean supportsQuantifiers;
	
	/** flag indicating if this type of beliefbase supports formulas containing variables */
	private boolean supportsVariables;
	
	/** 
	 *	Empty until isQueryValid returns false the first time. Then it contains the reason why the last
	 * 	query was rejected.
	 */
	protected String reason = "";
	
	/** Reference to the used expansion operator */
	private BaseExpansion expansionOperator;
	
	/** Reference to the used consolidation operator */
	private BaseConsolidation consolidationOperator;
	
	/** Reference to the used revision operator */
	private BaseRevision revisionOperator;
	
	/** Reference to the used reasoning operator */
	private BaseReasoner reasoningOperator;
	
	/** Default Ctor: Generates an empty belief base which does not supports quantifiers or variables in its formulas */
	public BaseBeliefbase() {
		this.supportsQuantifiers = false;
		this.supportsVariables = false;
	}

	public void setEnvironment(AngeronaEnvironment env) {
		this.env = env;
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
	 * Ctor: Generates a belief base by parsing a file
	 * @param filepath	path to the file containing the string representation of the belief base.
	 * @throws IOException 
	 */
	public BaseBeliefbase(String filepath) throws IOException {
		parse(filepath);
	}

	/**
	 * Generates the content of this beliefbase by parsing a file
	 * @param filepath	path to the file containing the representation of the belief base.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parse(String filepath) throws FileNotFoundException, IOException {
		File f = new File(filepath);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
		parseInt(br);
	}
	
	public void parse(BufferedReader br) throws ParserException, IOException {
		parseInt(br);
	}
	
	/**
	 * Helper method: Instantiates the used operators for performing operations on this belief base.
	 * THis is called at by PluginInstatiator when creating new belief bases.
	 * @param bbc	Data-structure with information about the classes which will be used for the different
	 * 				operations.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public void generateOperators(BeliefbaseConfiguration bbc) throws InstantiationException, IllegalAccessException {		
		reasoningOperator = PluginInstantiator.createReasoner(bbc.getReasonerClassName());
		reasoningOperator.setBeliefbase(this);
		
		if(bbc.getExpansionClassName() != null && !bbc.getExpansionClassName().equals("empty"))
			expansionOperator = PluginInstantiator.createExpansion(bbc.getExpansionClassName());
		
		if(bbc.getConsolidationClassName() != null && !bbc.getConsolidationClassName().equals("empty"))
			consolidationOperator = PluginInstantiator.createConsolidation(bbc.getConsolidationClassName());
		
		if(bbc.getRevisionClassName() != null && !bbc.getRevisionClassName().equals("empty"))
			revisionOperator = PluginInstantiator.createRevision(bbc.getRevisionClassName());
		
		try {
			defaultUpdateBehavior = bbc.getUpdateBehavior();
		} catch(IllegalArgumentException ex) {
			// Do nothing yet.
		}
	}
	
	/**
	 * The internal parse function for the belief base.
	 * Sub classes must implement this method to parse a string representation into a belief base living in memory.
	 * @param content	The string representing the belief base. (Content of a file on the filesystem as an example)
	 * @throws IOException 
	 * @throws ParserException 
	 */
	protected abstract void parseInt(BufferedReader br) throws ParserException, IOException;
	
	/**
	 * adds the given formulas to the knowledgebase as new knowledge. Using the default update mechanism
	 * defined by the config file. If default update behavior was defined in the config file U_EXPANSION is used.
	 * @param newKnowledge	set of formulas representing the new knowledge.
	 */
	public void addNewKnowledge(Set<FolFormula> newKnowledge) {
		addNewKnowledge(newKnowledge, defaultUpdateBehavior);
	}
	
	/**
	 * adds the given formulas to the knowledgebase as new knowledge. Using the given update mechanism.
	 * @param newKnowledge	set of formulas representing the new knowledge.
	 * @param updateType	type of used update behavior.
	 */
	public void addNewKnowledge(Set<FolFormula> newKnowledge, UpdateType updateType) {
		for(FolFormula ff : newKnowledge) {
			if(!isFormulaValid(ff)) {
				throw new RuntimeException("Cant add knowledge, dont support: " + ff + " - reason: " + this.reason);
			}
		}
		
		// TODO: Think about local copies...
		BeliefUpdateParameter bup = new BeliefUpdateParameter(this, newKnowledge, env);
		switch(updateType) {
		case U_EXPANSION:
			if(expansionOperator == null)
				throw new RuntimeException("Can't use expansion on a beliefbase which doesn't has a valid expansion operator.");
			expansionOperator.process(bup);
			break;
			
		case U_EXPANSION_AND_CONSOLIDATION:
			if(expansionOperator == null)
				throw new RuntimeException("Can't use expansion on a beliefbase which doesn't has a valid expansion operator.");
			if(consolidationOperator == null)
				throw new RuntimeException("Can't use consolidation on a beliefbase which doesn't has a valid consolidation operator.");
			expansionOperator.process(bup);
			consolidationOperator.process(new BeliefbaseParameter(this, env));
			break;
			
		case U_REVISION:
			if(revisionOperator == null)
				throw new RuntimeException("Can't use revision on a beliefbase which doesn't has a valid revision operator.");;
			revisionOperator.process(bup);
			break;
		}
	}
	
	/**
	 * reasons the given query.
	 * @param query
	 * @return An instance of angerona Answer containing the answer.
	 */
	public AngeronaAnswer reason(FolFormula query) {
		if(reasoningOperator == null)
			throw new RuntimeException("Can't reason on a beliefbase which doesn't has a valid reasoning Operator");
		else if(!isFormulaValid(query)) 
			throw new RuntimeException("Can't reason: " + query + " - because: " + reason);
		AngeronaAnswer answer = (AngeronaAnswer)reasoningOperator.query(query);
		return answer;
	}
	
	/**
	 * This method checks if the given formula can be translate into the knowledge base langauge.
	 * Angerona uses a subset of FOL to communicate between different plugins.
	 * At the moment no quantified formulas or formulas with variables are allowed.
	 * @param query
	 * @return
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
		
		if(!supportsVariables && !fol.isGround()) {
			reason = RES_HAS_VARIABLES;
			return false;
		}
		
		return true;
	}
	
	/** gets the atoms of the belief base in string representation usefull for viewing them in UIs and so on */
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
	public Long getGUID() {
		return null;
	}
	
	@Override
	public Long getParent() {
		return null;
	}
	
	@Override
	public List<Long> getChilds() {
		// TODO: implement.
		return null;
	}
}
