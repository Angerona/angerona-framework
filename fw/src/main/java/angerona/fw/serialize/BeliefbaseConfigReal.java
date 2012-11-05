package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.xml.sax.SAXException;


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
	
	@Element(name="reasoners")
	protected OperatorSetConfig reasonerOperators;
	
	@Element(name="change-operators")
	protected OperatorSetConfig changeOperators;
	
	@Element(name="translators")
	protected OperatorSetConfig translators;
	
	/** the class name of the beliefbase */
	@Element(name="beliefbase-class")
	protected String beliefbaseClassName;
	
	
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
	public OperatorSetConfig getReasoners() {
		return reasonerOperators;
	}

	@Override
	public OperatorSetConfig getChangeOperators() {
		return changeOperators;
	}

	@Override
	public OperatorSetConfig getTranslators() {
		return translators;
	}
}
