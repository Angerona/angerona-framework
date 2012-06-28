package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
public class BeliefbaseConfigReal implements BeliefbaseConfig {
	@Element(name="name")
	private String name;
	
	@Element(name="default-reasoner")
	private String defaultReasonerClass;
	
	@Element(name="default-change-operator")
	private String defaultChangeClass;
	
	/** the class name used for the revision operation */
	@ElementList(name="change-operators", inline=true, entry="change-operator", empty=false)
	private Set<String> changeClassNames = new HashSet<String>();

	/** the class name used for the reasoning operations */
	@ElementList(name="reasoner-class", inline=true, entry="reasoner", empty=false)
	private Set<String> reasonerClassNames = new HashSet<String>();
	
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
	public Set<String> getRevisionClassName() {
		return Collections.unmodifiableSet(changeClassNames);
	}

	@Override
	public Set<String> getReasonerClassName() {
		return reasonerClassNames;
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
	public String getDefaultReasonerClass() {
		return defaultReasonerClass;
	}

	@Override
	public String getDefaultChangeClass() {
		return defaultChangeClass;
	}
	
	public static void main(String [] args) {
		BeliefbaseConfigReal test = new BeliefbaseConfigReal();
		test.beliefbaseClassName = "AspBeliefbase";
		test.changeClassNames.add("ChangeOperator");
		test.reasonerClassNames.add("AspReasoner");
		test.name = "AspBeliefbase";
		test.defaultChangeClass = "ChangeOperator";
		test.defaultReasonerClass = "Reasoner";
		SerializeHelper.outputXml(test, System.out);
	}
}
