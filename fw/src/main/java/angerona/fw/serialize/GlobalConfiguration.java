package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;

/**
 * This class is responsible for the serialization of the global configuration
 * file of Angerona. This file contains a list of plugins to load and a list of
 * parameters which are key value pairs.
 * @author Tim Janus
 *
 */
@Root(name="config")
public class GlobalConfiguration {
	
	@ElementList(name="plugins", inline=true, entry="plugin")
	private List<String>	pluginPaths = new LinkedList<String>();
	
	@ElementMap(entry="parameter", key="name", value="value", attribute=true, inline=true, required=false)
	private Map<String, String>	parameters = new HashMap<>();
	
	public List<String> getPluginPaths() {
		return Collections.unmodifiableList(pluginPaths);
	}
	
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}
	
	/**
	 * Adds to the parameter with the given name a postfix for representing the 
	 * executables on different OSes if the name is no file.
	 * @param name	The name of the parameter
	 * @return		A string representing the path to the exeutable inclusive os-dependent postfix.
	 */
	public String getAsExecutable(String name) {
		String reval = null;
		if(parameters.containsKey(name)) {
			reval = parameters.get(name);
			if(!new File(reval).exists()) {
				reval += System.getProperty("os.name").toLowerCase().indexOf("win") >= 0 ? ".exe" : "";
				reval += System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0 ? ".bin" : "";
				if(! new File(reval).exists()) {
					reval = null;
				}
			} 
		}
		return reval;
	}
	
	/**
	 * loads the global-configuration from the given file-path.
	 * @param source	reference to the source File.
	 * @return			Data structure containing the global configuration
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static GlobalConfiguration loadXml(File source) {
		return SerializeHelper.loadXml(GlobalConfiguration.class, source);
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
		
		test.parameters.put("path-dlv-complex", "D:/dlv-complex");
		try {
			serializer.write(test, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
