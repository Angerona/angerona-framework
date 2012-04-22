package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the dynamic configurations of a belief base.
 * It holds the type names of the classes use for different operations.
 * 
 * @author Tim Janus
 */
@Root(name="Beliefbase")
public class BeliefbaseConfiguration {
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(BeliefbaseConfiguration.class);
	
	
	@Element
	private String name;
	
	/** the class name used for the revision operation */
	@ElementList
	private List<String> changeClassName = new LinkedList<String>();

	/** the class name used for the reasoning operations */
	@Element
	private String reasonerClassName;
	
	/** the class name of the beliefbase */
	@Element
	private String beliefbaseClassName;
	
	
	/**
	 * Reads a list of belief base configurations from a given xml file
	 * @param filename	name of the xml file
	 * @return	list with configurations for belief bases.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static BeliefbaseConfiguration loadXml(String filename) throws IOException {
		Serializer serializer = new Persister();
		File source = new File(filename);
		BeliefbaseConfiguration reval = new BeliefbaseConfiguration();
		try {
			reval = serializer.read(BeliefbaseConfiguration.class, source);
		} catch (Exception e) {
			reval.LOG.error("Something went wrong during loading of '{}': {}", filename, e.getMessage());
			e.printStackTrace();
		}
		return reval;
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
		Serializer serializer = new Persister();
		BeliefbaseConfiguration test = new BeliefbaseConfiguration();
		test.beliefbaseClassName = "AspBeliefbase";
		test.changeClassName.add("ChangeOperator");
		test.reasonerClassName = "AspReasoner";
		test.name = "AspBeliefbase";
		try {
			serializer.write(test, System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
