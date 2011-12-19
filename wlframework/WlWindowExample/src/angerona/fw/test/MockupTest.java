package angerona.fw.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import angerona.fw.ui.TemplateComponent;

import com.whiplash.gui.WlWindow;
import com.whiplash.gui.WlWindowSet;
import com.whiplash.res.DefaultResourceManager;
import com.whiplash.res.WlResourceManager;

public class MockupTest {

	private static WlWindow window;
	
	private static WlWindowSet windowSet;
	
	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		DefaultResourceManager resourceManager = new DefaultResourceManager(new File(".").toURI().toURL());
		WlResourceManager.setDefaultResourceManager(resourceManager);
		
		JMenuBar menuBar = new JMenuBar();
		windowSet = new WlWindowSet(menuBar);
		window = windowSet.createWindow("Angerona - Simulation Monitor");
		window.setBounds(100, 100, 640, 480);
		window.setVisible(true);
		switchToSim();
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem("Exit"));
		menuBar.add(menuFile);
		
		JMenu menuPerspective = new JMenu("Perspective");
		JMenuItem miSim = new JMenuItem("Simulation");
		miSim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchToSim();
			}
		});
		menuPerspective.add(miSim);
		
		JMenuItem miConf = new JMenuItem("Config");
		miConf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				switchToConf();
			}
		});
		menuPerspective.add(miConf);
		menuBar.add(menuPerspective);
	}
	
	private static void switchToSim() {
		window.removeAllWlComponents();
		window.addWlComponent(new TemplateComponent("Sim. Report"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("Alice BB - World"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("Explanation - 'has_time' but lied: '-has_time'"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("Boss BB - View->Alice"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("Resources"), BorderLayout.WEST);
	}
	
	private static void switchToConf() {
		window.removeAllWlComponents();
		window.addWlComponent(new TemplateComponent("Config"), BorderLayout.WEST);
		window.addWlComponent(new TemplateComponent("Agent-Config: Dummy"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("BB-Config: ASP"), BorderLayout.CENTER);
		window.addWlComponent(new TemplateComponent("Simulation-Config: SCM"), BorderLayout.CENTER);
	}
}
