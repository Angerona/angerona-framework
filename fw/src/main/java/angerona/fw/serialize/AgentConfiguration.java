package angerona.fw.serialize;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains the dynamic configurations of an agent.
 * It holds the type names of the classes use for different operators.
 * 
 * @author Tim Janus
 */
public class AgentConfiguration {

	/** String identifying the GenerateOptions Operator class name for dynamic instantiation */
	private String generateOptionsOperatorClass;

	/** String identifying the Filter Operator class name for dynamic instantiation */
	private String filterOperatorClass;

	/** String identifying the Violates Operator class name for dynamic instantiation */
	private String violatesOperatorClass;

	/** String identifying the Policy-Control Operator class name for dynamic instantiation */
	private String policyControlOperatorClass;

	/** String identifying the Update Operator class name for dynamic instantiation */
	private String updateOperatorClass;

	/** String identifying the Planer class name for dynamic instantiation */
	private String planerClass;
	
	private List<String> componentClasses = new LinkedList<String>();
	
	/** String with name of this agent configuration */
	private String name;
	

	private static final String EL_ROOT = "AgentConfiguration";
	
	/**
	 * Loads a list of Agent configuration from a xml file.
	 * @param filename	filename of the xml file to load
	 * @return			A list of AgentConfiguration objects
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<AgentConfiguration> loadXml(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<AgentConfiguration> reval = new LinkedList<AgentConfiguration>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filename);
		
		NodeList nl = doc.getElementsByTagName(EL_ROOT);
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			AgentConfiguration cfg = loadFromElement(el);
			reval.add(cfg);
		}
		
		return reval;
	}
	
	/**
	 * Loads one AgentConfigurtion from a xml element
	 * @param el	The xml element containing the agent configuration.
	 * @return		The agent configuration data-structure which was saved in the xml element.
	 */
	public static AgentConfiguration loadFromElement(Element el) {
		AgentConfiguration reval = new AgentConfiguration();
		
		reval.name = el.getAttribute("name");
		reval.generateOptionsOperatorClass = getClassNameOfElement(el.getElementsByTagName("GenerateOptionsOperator"));
		reval.filterOperatorClass = getClassNameOfElement(el.getElementsByTagName("FilterOperator"));
		reval.violatesOperatorClass = getClassNameOfElement(el.getElementsByTagName("ViolatesOperator"));
		reval.policyControlOperatorClass = getClassNameOfElement(el.getElementsByTagName("PolicyControlOperator"));
		reval.planerClass = getClassNameOfElement(el.getElementsByTagName("Planer"));
		reval.updateOperatorClass = getClassNameOfElement(el.getElementsByTagName("UpdateOperator"));
		
		el = (Element)el.getElementsByTagName("Components").item(0);
		if(el != null) {
			NodeList lst = el.getElementsByTagName("Component");
			for(int k=0; k< lst.getLength(); ++k) {
				Element elC = (Element)lst.item(k);
				reval.componentClasses.add(elC.getAttribute("class"));
			}
		} 
		
		return reval;
	}
	
	/**
	 * Helper method: Returns the class attribute of the given element in the node list.
	 * The method returns null if the NodeList contains more than one Element.
	 * @param lst	list of nodes in the xml file
	 * @return		null if the list nodes doesn't has exactly one item, otherwise the string of the class attribute of the first element in the node list.
	 */
	private static String getClassNameOfElement(NodeList lst) {
		if(lst.getLength() != 1) {
			return null;
		} else {
			Element el = (Element)lst.item(0);
			return el.getAttribute("class");
		}
	}
	

	/** @return String identifying the GenerateOptions Operator class name */
	public String getGenerateOptionsOperatorClass() {
		return generateOptionsOperatorClass;
	}

	/** @return String identifying the Filter Operator class name */
	public String getFilterOperatorClass() {
		return filterOperatorClass;
	}

	/** @return String identifying the Violates Operator class name */
	public String getViolatesOperatorClass() {
		return violatesOperatorClass;
	}

	/** @return String identifying the Policy-Control Operator class name */
	public String getPolicyControlOperatorClass() {
		return policyControlOperatorClass;
	}

	/** @return String identifying the Update Operator class name */
	public String getUpdateOperatorClass() {
		return updateOperatorClass;
	}

	/** @return String identifying the Planer Operator class name */
	public String getPlanerClass() {
		return planerClass;
	}
	
	/** @return String with name of the agent configuration */
	public String getName() {
		return name;
	}
	
	public List<String> getComponents() {
		return Collections.unmodifiableList(componentClasses);
	}
}
