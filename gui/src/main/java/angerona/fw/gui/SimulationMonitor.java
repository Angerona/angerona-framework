package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.xml.sax.SAXException;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.PluginInstantiator;
import angerona.fw.report.ReportAttachment;

import com.whiplash.gui.WlComponent;
import com.whiplash.gui.WlWindow;
import com.whiplash.gui.WlWindowSet;
import com.whiplash.res.DefaultResourceManager;
import com.whiplash.res.WlResourceManager;

public class SimulationMonitor  {
	private WlWindow window;
	
	private WlWindowSet windowSet;

	private SimulationControlBar simLoadBar;
	
	private Map<String, Class<? extends BaseComponent>> map = new HashMap<String, Class<? extends BaseComponent>>();
	
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
		
		JMenu menuWindow = new JMenu("Windows");
		JMenuItem miCreate = new JMenuItem("Create...");
		miCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				String str = (String) JOptionPane.showInputDialog(window, 
						"Select a Window to create...",
						"Create Window",
						JOptionPane.PLAIN_MESSAGE,
						null,
						map.keySet().toArray(),
						null);
				if(map.containsKey(str)) {
					try {
						BaseComponent bc = map.get(str).newInstance();
						Class<?> type = bc.getObservationObjectType();
						if(type == null) {
							bc.init();
							window.addWlComponent(bc, BorderLayout.CENTER);
						} else {
							AngeronaEnvironment env = simLoadBar.getEnvironment();
							List<ReportAttachment> tempList = new LinkedList<ReportAttachment>();
							for(Long id : env.getEntityMap().keySet()) {
								ReportAttachment possible = env.getEntityMap().get(id);
								if(possible.getClass().equals(bc.getObservationObjectType())) {
									tempList.add(possible);
								}
							}
							
							List<String> names = new LinkedList<String>();
							for(ReportAttachment att : tempList) {
								names.add("<" + att.getGUID() + ">");
							}
							
							ReportAttachment selection = null;
							if(tempList.size() == 0) {
								JOptionPane.showMessageDialog(window, "No object for observation found.");
							} else if(tempList.size() == 1) {
								selection = tempList.get(0);
							} else {
								String str2 = (String) JOptionPane.showInputDialog(window, 
										"Select a Object for observation...",
										"Create Window 2",
										JOptionPane.PLAIN_MESSAGE,
										null,
										names.toArray(),
										null);
								
								int index = names.indexOf(str2);
								if(index != -1) {
									selection = tempList.get(index);
								}
							}
							
							if(selection != null) {
								BaseComponent comp = SimulationMonitor.createBaseComponent(map.get(str), selection);
								SimulationMonitor.getInstance().addComponentToCenter(comp);
							}
						}
						
						
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		menuWindow.add(miCreate);
		menuBar.add(menuWindow);
		
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
		
		map.put("Report-View", ReportView.class);
		map.put("Resourcen-View", ResourcenView.class);
		
		// TODO: Move this somewhere else.
		PluginManagerUtil pluginManagerUtil = PluginInstantiator.getPluginManagerUtil();
		Collection<UIPlugin> uiPlugins = new LinkedList<UIPlugin>(pluginManagerUtil.getPlugins(UIPlugin.class));
		for(UIPlugin pl : uiPlugins) {
			map.putAll(pl.getUIComponents());
		}
		
		ReportView rv = new ReportView();
		rv.init();
		ResourcenView resv = new ResourcenView();
		resv.init();
		simLoadBar = new SimulationControlBar();
		simLoadBar.init();
		
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
	
	public WlWindow getWindow() {
		return window;
	}
	
	public static <T extends BaseComponent> T createBaseComponent(Class<? extends T> cls, Object toObserve) {
		T reval;
		try {
			reval = cls.newInstance();
			if(toObserve != null) {
				reval.setObservationObject(toObserve);
			}
			reval.init();
			return reval;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
