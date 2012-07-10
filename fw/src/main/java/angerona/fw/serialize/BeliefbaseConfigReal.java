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
	private String name;
	
	@Element(name="reasoners")
	private OperatorSetConfig reasonerOperators;
	
	@Element(name="change-operators")
	private OperatorSetConfig changeOperators;
	
	@Element(name="translators")
	private OperatorSetConfig translators;
	
	/** the class name of the beliefbase */
	@Element(name="beliefbase-class")
	private String beliefbaseClassName;
	
	
	/**
	 * Reads a list of belief base configurations from a given xml file
	 * @param filename	name of the xml file
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
	
	public static void main(String [] args) {
		BeliefbaseConfigReal test = new BeliefbaseConfigReal();
		test.beliefbaseClassName = "AspBeliefbase";
		test.name = "AspBeliefbase";
		test.changeOperators = OperatorSetConfigReal.getExample("asp-change");
		test.reasonerOperators = OperatorSetConfigReal.getExample("asp-reasoner");
		test.translators = OperatorSetConfigReal.getExample("asp-translator");
		SerializeHelper.outputXml(test, System.out);
	}
}
