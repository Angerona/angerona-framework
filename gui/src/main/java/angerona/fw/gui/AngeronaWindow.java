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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import angerona.fw.AgentComponent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.view.BaseView;
import angerona.fw.gui.view.ConfidentialView;
import angerona.fw.gui.view.ReportView;
import angerona.fw.gui.view.ResourcenView;
import angerona.fw.internal.Entity;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.listener.ErrorListener;
import angerona.fw.listener.PluginListener;

import com.whiplash.gui.WlComponent;
import com.whiplash.gui.WlWindow;
import com.whiplash.gui.WlWindowSet;
import com.whiplash.res.DefaultResourceManager;
import com.whiplash.res.WlResourceManager;

/**
 * The main window of the Angerona UI - Extension. It is a Singleton. 
 * @author Tim Janus
 */
public class AngeronaWindow implements PluginListener, ErrorListener {
	/** the main window of the angerona gui-extension */
	private WlWindow window;
	
	/** the window set containing the window */
	private WlWindowSet windowSet;

	/** a bar allowing the loading, running and initalization of simulations */
	private SimulationControlBar simLoadBar;
	
	/** map containing registered views some of them are default other might be provided by plugins */
	private Map<String, Class<? extends BaseView>> viewMap = new HashMap<String, Class<? extends BaseView>>();
	
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaWindow.class);
	
	/** unique instance of the AngeronaWindow (Singleton) */
	private static AngeronaWindow instance;
	
	/** @return reference to the unique instance of the AngeronaWindow */
	public static AngeronaWindow getInstance() {
		if(instance == null) {
			instance = new AngeronaWindow();
		}
		return instance;
	}
	
	private AngeronaWindow() {
		DefaultResourceManager resourceManager = null;
		try {
			resourceManager = new DefaultResourceManager(new File("./resources").toURI().toURL());
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
				onCreateWindowClicked();
			}
		});
		
		menuWindow.add(miCreate);
		menuBar.add(menuWindow);
		
		window.pack();
	}

	public void init() throws ParserConfigurationException, SAXException,
			IOException {
		LOG.trace("init() AngeronaWindow");
		window.setTitle("Angerona - Simulation Monitor");
		window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		PluginInstantiator.getInstance().addListener(this);
		
		Angerona angerona = Angerona.getInstance();
		angerona.addAgentConfigFolder("config/agents");
		angerona.addBeliefbaseConfigFolder("config/beliefbases");
		angerona.addSimulationFolders("examples");
		angerona.bootstrap();
		
		angerona.addErrorListener(this);
		
		// TODO: Implement internal plugin
		viewMap.put("Report-View", ReportView.class);
		viewMap.put("Resourcen-View", ResourcenView.class);
		viewMap.put("Confidential-Knowledge", ConfidentialView.class);
		
		window.addWlComponent(createBaseView(ReportView.class, null), BorderLayout.CENTER);
		window.addWlComponent(createBaseView(ResourcenView.class, null), BorderLayout.WEST);
		window.addWlComponent(simLoadBar = createBaseView(SimulationControlBar.class, null), BorderLayout.SOUTH);
	}
	
	/**
	 * Adds the given component as tab to the center area of the window
	 * @param component an wlwindow component (BaseView)
	 */
	public void addComponentToCenter(WlComponent component) {
		window.addWlComponent(component, BorderLayout.CENTER);
	}
	
	/**
	 * loads the simulation at the specified path
	 * @param path	path to the simulation which is loaded.
	 */
	public void loadSimulation(String path) {
		LOG.trace("Load simulation {}", path);
		File f = new File(path);
		if(f.exists()) {
			simLoadBar.loadSimulation(f);
		}
	}
	
	/** @return the main WlFramework Window */
	public WlWindow getWindow() {
		return window;
	}
	
	/**
	 * Creates (but not add) a view for the given AgentComponent. 
	 * @param comp	Reference to the component which should be showed in the new view.
	 * @return	reference to the created view. null if no view for the AgentComponent is
	 * 			registered or an error occured.
	 */
	public BaseView createViewForAgentComponent(AgentComponent comp) {
		for (Class<? extends BaseView> cls : viewMap.values()) {
			BaseView view = null;
			try {
				view = cls.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(view == null)
				return null;
			
			if (comp.getClass().equals(view.getObservationObjectType())) {
				BaseView newly = AngeronaWindow.createBaseView(cls, comp);
				return newly;
			}
		}
		
		LOG.warn("Cannot find UI-View for Agent-Component '{}' of agent '{}'", 
				comp, comp.getAgent().getName());
		return null;
	}
	
	/**
	 * creates and initialized an UI View.
	 * @param cls class information about the UI component which should be created.
	 * @param toObserve	reference to the object the UI component should observe (might be null if no direct mapping between observed object and UI component can be given)
	 * @return a new instance of UIComponent which is ready to use.
	 */
	public static <T extends BaseView> T createBaseView(Class<? extends T> cls, Object toObserve) {
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
	
	/** helper method: called if the 'Create Window...' menu item is clicked */
	private void onCreateWindowClicked() {
		String str = (String) JOptionPane.showInputDialog(window, 
				"Select a Window to create...",
				"Create Window",
				JOptionPane.PLAIN_MESSAGE,
				null,
				viewMap.keySet().toArray(),
				null);
		if(viewMap.containsKey(str)) {
			try {
				BaseView bc = viewMap.get(str).newInstance();
				Class<?> type = bc.getObservationObjectType();
				if(type == null) {
					bc.init();
					window.addWlComponent(bc, BorderLayout.CENTER);
				} else {
					AngeronaEnvironment env = simLoadBar.getEnvironment();
					List<Entity> tempList = new LinkedList<Entity>();
					for(Long id : env.getEntityMap().keySet()) {
						Entity possible = env.getEntityMap().get(id);
						if(possible.getClass().equals(bc.getObservationObjectType())) {
							tempList.add(possible);
						}
					}
					
					List<String> names = new LinkedList<String>();
					for(Entity att : tempList) {
						names.add("<" + att.getGUID() + ">");
					}
					
					Entity selection = null;
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
						BaseView comp = AngeronaWindow.createBaseView(viewMap.get(str), selection);
						AngeronaWindow.getInstance().addComponentToCenter(comp);
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

	@Override
	public void loadingImplementations(PluginInstantiator pi) {
		LOG.info("Load UI-Plugins");
		PluginManagerUtil pmu = PluginInstantiator.getInstance().getPluginUtil();
		Collection<UIPlugin> uiPlugins = new LinkedList<UIPlugin>(pmu.getPlugins(UIPlugin.class));
		for(UIPlugin pl : uiPlugins) {
			LOG.info("UI-Plugin: '{}' loaded", pl.getClass().getName());
			viewMap.putAll(pl.getUIComponents());
		}
	}

	@Override
	public void onError(String errorTitle, String errorMessage) {
		JOptionPane.showMessageDialog(this.getWindow(), errorMessage, 
				errorTitle, JOptionPane.ERROR_MESSAGE);
	}
}
