package angerona.fw.serialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains the configuration for a Skill. It holds the
 * Skill name and an ordered list of statements which process
 * the Skill.
 * @author Tim Janus
 */
public class SkillConfiguration {
	
	/**
	 * A statement of a Skill. It consist of a name of the commando
	 * a name for the return value and a map of parameter names.
	 * @author Tim Janus
	 */
	public class Statement {
		/** The name of the commando */
		private String commandoName;
		
		/** a map from parameter names to object names in the context */
		private Map<String, String> parameterMap = new HashMap<String, String>();
		
		/** the future name of the return value of this commando in the context */
		private String outName;
	
		/** an inner xml element for complex commandos like sendAction */
		private Element innerElement;
		
		/** @return the name of the commando */
		public String getCommandoName() {
			return commandoName;
		}

		/** @return a map from parameter names to object names in the context */
		public Map<String, String> getParameterMap() {
			return parameterMap;
		}

		/** @return the future name of the return value of this commando in the context */
		public String getOutName() {
			return outName;
		}
		
		/** @return an inner xml element for complex commandos like sendAction */
		public Element getInnerElement() {
			return innerElement;
		}
	}
	
	/** the name of the Skill */
	private String name;
	
	/** @return the name of the Skill */
	public String getName() {
		return name;
	}

	/** @return ordered list of statements performing the Skill */
	public List<Statement> getStatements() {
		return statements;
	}

	/** ordered list of statements performing the Skill */
	private List<Statement> statements = new LinkedList<SkillConfiguration.Statement>();
	
	/** the name of the root xml element of an intention xml file */
	private static final String EL_ROOT = "Skill";
	
	/**
	 * Reads a list of belief base configurations from a given xml file
	 * @param filename	name of the xml file
	 * @return	list with configurations for belief bases.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<SkillConfiguration> loadXml(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<SkillConfiguration> reval = new LinkedList<SkillConfiguration>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filename);
		
		NodeList nl = doc.getElementsByTagName(EL_ROOT);
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			reval.add(loadFromElement(el));
		}
		return reval;
	}

	/**
	 * Loads one belief base configuration from a given xml element.
	 * @param el	reference to the xml element containing the belief base configuration data.
	 * @return		An in-memory data structure of the belief base configuration.
	 */
	public static SkillConfiguration loadFromElement(Element el) {
		SkillConfiguration reval = new SkillConfiguration();
		reval.name = el.getAttribute("name");
		
		for(int i=0; i<el.getChildNodes().getLength(); ++i) {
			Node n = el.getChildNodes().item(i);
			if(!(n instanceof Element)) {
				continue;
			}
			Element act = (Element)n;
			
			if(act.getNodeName().equalsIgnoreCase("desires")) {
				// TODO: Read the desires
				continue;
			} else if(act.getNodeName().equalsIgnoreCase("commandos")) {
				reval = parseCommandosElement(act, reval);
			}
		}
		
		return reval;
	}
	
	/**
	 * Helper method: parses the commando elements of the Skill (the statements)
	 * @param commandos element containing the commando/statement list.
	 * @param config actual configuration instance 
	 * @return it returns the same reference as the given config parameter.
	 */
	private static SkillConfiguration parseCommandosElement(Element commandos, SkillConfiguration config) {
		for(int k=0; k<commandos.getChildNodes().getLength(); ++k) {
			if(commandos.getChildNodes().item(k) instanceof Element) {
				Element el = (Element)commandos.getChildNodes().item(k);
				Statement st = config.new Statement();
				config.statements.add(st);
				st.commandoName = el.getNodeName();
				
				for(int i=0; i<el.getChildNodes().getLength(); ++i) {
					if(el.getChildNodes().item(i) instanceof Element) {
						if(st.innerElement != null) {
							// TODO: Throw exception.
						}
						st.innerElement = (Element)el.getChildNodes().item(i);
					}
				}
				
				NamedNodeMap attributes = el.getAttributes();
				for(int i=0; i<attributes.getLength(); ++i) {
					Attr attribute = (Attr)attributes.item(i);
					if(attribute.getNodeName().equals("out")) {
						st.outName = attribute.getValue();
					} else {
						st.parameterMap.put(attribute.getNodeName(), attribute.getValue());
					}
					
				}
			}
		}
		return config;
	}
}
