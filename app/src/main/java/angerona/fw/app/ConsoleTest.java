package angerona.fw.app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.commons.syntax.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import angerona.fw.Agent;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;

/**
 * A class with a main method for performing functional tests. Using the dummy implementations.
 *
 * @author Tim Janus
 */
public class ConsoleTest {
	static private Logger LOG = LoggerFactory.getLogger(ConsoleTest.class);
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws AgentInstantiationException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws AgentIdException, ParserConfigurationException, SAXException, IOException, AgentInstantiationException, InstantiationException, IllegalAccessException {
		for(String arg : args)
			System.out.println(arg);
		dataDrivenTest();
				
	}
	
	public static void dataDrivenTest() throws AgentIdException, ParserConfigurationException, SAXException, IOException, AgentInstantiationException, InstantiationException, IllegalAccessException {
		AngeronaEnvironment env = new AngeronaEnvironment();
		//env.loadSimulation("examples/strike_committee_meeting/simulation.xml");
		env.loadSimulation("examples/strike_committee_meeting/simulation_asp.xml");
		
		Agent boss = env.getAgentByName("Boss");
		Agent employee = env.getAgentByName("Employee");
		
		LOG.info("Employee Beliefs:\n" + employee.getBeliefs().toString());
		LOG.info("Boss Beliefs:\n" + boss.getBeliefs().toString());		
		
		env.run();
		
		LOG.info("Employee Beliefs:\n" + employee.getBeliefs().toString());
		LOG.info("Boss Beliefs:\n" + boss.getBeliefs().toString());
		
		AngeronaAnswer aa = boss.reason(new Atom(new Predicate("attends")));
		if(aa.getAnswerValue() == AnswerValue.AV_TRUE)
			LOG.info("Boss thinks the employee attends to the meeting");
		else if(aa.getAnswerValue() == AnswerValue.AV_FALSE)
			LOG.info("Boss thinks the empolyee doesn't attends to the meeting-");
		else
			LOG.info("Boss does not know.");
	}
}
