package com.github.kreatures.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.error.KReaturesException;
import com.github.kreatures.core.serialize.AgentConfigReal;
import com.github.kreatures.core.serialize.BeliefbaseConfigReal;
import com.github.kreatures.core.serialize.Resource;
import com.github.kreatures.core.serialize.SerializeHelper;
import com.github.kreatures.core.serialize.SimulationConfiguration;
import com.github.kreatures.core.util.ModelAdapter;
import com.github.kreatures.core.util.ObservableMap;
import com.github.kreatures.core.util.Utility;

/**
 * Represents a project in the KReatures workspace.
 * @author Tim Janus
 */
public class KReaturesProject extends ModelAdapter {
	
	/** the property name of the resource map */
	public static final String RESOURCE_MAP_NAME = "resouceMap";
	
	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(KReaturesProject.class);
	
	/** map containing all loaded resources ordered by name */
	private ObservableMap<String, Resource> resourceMap 
		= new ObservableMap<>(RESOURCE_MAP_NAME);
	
	/** Default Ctor: Registers observable maps */
	public KReaturesProject() {
		registerMap(resourceMap);
	}
	
	public void removeResource(Resource res) {
		if(resourceMap.remove(res.getName()) != null) {
			LOG.info("'{}' named '{}' removed from KReatures project.",
					res.getResourceType(), res.getName());
		}
	}
	
	private <T extends Resource> void loadResource(File file, Class<T> cls) {
		try {
			T res = SerializeHelper.get().loadXml(cls, file);
			resourceMap.put(res.getName(), res);
			LOG.info("'{}' '"+res.getName()+"' in '{}' added to KReatures project.", 
					res.getResourceType(), file.getAbsolutePath());
			
			// hack:
			if(res instanceof SimulationConfiguration) {
				((SimulationConfiguration)res).setFile(file);
			}
		} catch(Exception e) {
			KReatures.getInstance().onError("Cannot parse file!", "Cannot parse: '" + file.getPath() + "':\n" +
					Utility.format(e));
		}
	}
	
	public void loadFile(File file) throws IOException, KReaturesException {
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
			String message = String.format("The file '%s' is no KReatures resource, " +
					"therefore it cannot be loaded by the KReatures Project.", 
					file.getAbsolutePath());
			LOG.warn(message);
			throw new KReaturesException(message);
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
				try {
					loadFile(actFile);
				} catch(KReaturesException e) {
					LOG.info("'{}' is no KReatures project resource and therefore be skipped.", 
							actFile.getAbsolutePath());
				}
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
