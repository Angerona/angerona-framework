package angerona.fw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.serialize.AgentConfigReal;
import angerona.fw.serialize.BeliefbaseConfigReal;
import angerona.fw.serialize.Resource;
import angerona.fw.serialize.SerializeHelper;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.util.ModelAdapter;
import angerona.fw.util.ObservableMap;

/**
 * Represents a project in the Angerona workspace.
 * @author Tim Janus
 */
public class AngeronaProject extends ModelAdapter {
	
	/** the property name of the resource map */
	public static final String RESOURCE_MAP_NAME = "resouceMap";
	
	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaProject.class);
	
	/** map containing all loaded resources ordered by name */
	private ObservableMap<String, Resource> resourceMap 
		= new ObservableMap<>(RESOURCE_MAP_NAME);
	
	/** Default Ctor: Registers observable maps */
	public AngeronaProject() {
		registerMap(resourceMap);
	}
	
	public void removeResource(Resource res) {
		if(resourceMap.remove(res.getName()) != null) {
			LOG.info("'{}' named '{}' removed from Angerona project.",
					res.getResourceType(), res.getName());
		}
	}
	
	private <T extends Resource> void loadResource(File file, Class<T> cls) {
		T res = SerializeHelper.loadXml(cls, file);
		resourceMap.put(res.getName(), res);
		LOG.info("'{}' '"+res.getName()+"' in '{}' added to Angerona project.", 
				res.getResourceType(), file.getAbsolutePath());
		
		// hack:
		if(res instanceof SimulationConfiguration) {
			((SimulationConfiguration)res).setFile(file);
		}
	}
	
	public void loadFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		
		Class<? extends Resource> cls = null;
		final int maxLines = 5;
		int counter = 0;
		
		while((line = reader.readLine()) != null && counter < maxLines) {
			if(line.contains("<agent-configuration>")) {
				cls = AgentConfigReal.class;
			} else if(line.contains("<simulation-configuration>")) {
				cls = SimulationConfiguration.class;
			} else if(line.contains("<beliefbase-configuration>")) {
				cls = BeliefbaseConfigReal.class;
			}
			
			++counter;
		}
		
		reader.close();
		
		if(cls != null) {
			loadResource(file, cls);
		} else {
			LOG.warn("The file '{}' neither is no Angerona resource, therefore it cannot be " +
					"loaded by the Angerona Project.", file.getAbsolutePath());
		}
	}
	
	/**
	 * Adds all the configuration files in the given directory and it sub-directory to the
	 * project.
	 * @param directory
	 * @throws IOException
	 */
	public void addDirectory(File directory) throws IOException {
		if(!directory.isDirectory())
			throw new IOException(directory.getAbsolutePath() + " is no directory.");
		
		File [] files = directory.listFiles();
		if(files == null)
			return;
		for(File actFile : files) {
			if(actFile.isFile() && actFile.getPath().endsWith("xml")) {
				loadFile(actFile);
			} else if(actFile.isDirectory()) {
				addDirectory(actFile);
			}
		}
	}
	
	public Map<String, Resource> getResources() {
		return Collections.unmodifiableMap(resourceMap);
	}
	
	public Resource getResource(String name) {
		return resourceMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Resource> T getResource(String name, Class<T> cls) {
		Resource reval = resourceMap.get(name);
		if(reval != null) {
			if(reval.getClass() != cls) {
				LOG.warn("");
				return null;
			}
			return ((T)reval);
		}
		return null;
	}
}
