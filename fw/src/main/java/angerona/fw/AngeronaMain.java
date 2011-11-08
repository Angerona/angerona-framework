package angerona.fw;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.serialize.GlobalConfiguration;

/**
 * Main class of the framework having global class instances like the global configuration.
 * @author Tim Janus
 *
 */
public class AngeronaMain {
	private static GlobalConfiguration config = null;
	
	public static GlobalConfiguration getConfig() {
		if(config == null) {
			try {
				config = GlobalConfiguration.loadXml("config/configuration.xml");
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(config == null)
				config = new GlobalConfiguration();
		}
		return config;
	}
}
