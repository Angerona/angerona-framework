package angerona.fw.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.Angerona;

import com.whiplash.gui.WlComponent;
import com.whiplash.gui.WlWindow;
import com.whiplash.gui.WlWindowSet;
import com.whiplash.res.DefaultResourceManager;
import com.whiplash.res.WlResourceManager;

public class SimulationMonitor  {
	private WlWindow window;
	
	private WlWindowSet windowSet;

	private SimulationControlBar simLoadBar;
	
	private static SimulationMonitor instance;
	
	public static SimulationMonitor getInstance() {
		if(instance == null) {
			instance = new SimulationMonitor();
		}
		return instance;
	}
	
	private SimulationMonitor() {
		DefaultResourceManager resourceManager = null;
		try {
			resourceManager = new DefaultResourceManager(new File(".").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WlResourceManager.setDefaultResourceManager(resourceManager);
		
		JMenuBar menuBar = new JMenuBar();
		windowSet = new WlWindowSet(menuBar);
		window = windowSet.createWindow("Angerona - Simulation Monitor");
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem("Exit"));
		menuBar.add(menuFile);
		
		window.pack();
	}

	public void init() throws ParserConfigurationException, SAXException,
			IOException {
		window.setTitle("Angerona - Simulation Monitor");
		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		Angerona angerona = Angerona.getInstance();
		angerona.addAgentConfigFolder("config/agents");
		angerona.addBeliefbaseConfigFolder("config/beliefbases");
		angerona.addSimulationFolders("config/examples");
		angerona.bootstrap();
		
		ReportView rv = new ReportView();
		ResourcenView resv = new ResourcenView();
		simLoadBar = new SimulationControlBar();
		
		window.addWlComponent(rv, BorderLayout.CENTER);
		window.addWlComponent(resv , BorderLayout.WEST);
		window.addWlComponent(simLoadBar, BorderLayout.SOUTH);
		
		System.out.println("Report: " + rv.getMinimumSize());
		System.out.println("Resourcen: " + resv.getMinimumSize());
		System.out.println("Simulation: " + simLoadBar.getMinimumSize());
	}
	
	public void addComponentToCenter(WlComponent component) {
		window.addWlComponent(component, BorderLayout.CENTER);
	}
	
	public void loadSimulation(String path) {
		File f = new File(path);
		if(f.exists()) {
			simLoadBar.loadSimulation(f);
		}
	}
}
