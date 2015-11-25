package com.github.kreatures.app;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.github.kreatures.gui.KReaturesGUIDataStorage;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.core.serialize.SimulationConfiguration;
import com.github.kreatures.core.util.Utility;
public class GUIApplication {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		KReaturesWindow.get().init();
		
		// load simulation per commandline.
		for(int k=0; k<args.length; ++k) {
			if(args[k].equalsIgnoreCase("-presentation")) {
				Utility.presentationMode = true;
			}
			
			String [] ary = args[k].split("=");
			if(ary.length == 2 && ary[0].equalsIgnoreCase("simulation")) {
				SimulationConfiguration config = SimulationConfiguration.loadXml(new File(ary[1]));
				KReaturesGUIDataStorage.get().getSimulationControl().setSimulation(config);
			}
		}
	}

}
