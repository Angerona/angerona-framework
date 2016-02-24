package com.github.kreatures.core.serialize;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.core.Persister;

import com.github.kreatures.core.asml.CommandSequence;
import com.github.kreatures.core.serialize.BeliefbaseConfig;
import com.github.kreatures.core.serialize.SimulationConfiguration;
import com.github.kreatures.swarm.serialize.SwarmAgentTypeConfig;
import com.github.kreatures.swarm.serialize.SwarmConfigRead;
import com.github.kreatures.swarm.serialize.SwarmPerspectiveConfig;

/**
 * 
 * @author donfack
 *
 */
public class CreateKReaturesXMLFileDefault implements CreateKReaturesXMLFile {
	private static Persister persister = new Persister();
	private static SwarmConfigRead swarmConfig;
	private static String fileLoaded; 
	private static String filepath; // folder where the simulation configuration's file is.
	// folder where all simulation configuration's file are.
	private static final String kreaturesConfigSimDirectory="../app/src/main/examples/";
	//Folder where the Abstract_Swarm-Dateien are
	private static final String AbstractSwarmDirectory="../app/src/main/config/swarm/";
	
	public CreateKReaturesXMLFileDefault() throws Exception{
		
		//this.testLoadSwarmXMLDatei(filepath); // nur für Test gegedacht, muss nachher gelöscht werden.
		loadSwarmXMLFile(); 
	}
	
	/**
	 * Load the XMLfile of Swarm into the object SwarmConfig. 
	 * @param filepaht is the way to the swarmXmlfile. 
	 * @return SwarmConfig, that is an object with all informations of swarm-scenario.
	 * @throws Exception
	 */
	
	private void loadSwarmXMLFile() throws Exception{
		// There are no current loaded simulation, which belong to abstract_Swarm.
		fileLoaded=null;
		//search all abstract_Swarm_config_files and creates the coresponding simulation file.	
		for( File file : CreateKReaturesXMLFileDefault.searchSwarmConfigFile(AbstractSwarmDirectory)){
			filepath=file.getName();
			System.out.println(filepath);
			filepath=getFilename(file.getName());
			swarmConfig = persister.read(SwarmConfigRead.class, file);	 
			System.out.println(kreaturesConfigSimDirectory+filepath);
			File fileKreatures=new File(kreaturesConfigSimDirectory+filepath);
			System.out.println(!fileKreatures.exists());
			
			if(!fileKreatures.exists()){
				fileKreatures.mkdir();
			}
		    	  
		    fileKreatures=new File(kreaturesConfigSimDirectory+filepath+"/"+filepath+"_simulation.xml");
		    System.out.println(kreaturesConfigSimDirectory+filepath+"/"+filepath+"_simulation.xml");
		    System.out.println(!fileKreatures.exists());
		    if(!fileKreatures.exists()){
			  persister.write(createSimulationConfig(), fileKreatures);
		    }
		}

	}
	
	/**
	 * @param filepath is a file's name with extension
	 * @return a file's name without extension.
	 */
	private static String getFilename(String filename){
		//TODO a Exeception has to be write
		if (filename==null)
			return null;
		String name=filename.substring(0, filename.lastIndexOf("."));
		return name;
	}

	

	//@Override
	private SimulationConfiguration createSimulationConfig() {
		
		SimulationConfiguration simulaConfig=new SimulationConfiguration() ;
		//List<SwarmPerspectiveConfig> swarmPerspectiveConfig=swarmConfig.getListPerspective() ;
		
		
		simulaConfig.name= filepath; // the file's is the simulation configuration's name.
		simulaConfig.category=filepath+"OfSwarm";
		simulaConfig.behaviorCls="com.github.kreatures.swarm.basic.SwarmBehavior";
		
		//agentInstance.
		simulaConfig.agents=createAgentConfig();
		
		//simulaConfig.setName(swarmConfig.getName());
		return simulaConfig;
	}

	//@Override
	private  List<AgentInstance> createAgentConfig() {
		List<AgentInstance> listAgentInstance=new LinkedList<AgentInstance>();
		for(SwarmPerspectiveConfig perceptionSwarm: swarmConfig.getListPerspective()){
			for(SwarmAgentTypeConfig agentSwarm: perceptionSwarm.getListAgentType() ){
				AgentInstance agentInstance=new AgentInstance();
				agentInstance.name=agentSwarm.getNameSwarmAgentType();
				AgentConfigImport agentConfigImport=new AgentConfigImport();
				File sourceOfAgentConfig=new File("config/agents/"+filepath+"_agent.xml");
				agentConfigImport.source=sourceOfAgentConfig;
				agentInstance.config= agentConfigImport;
				BeliefbaseConfigImport sourceOfBB=new BeliefbaseConfigImport();
				File sourceOfBBConfig=new File("config/beliefbases/asp_"+filepath+"_beliefbase.xml");
				sourceOfBB.source=sourceOfBBConfig;
				agentInstance.beliefbaseConfig=sourceOfBB;
				listAgentInstance.add(agentInstance);
			}
		}
		return listAgentInstance;
	}

	//@Override
	public BeliefbaseConfig createBeliefbaseConfigReal() {
		// TODO Auto-generated method stub
		return null;
	}
	public SwarmConfigRead getSwarmConfigRead(){
		
		return swarmConfig;
	}
	/**
	 * 
	 * @param dirName folder where the Abstract_Swarm-Dateien are located.
	 * @return List of all files which are into the abstract_Swarm folder.
	 */
	private static File[] searchSwarmConfigFile( String dirName){
    	File dir = new File(dirName);

    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".xml"); }
    	} );

    }

}