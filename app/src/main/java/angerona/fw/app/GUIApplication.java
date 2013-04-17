package angerona.fw.app;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.gui.AngeronaGUIDataStorage;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.serialize.SimulationConfiguration;
public class GUIApplication {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		AngeronaWindow.get().init();
		
		// load simulation per commandline.
		for(int k=0; k<args.length; ++k) {
			String [] ary = args[k].split("=");
			if(ary.length == 2 && ary[0].equalsIgnoreCase("simulation")) {
				SimulationConfiguration config = SimulationConfiguration.loadXml(new File(ary[1]));
				AngeronaGUIDataStorage.get().getSimulationControl().setSimulation(config);
			}
		}
	}

}
