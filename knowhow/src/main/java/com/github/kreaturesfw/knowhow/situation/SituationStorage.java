package com.github.kreaturesfw.knowhow.situation;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.BaseAgentComponent;
import com.github.kreaturesfw.core.serialize.SerializeHelper;

/**
 * Stores different communication situations of the environment that can be
 * used by the agent for further reasoning
 * 
 * @author Tim Janus
 */
public class SituationStorage extends BaseAgentComponent {

	private static Logger LOG = LoggerFactory.getLogger(SituationStorage.class);
	
	private Map<String, Situation> situations = new HashMap<String, Situation>();
	
	public SituationStorage() {}
	
	public SituationStorage(SituationStorage ss) {
		for(Entry<String, Situation> entry : ss.situations.entrySet()) {
			// no deep clone needed because static data atm
			this.situations.put(entry.getKey(), entry.getValue());
		}
	}
	
	public Situation getSituation(String goal) {
		return situations.get(goal);
	}
	
	@Override
	public void init(Map<String, String> parameters) {
		String situationFiles = parameters.get("situationFiles");
		if(situationFiles != null) {
			String [] lines = situationFiles.split("\n");
			for(String line : lines) {
				line = line.trim();
				if(line.isEmpty())
					continue;
				
				String [] pair = line.split(",");
				if(pair.length != 2) {
					LOG.warn("Cannot parse line: '{}' it has to be in the format: <filename>,<situationClass>");
					continue;
				}
				String filename = pair[0].trim();
				String clsName = pair[1].trim();
				Class<?> cls = null;
				try {
					cls = Class.forName(clsName);
				} catch (ClassNotFoundException e1) {
					try {
						cls = Class.forName("com.github.angerona.knowhow.situation." + clsName);
					} catch(ClassNotFoundException e2) {
						LOG.warn("Cannot find class '{}' - '{]'", clsName, e1);
						continue;
					}
				}
				
				File f = new File(getAgent().getEnvironment().getDirectory() + "/" + filename);
				if(!f.exists()) {
					LOG.warn("The given file: '{}' does not exist", filename);
					continue;
				}
				
				try {
					Object o = SerializeHelper.get().loadXml(cls, new FileReader(f));
					if(o instanceof Situation) {
						Situation s = (Situation)o;
						situations.put(s.getGoal(), s);
					}
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));
					LOG.error("Cannot load situation xml file '{}' - '{}'", filename, sw.toString());
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public SituationStorage clone() {
		return new SituationStorage(this);
	}
	
}
