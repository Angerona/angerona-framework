package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

@Root(name="Config")
public class GlobalConfiguration {
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(GlobalConfiguration.class);
	
	
	@ElementList
	private List<String>	pluginPaths = new LinkedList<String>();
	
	public List<String> getPluginPaths() {
		return pluginPaths;
	}
	
	/**
	 * loads the global-configuration from the given file-path.
	 * @param filepath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static GlobalConfiguration loadXml(String filepath) throws IOException {
		Serializer serializer = new Persister();
		File source = new File(filepath);
		GlobalConfiguration reval = new GlobalConfiguration();
		try {
			reval = serializer.read(GlobalConfiguration.class, source);
		} catch (Exception e) {
			reval.LOG.error("Something went wrong during loading of '{}': {}", filepath, e.getMessage());
			e.printStackTrace();
		}
		return reval;
	}
	
	/**
	 * Test method for proofing the syntax of xml test files.
	 * @param args
	 */
	public static void main(String args []) {
		Serializer serializer = new Persister();
		GlobalConfiguration test = new GlobalConfiguration();
		test.pluginPaths.add("AspPlugin.jar");
		test.pluginPaths.add("DummyPlugin.jar");
		try {
			serializer.write(test, System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
