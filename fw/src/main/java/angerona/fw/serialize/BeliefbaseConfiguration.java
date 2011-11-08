package angerona.fw.serialize;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseBeliefbase.UpdateType;

/**
 * Contains the dynamic configurations of a belief base.
 * It holds the type names of the classes use for different operations.
 * 
 * @author Tim Janus
 */
public class BeliefbaseConfiguration {
	/** the class name used for the expansion operation */
	private String expansionClassName;
	
	/** the class name used for the consolidation operation */
	private String consolidationClassName;

	/** the class name used for the revision operation */
	private String revisionClassName;

	/** the class name used for the reasoning operations */
	private String reasonerClassName;
	
	/** the class name of the beliefbase */
	private String beliefbaseClassName;
	
	/** the updateBehavior Attribute String */
	private String updateBehaviorStr;
	
	/** string representing the expansion update behavior */
	public static final String EXPANSION 					= "EXPANSION";
	
	/** string representing the expansion and consolidation update behavior */
	public static final String EXPANSION_AND_CONSOLIDATION 	= "EXPANSION_AND_CONSOLIDATION";
	
	/** string representing the revision update behavior */
	public static final String REVISION						= "REVISION";
	
	// The unique element identifier
	private static final String EL_ROOT = "BeliefbaseConfiguration";
	private static final String EL_REASONER = "Reasoner";
	private static final String EL_EXPANSION = "Expansion";
	private static final String EL_CONSOLIDATION = "Consolidation";
	private static final String EL_REVISION = "Revision";
	
	// The unique attribute identifier
	private static final String A_CLASS = "class";
	private static final String A_UPDATEBEHAVIOR = "updateBehavior";
	
	/**
	 * Reads a list of belief base configurations from a given xml file
	 * @param filename	name of the xml file
	 * @return	list with configurations for belief bases.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<BeliefbaseConfiguration> loadXml(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<BeliefbaseConfiguration> reval = new LinkedList<BeliefbaseConfiguration>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filename);
		
		NodeList nl = doc.getElementsByTagName(EL_ROOT);
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			BeliefbaseConfiguration cfg = loadFromElement(el);
			cfg.updateBehaviorStr = el.getAttribute(A_UPDATEBEHAVIOR);
			reval.add(cfg);
		}
		return reval;
	}

	/**
	 * Loads one belief base configuration from a given xml element.
	 * @param el	reference to the xml element containing the belief base configuration data.
	 * @return		An in-memory data structure of the belief base configuration.
	 */
	public static BeliefbaseConfiguration loadFromElement(Element el) {
		BeliefbaseConfiguration reval = new BeliefbaseConfiguration();
		reval.beliefbaseClassName = getClassNameOfElement(el);
		reval.reasonerClassName = getClassNameOfElement(el.getElementsByTagName(EL_REASONER));
		reval.expansionClassName = getClassNameOfElement(el.getElementsByTagName(EL_EXPANSION));
		reval.consolidationClassName = getClassNameOfElement(el.getElementsByTagName(EL_CONSOLIDATION));
		reval.revisionClassName = getClassNameOfElement(el.getElementsByTagName(EL_REVISION));
		
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
			return getClassNameOfElement((Element)lst.item(0));
		}
	}
	
	private static String getClassNameOfElement(Element el) {
		if(el == null)
			throw new IllegalArgumentException("The given element must not be null");
		return el.getAttribute(A_CLASS);
	}
	
	/** @return the class name used for the expansion operation */
	public String getExpansionClassName() {
		return expansionClassName;
	}

	/** @return the class name used for the consolidation operation */
	public String getConsolidationClassName() {
		return consolidationClassName;
	}

	/** @return the class name used for the revision operation */
	public String getRevisionClassName() {
		return revisionClassName;
	}

	/** @return the class name used for the reasoning operations */
	public String getReasonerClassName() {
		return reasonerClassName;
	}
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName() {
		return beliefbaseClassName;
	}
	
	public BaseBeliefbase.UpdateType getUpdateBehavior() {
		if(updateBehaviorStr.compareToIgnoreCase(EXPANSION) == 0) {
			return UpdateType.U_EXPANSION;
		} else if(updateBehaviorStr.compareToIgnoreCase(EXPANSION_AND_CONSOLIDATION) == 0) {
			return UpdateType.U_EXPANSION_AND_CONSOLIDATION;
		} else if(updateBehaviorStr.compareToIgnoreCase(REVISION) == 0) {
			return UpdateType.U_REVISION;
		}
		
		throw new IllegalArgumentException(updateBehaviorStr + " can't be convert to UpdateType");
	}
}
