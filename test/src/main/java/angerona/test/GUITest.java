package angerona.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.gui.SimulationMonitor;
public class GUITest {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SimulationMonitor.getInstance().init();
		
		// load simulation per commandline.
		for(int k=0; k<args.length; ++k) {
			String [] ary = args[k].split("=");
			if(ary.length == 2 && ary[0].equalsIgnoreCase("simulation")) {
				SimulationMonitor.getInstance().loadSimulation(ary[1]);
			}
		}
	}

}
