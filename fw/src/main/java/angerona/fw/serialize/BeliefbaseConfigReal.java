package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.xml.sax.SAXException;

import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;


/**
 * Contains the dynamic configurations of a belief base.
 * It holds the type names of the classes use for different operations.
 * 
 * @author Tim Janus
 */
@Root(name="beliefbase-configuration")
public class BeliefbaseConfigReal implements BeliefbaseConfig {
	@Element(name="name")
	protected String name;
	
	@Element(name="reasoners" , type=OperationSetConfigReal.class)
	protected OperationSetConfig reasonerOperators;
	
	@Element(name="change-operators", type=OperationSetConfigReal.class)
	protected OperationSetConfig changeOperators;
	
	@Element(name="translators", type=OperationSetConfigReal.class)
	protected OperationSetConfig translators;
	
	/** the class name of the beliefbase */
	@Element(name="beliefbase-class")
	protected String beliefbaseClassName;

	protected String viewName;
	
	@Commit
	protected void createOperationTypes() {
		// extract operation type from element name...
		((OperationSetConfigReal)reasonerOperators).operationType = BaseReasoner.OPERATION_TYPE;
		((OperationSetConfigReal)changeOperators).operationType = BaseChangeBeliefs.OPERATION_TYPE;
		((OperationSetConfigReal)this.translators).operationType = BaseTranslator.OPERATION_TYPE;
	}
	
	/**
	 * Reads a list of belief base configurations from a given xml file
	 * @param source	name of the xml file
	 * @return	list with configurations for belief bases.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static BeliefbaseConfigReal loadXml(File source) {
		return SerializeHelper.loadXml(BeliefbaseConfigReal.class, source);
	}
			
	@Override
	public String getBeliefbaseClassName() {
		return beliefbaseClassName;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public OperationSetConfig getReasoners() {
		return reasonerOperators;
	}

	@Override
	public OperationSetConfig getChangeOperators() {
		return changeOperators;
	}

	@Override
	public OperationSetConfig getTranslators() {
		return translators;
	}

	@Override
	public String getViewOn() {
		return viewName;
	}
}
