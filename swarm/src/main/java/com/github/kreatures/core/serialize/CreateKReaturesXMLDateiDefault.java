package com.github.kreatures.core.serialize;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.core.Persister;


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
public class CreateKReaturesXMLDateiDefault implements CreateKReaturesXMLDatei {
	private static Persister persister = new Persister();
	private static File file;
	private static SwarmConfigRead swarmConfig;
	private String filepath; // folder where the simulation configuration's file is.
	private String kreaturesConfigSimDirectory="../app/src/main/examples/"; // folder where all simulation configuration's file are.

	
	public CreateKReaturesXMLDateiDefault(String filepath) throws Exception{
		
		//this.testLoadSwarmXMLDatei(filepath); // nur für Test gegedacht, muss nachher gelöscht werden.
		this.loadSwarmXMLDatei(filepath); 
	}
	
	public void testLoadSwarmXMLDatei(String filepath) throws Exception{
		
		file = new File(filepath);
		
		this.filepath=getFilename(file.getName());
		
	      
	    swarmConfig = persister.read(SwarmConfigRead.class, file);	     
	    System.out.println("Filename is "+this.filepath);
	    persister.write(swarmConfig, System.out);		
	}
	/**
	 * @param filepath is a file's name with extension
	 * @return a file's name without extension.
	 */
	private String getFilename(String filename){
		//TODO a Exeception has to be write
		if (filename==null)
			return null;
		String name=filename.substring(0, filename.lastIndexOf("."));
		return name;
	}
	/**
	 * Load the XMLfile of Swarm into the object SwarmConfig. 
	 * @param filepaht is the way to the swarmXmlfile. 
	 * @return SwarmConfig, that is an object with all informations of swarm-scenario.
	 * @throws Exception
	 */
	
	public void loadSwarmXMLDatei(String filepath) throws Exception{
			
		file = new File(filepath);
		
		this.filepath=getFilename(file.getName());
		
	      
	      swarmConfig = persister.read(SwarmConfigRead.class, file);	     
	     // System.out.println("Filename is "+this.filepath);
	     // persister.write(swarmConfig, System.out);		
	    /*  
	      file=new File(kreaturesConfigSimDirectory+this.filepath);
	      
	      if(!file.exists()){
	    	  file.mkdir();
	      }
    	  
	      file=new File(kreaturesConfigSimDirectory+this.filepath+"/"+this.filepath+"_simulation.xml");
    	  
	      if(!file.exists())
    		  persister.write(createSimulationConfig(), file);
			*/
    	  //System.out.println("Fertig");
	}
	

	@Override
	public SimulationConfiguration createSimulationConfig() {
		
		SimulationConfiguration simulaConfig=new SimulationConfiguration() ;
		//List<SwarmPerspectiveConfig> swarmPerspectiveConfig=swarmConfig.getListPerspective() ;
		
		
		simulaConfig.name= this.filepath; // the file's is the simulation configuration's name.
		simulaConfig.category=filepath+"OfSwarm";
		simulaConfig.behaviorCls="com.github.kreatures.swarm.basic.SwarmBehavior";
		
		//agentInstance.
		simulaConfig.agents=createAgentConfig();
		
		//simulaConfig.setName(swarmConfig.getName());
		return simulaConfig;
	}

	@Override
	public List<AgentInstance> createAgentConfig() {
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

	@Override
	public BeliefbaseConfig createBeliefbaseConfigReal() {
		// TODO Auto-generated method stub
		return null;
	}
	public SwarmConfigRead getSwarmConfigRead(){
		
		return swarmConfig;
	}
}