package com.github.kreaturesfw.app;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.github.kreaturesfw.core.serialize.SimulationConfiguration;
import com.github.kreaturesfw.core.util.Utility;
import com.github.kreaturesfw.gui.AngeronaGUIDataStorage;
import com.github.kreaturesfw.gui.AngeronaWindow;
public class GUIApplication {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		AngeronaWindow.get().init();
		
		// load simulation per commandline.
		for(int k=0; k<args.length; ++k) {
			if(args[k].equalsIgnoreCase("-presentation")) {
				Utility.presentationMode = true;
			}
			
			String [] ary = args[k].split("=");
			if(ary.length == 2 && ary[0].equalsIgnoreCase("simulation")) {
				SimulationConfiguration config = SimulationConfiguration.loadXml(new File(ary[1]));
				AngeronaGUIDataStorage.get().getSimulationControl().setSimulation(config);
			}
		}
	}

}
