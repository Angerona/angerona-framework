package angerona.fw.serialize;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class instance holding all the configuration options for a complete simulation.
 * 
 * @author Tim Janus
 */
public class SimulationConfiguration {
	
	/**
	 * An instance of this class represents one agent in the simulation
	 * @author Tim Janus
	 */
	public class AgentInstance {		
		/** the unique name of the agent */
		private String name;
		
		/** a data structure with type information of the agents operators */
		private AgentConfiguration config;
		
		/** data structure containing information about the beliefbase type and operators */
		private BeliefbaseConfiguration beliefbaseConfig;

		/** the file suffix identifying the belifbase */
		private String fileSuffix;
		
		/** list of file names for used intentions */
		private List<SkillConfiguration> skillConfigs = new LinkedList<SkillConfiguration>();
	
		private List<FolFormula> desires = new LinkedList<FolFormula>();
		
		private Map<String, String> additionalData = new HashMap<String, String>();
	
		/** @return the unique name of the agent */
		public String getName() {
			return name;
		}

		/** @return a data structure with type information of the agents operators */
		public AgentConfiguration getConfig() {
			return config;
		}
		
		/** @return list of file names for used intentions */
		public List<SkillConfiguration> getSkillConfig() {
			return skillConfigs;
		}
		
		public List<FolFormula> getDesires() {
			return desires;
		}
		
		
		public Map<String, String> getAdditionalData() {
			return Collections.unmodifiableMap(additionalData);
		}
		
		/**
		 * Helper method: Loads the agent information form the given xml element
		 * @param el	xml element containing data about the agent.
		 * @throws ParserConfigurationException
		 * @throws SAXException
		 * @throws IOException
		 */
		public void loadFromElement(Element el) throws ParserConfigurationException, SAXException, IOException {
			name = el.getAttribute("name");
			String configFile = el.getAttribute("configuration");
			
			NodeList lst = el.getElementsByTagName("Skill");
			for(int i=0; i<lst.getLength(); ++i) {
				Element ie = (Element)lst.item(i);
				String filename = ie.getAttribute("file"); 
				try {
					List<SkillConfiguration> skills = SkillConfiguration.loadXml(filename);
					skillConfigs.addAll(skills);
				} catch(FileNotFoundException fnf) {
					fnf.printStackTrace();
					throw new ParserConfigurationException(fnf.getMessage());
				}	
			}
			
			Element elDes = (Element)el.getElementsByTagName("Desires").item(0);
			if(elDes != null) {
				lst = elDes.getElementsByTagName("Desire");
				for(int i=0; i<lst.getLength(); ++i) {
					Element des = (Element)lst.item(i);
					FolFormula formula = new Atom(new Predicate(des.getTextContent()));
					desires.add(formula);
				}
			}
			
			Element elAdditionalData = (Element)el.getElementsByTagName("AdditionalData").item(0);
			if(elAdditionalData != null) {
				lst = elAdditionalData.getChildNodes();
				for(int i=0; i<lst.getLength(); ++i) {
					if(!(lst.item(i) instanceof Element)) 
						continue;
					
					Element act = (Element)lst.item(i);
					additionalData.put(act.getNodeName(), act.getTextContent());
				}
			}
			
			try {
				config = AgentConfiguration.loadXml(configFile).get(0);
			} catch (FileNotFoundException ex) {
				throw new ParserConfigurationException(ex.getMessage());
			}
			Element bel = (Element)el.getElementsByTagName("Beliefbase").item(0);			
			fileSuffix = bel.getAttribute("fileSuffix");
			beliefbaseConfig = BeliefbaseConfiguration.loadXml(bel.getAttribute("configuration"));
		}

		public BeliefbaseConfiguration getBeliefbaseConfig() {
			return beliefbaseConfig;
		}

		public String getFileSuffix() {
			return fileSuffix;
		}

		public List<SkillConfiguration> getSkillConfigs() {
			return skillConfigs;
		}
	
	}

	/** name of the root xml element of an simulation */
	private static final String EL_ROOT =  "AngeronaSimulation";
	
	/** name of the simulation */
	private String name;
	
	/** collection of data structure containing the agents of the simulation */
	private List<AgentInstance> agents = new LinkedList<SimulationConfiguration.AgentInstance>();
	
	/** xml "flow" element containing the initial perceptions of the simulation */
	private Element flowElement;
	
	/** @return name of the simulation */
	public String getName() {
		return name;
	}

	/** @return collection of data structure containing the agents of the simulation */
	public List<AgentInstance> getAgents() {
		return agents;
	}
	
	/** @return xml "flow" element containing the initial perceptions of the simulation */
	public Element getFlowElement() {
		return flowElement;
	}

	
	/**
	 * Helper method: Loads the simulation configuration data structure from the given filename
	 * @param filename	name of the file containing the simulation configuration.
	 * @return list of simulation configuration instances
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<SimulationConfiguration> loadXml(String filename) throws ParserConfigurationException, SAXException, IOException {
		List<SimulationConfiguration> reval = new LinkedList<SimulationConfiguration>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filename);
		
		NodeList nl = doc.getElementsByTagName(EL_ROOT);
		for (int i = 0; i < nl.getLength(); i++) {
			Element el = (Element) nl.item(i);
			SimulationConfiguration cfg = loadFromElement(el);
			reval.add(cfg);
		}
		return reval;
	}
	
	/**
	 * Helper method: Loads a simulation configuration instance from the given xml element.
	 * @param el 	Reference to the xml element containing the simulation configuration.
	 * @return		The instance of the simulation configuration.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static SimulationConfiguration loadFromElement(Element el) throws ParserConfigurationException, SAXException, IOException {
		SimulationConfiguration reval = new SimulationConfiguration();
		reval.name = el.getAttribute("name");
		
		NodeList list = el.getElementsByTagName("Agent");
		for(int i=0; i<list.getLength(); ++i) {
			Element elAgent = (Element) list.item(i);
			AgentInstance ai = reval.new AgentInstance();
			
			ai.loadFromElement(elAgent);
			reval.agents.add(ai);
		}
		
		reval.flowElement = (Element)el.getElementsByTagName("Flow").item(0);
		
		return reval;
	}
}
