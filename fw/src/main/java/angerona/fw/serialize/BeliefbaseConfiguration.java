package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.xml.sax.SAXException;

/**
 * Contains the dynamic configurations of a belief base.
 * It holds the type names of the classes use for different operations.
 * 
 * @author Tim Janus
 */
@Root(name="beliefbase-configuration")
public class BeliefbaseConfiguration {
	@Element
	private String name;
	
	/** the class name used for the revision operation */
	@ElementList(name="change-operators")
	private List<String> changeClassName = new LinkedList<String>();

	/** the class name used for the reasoning operations */
	@Element(name="reasoner-class")
	private String reasonerClassName;
	
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
	public static BeliefbaseConfiguration loadXml(File source) {
		return SerializeHelper.loadXml(BeliefbaseConfiguration.class, source);
	}
		
	/** @return the class name used for the revision operation */
	public String getRevisionClassName() {
		// TODO support list in framework
		return changeClassName.get(0);
	}

	/** @return the class name used for the reasoning operations */
	public String getReasonerClassName() {
		return reasonerClassName;
	}
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName() {
		return beliefbaseClassName;
	}
	
	public String getName() {
		return name;
	}
	
	public static void main(String [] args) {
		BeliefbaseConfiguration test = new BeliefbaseConfiguration();
		test.beliefbaseClassName = "AspBeliefbase";
		test.changeClassName.add("ChangeOperator");
		test.reasonerClassName = "AspReasoner";
		test.name = "AspBeliefbase";
		SerializeHelper.outputXml(test, System.out);
	}
}
