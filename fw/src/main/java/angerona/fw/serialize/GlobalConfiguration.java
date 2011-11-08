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

public class GlobalConfiguration {
	private List<String>	pluginPaths = new LinkedList<String>();
	
	static private final String EL_ROOT = "Config";
	
	public List<String> getPluginPaths() {
		return pluginPaths;
	}
	
	public static GlobalConfiguration loadXml(String filepath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(filepath);
		
		NodeList nl = doc.getElementsByTagName(EL_ROOT);
		if(nl.getLength() > 0)
			return loadFromElement((Element)nl.item(0));
		
		return null;
	}
	
	/**
	 * Loads the global configuration from a xml element
	 * @param el	The xml element containing the global configuration.
	 * @return		The global configuration data-structure which was saved in the xml element.
	 */
	public static GlobalConfiguration loadFromElement(Element el) {
		GlobalConfiguration reval = new GlobalConfiguration();
		
		NodeList nl = el.getElementsByTagName("Plugin");
		for(int i=0; i<nl.getLength(); ++i) {
			Element actEl = (Element)nl.item(i);
			reval.pluginPaths.add(actEl.getAttribute("file"));
		}
		
		return reval;
	}
}
