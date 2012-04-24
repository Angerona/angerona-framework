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
import org.xml.sax.SAXException;


@Root(name="config")
public class GlobalConfiguration {
	
	@ElementList(name="plugins")
	private List<String>	pluginPaths = new LinkedList<String>();
	
	public List<String> getPluginPaths() {
		return pluginPaths;
	}
	
	/**
	 * loads the global-configuration from the given file-path.
	 * @param source	reference to the source File.
	 * @return
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
		try {
			serializer.write(test, System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
