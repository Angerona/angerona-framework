package angerona.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.gui.SimulationMonitor;
public class GUITest {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		new SimulationMonitor();
	}

}
