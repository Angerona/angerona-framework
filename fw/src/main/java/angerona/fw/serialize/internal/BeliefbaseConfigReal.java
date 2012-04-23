package angerona.fw.serialize.internal;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.xml.sax.SAXException;

import angerona.fw.serialize.BeliefbaseConfig;

/**
 * Contains the dynamic configurations of a belief base.
 * It holds the type names of the classes use for different operations.
 * 
 * @author Tim Janus
 */
@Root(name="beliefbase-configuration")
public class BeliefbaseConfigReal implements BeliefbaseConfig {
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
	public static BeliefbaseConfigReal loadXml(File source) {
		return SerializeHelper.loadXml(BeliefbaseConfigReal.class, source);
	}
		
	@Override
	public String getRevisionClassName() {
		// TODO support list in framework
		return changeClassName.get(0);
	}

	@Override
	public String getReasonerClassName() {
		return reasonerClassName;
	}
	
	@Override
	public String getBeliefbaseClassName() {
		return beliefbaseClassName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public static void main(String [] args) {
		BeliefbaseConfigReal test = new BeliefbaseConfigReal();
		test.beliefbaseClassName = "AspBeliefbase";
		test.changeClassName.add("ChangeOperator");
		test.reasonerClassName = "AspReasoner";
		test.name = "AspBeliefbase";
		SerializeHelper.outputXml(test, System.out);
	}
}
