package angerona.fw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.xml.parsers.ParserConfigurationException;

import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.controller.ResourceTreeController;
import angerona.fw.gui.controller.SimulationTreeController;
import angerona.fw.gui.view.ReportView;
import angerona.fw.gui.view.ResourcenView;
import angerona.fw.gui.view.View;
import angerona.fw.internal.Entity;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.listener.FrameworkListener;
import angerona.fw.listener.PluginListener;
import angerona.fw.listener.SimulationListener;
import bibliothek.extension.gui.dock.theme.SmoothTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.HierarchyDockActionSource;
import bibliothek.gui.dock.action.LocationHint;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;
import bibliothek.gui.dock.station.split.SplitDockProperty;
import bibliothek.gui.dock.station.stack.tab.layouting.TabPlacement;
import bibliothek.gui.dock.themes.NoStackTheme;
import bibliothek.gui.dock.util.Priority;

/**
 * The main window of the Angerona UI - Extension. It is a Singleton. 
 * @author Tim Janus
 */
public class AngeronaWindow extends WindowAdapter
	implements 
	PluginListener, 
	SimulationListener,
	FrameworkListener
	{

	/** the root window of the application */
	private JFrame mainWindow;
	
	private DockController control;
	
	private StackDockStation mainStack;
	
	private SplitDockStation parentStation;
	
	private ResourcenView resourceView;
	
	private ReportView reportView;
	
	private List<Dockable> resMap = new LinkedList<>();
	
	/** a bar allowing the loading, running and initalization of simulations */
	private SimulationControlBar simLoadBar;
	
	/** map containing registered views some of them are default other might be provided by plugins */
	private Map<String, Class<? extends View>> viewMap = new HashMap<String, Class<? extends View>>();
	
	/** map containing an Entity as key mapping to all the views showing the entity */
	private Map<Entity, List<View>> registeredViewsByEntity = new HashMap<Entity, List<View>>();
	
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
	
	/** 
	 * Private Ctor: Singleton pattern.
	 */
	private AngeronaWindow() {}

	public void init() throws ParserConfigurationException, SAXException,
			IOException {
		LOG.trace("init() AngeronaWindow");
		
		// setup main window:
		mainWindow = new JFrame();
		mainWindow.addWindowListener(this);
		mainWindow.setTitle("Angerona");
		mainWindow.setBounds(100, 100, 400, 300);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setExtendedState(mainWindow.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		mainWindow.setIconImage(readImage("/angerona/fw/gui/icons/font.png"));
		
		resourceView = new ResourcenView();
		resourceView.setController(new ResourceTreeController(new JTree()));
		resourceView.init();
		
		reportView = new ReportView();
		reportView.init();
		
		// create the menu.
		createMenu();
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainWindow.dispose();
			}
		});
		menuFile.add(exit);
		menuBar.add(menuFile);
		
		JMenu menuWindow = new JMenu("Windows");
		JMenuItem miCreate = new JMenuItem("Create...");
		miCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				onCreateWindowClicked();
			}
		});
		
		JMenuItem miReset = new JMenuItem("Reset to default");
		miReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createDefaultPerspective();
			}
		});
		
		menuWindow.add(miCreate);
		menuWindow.add(miReset);
		menuBar.add(menuWindow);
		mainWindow.setJMenuBar(menuBar);
	}

	private void initAngeronaFramework() throws ParserConfigurationException,
			SAXException, IOException {
		PluginInstantiator.getInstance().addListener(this);
		Angerona angerona = Angerona.getInstance();
		angerona.addFrameworkListener(this);
		angerona.addSimulationListener(this);
		
		angerona.addAgentConfigFolder("config/agents");
		angerona.addBeliefbaseConfigFolder("config/beliefbases");
		angerona.addSimulationFolders("examples");
		angerona.bootstrap();
	}
	
	public Dockable openView(View view, String title) {
		DefaultDockable dd = new DefaultDockable((JPanel)view);
		dd.setTitleText(title);
		view.init();
		createCloseButton(dd);
		// easy if the center is a stack already, only adding the Dockable to the stack.
		if(mainStack.getController() != null) {
			mainStack.drop(dd);
		} else {
			// otherwise find the control with the most are and make it as new main stack.
			double maxArea = -1;
			SplitDockProperty curProp = null;
			Dockable centerDock = null;
			
			for(int i=0; i<parentStation.getDockableCount(); ++i) {
				Dockable cur = parentStation.getDockable(i);
				SplitDockProperty sdp = parentStation.getDockableLocationProperty(cur);
				double area = sdp.getWidth() * sdp.getHeight();
				if(area > maxArea) {
					maxArea = area;
					curProp = sdp;
					centerDock = cur;
				}
			}
			
			// remove the old dockable, drop it to the stack, drop the new dockable to the stack
			// and then put the stack on the old location of the main dockable.
			parentStation.removeDockable(centerDock);
			mainStack.drop(centerDock);
			mainStack.drop(dd);
			parentStation.drop(mainStack, curProp);
		}
		return dd;
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
	
	/**
	 * Creates (but not add) a view for the given AgentComponent. 
	 * @param comp	Reference to the component which should be showed in the new view.
	 * @return	reference to the created view. null if no view for the AgentComponent is
	 * 			registered or an error occured.
	 */
	public View createViewForEntityComponent(Entity comp) {
		for (Class<? extends View> cls : viewMap.values()) {
			View view = null;
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
			
			if (comp.getClass().equals(view.getObservedType())) {
				View newly = createEntityView(cls, comp);
				return newly;
			}
		}
		
		LOG.warn("Cannot find UI-View for '{}'", 
				comp.getClass().getName());
		return null;
	}
	
	/**
	 * creates and initialized an UI View.
	 * @param cls class information about the UI component which should be created.
	 * @param toObserve	reference to the object the UI component should observe (might be null if no direct mapping between observed object and UI component can be given)
	 * @return a new instance of UIComponent which is ready to use.
	 */
	public <E extends Entity, T extends View> T createEntityView(Class<? extends T> cls, E toObserve) {
		T reval;
		try {
			reval = cls.newInstance();
			if(toObserve != null) {
				reval.setObservedEntity(toObserve);
				if(!registeredViewsByEntity.containsKey(toObserve)) {
					registeredViewsByEntity.put(toObserve, new LinkedList<View>());
				}
				registeredViewsByEntity.get(toObserve).add(reval);
			}
			reval.init();
			return reval;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends View> T getBaseViewObservingEntity(Entity ent) {
		if(registeredViewsByEntity.containsKey(ent)) {
			return (T) registeredViewsByEntity.get(ent).get(0);
		}
		return null;
	}
	
	/** helper method: called if the 'Create Window...' menu item is clicked */
	private void onCreateWindowClicked() {
		String str = (String) JOptionPane.showInputDialog(null, 
				"Select a Window to create...",
				"Create Window",
				JOptionPane.PLAIN_MESSAGE,
				null,
				viewMap.keySet().toArray(),
				null);
		if(viewMap.containsKey(str)) {
			try {
				View bc = viewMap.get(str).newInstance();
				Class<?> type = bc.getObservedType();
				if(type == null) {
					bc.init();
					//window.addWlComponent(bc, BorderLayout.CENTER);
				} else {
					AngeronaEnvironment env = simLoadBar.getEnvironment();
					List<Entity> tempList = new LinkedList<Entity>();
					for(Long id : env.getEntityMap().keySet()) {
						Entity possible = env.getEntityMap().get(id);
						if(possible.getClass().equals(bc.getObservedType())) {
							tempList.add(possible);
						}
					}
					
					List<String> names = new LinkedList<String>();
					for(Entity att : tempList) {
						names.add("<" + att.getGUID() + ">");
					}
					
					Entity selection = null;
					if(tempList.size() == 0) {
						JOptionPane.showMessageDialog(null, "No object for observation found.");
					} else if(tempList.size() == 1) {
						selection = tempList.get(0);
					} else {
						String str2 = (String) JOptionPane.showInputDialog(null, 
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
						View comp = createEntityView(viewMap.get(str), selection);
						//AngeronaWindow.getInstance().addComponentToCenter(comp);
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
		try {
			uiPlugins.add(DefaultUIPlugin.class.newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(UIPlugin pl : uiPlugins) {
			LOG.info("UI-Plugin: '{}' loaded", pl.getClass().getName());
			viewMap.putAll(pl.getUIComponents());
		}
	}

	/**
	 * Shows an error message box if its receives error messages from the Angerona Framework.
	 */
	@Override
	public void onError(String errorTitle, String errorMessage) {
		JOptionPane.showMessageDialog(null, errorMessage, 
				errorTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void onBootstrapDone() {	
	}

	/**
	 * The content for the docking frame framework is generated in the
	 * windowOpened method, this is called if the Angerona main window
	 * was initialized and setVisible the first time. It is important that
	 * every change made in the docking frames framework occurs in the event
	 * handling thread of swing.
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {
		// init docking frames
		control = new DockController();
		control.setRootWindow(mainWindow);
		control.setTheme(new NoStackTheme(new SmoothTheme()));
		
		parentStation = new SplitDockStation();
		parentStation.setExpandOnDoubleclick(false);
		mainStack = new StackDockStation();
		mainStack.setTabPlacement(TabPlacement.TOP_OF_DOCKABLE);
		
		control.add(parentStation);
		mainWindow.add(parentStation);
		parentStation.setVisible(true);
		
		registerIcon("ico_main", "angerona/fw/gui/icons/font.png");
		registerIcon("close", "/angerona/fw/gui/icons/cross.png");
		registerIcon("report", "/angerona/fw/gui/icons/table.png");
		registerIcon("resources", "/angerona/fw/gui/icons/drive_link.png");
		registerIcon("monitor", "/angerona/fw/gui/icons/monitor.png");
		registerIcon("simulation", "/angerona/fw/gui/icons/world.png");
		
		
		// init Angerona
		try {
			initAngeronaFramework();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			onError("Angerona Initialization Failure", e.getMessage());
			e.printStackTrace();
		}
		
		createDefaultPerspective();
	}

	private boolean registerIcon(String id, String resUrl) {
		BufferedImage image = readImage(resUrl);
		if(image != null) {
			ImageIcon icon = new ImageIcon(image);
			control.getIcons().setIcon(id, Priority.CLIENT, icon);
			return true;
		}
		return false;
	}

	private BufferedImage readImage(String resUrl) {
		InputStream is = AngeronaWindow.class.getResourceAsStream(resUrl);
		if(is == null) {
			LOG.warn("Cannot found resource '{}' in JAR", resUrl);
			return null;
		}
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			LOG.error("Cannot load '{}' from JAR: '{}'", resUrl, e.getMessage());
			e.printStackTrace();
		}
		return image;
	}

	private void createCloseButton(final DefaultDockable dd) {
		DefaultDockActionSource actionSource = new DefaultDockActionSource(
				new LocationHint(LocationHint.DOCKABLE, LocationHint.RIGHT_OF_ALL));
		SimpleButtonAction sba = new SimpleButtonAction();
		sba.setText("Close");
		sba.setIcon(control.getIcons().get("close"));
		sba.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dd.getDockParent().drag(dd);
			}
		});
		actionSource.add(sba);
		dd.setActionOffers(actionSource);
		
	}
	
	private void createDefaultPerspective() {
		parentStation.removeAllDockables();
		
		DefaultDockable dd = new DefaultDockable(resourceView);
		dd.setTitleIcon(control.getIcons().get("resources"));
		dd.setTitleText("Angerona Resources");
		parentStation.drop(dd, new SplitDockProperty(0, 0, 0.25, 0.9));
		
		dd = new DefaultDockable(reportView);
		dd.setTitleIcon(control.getIcons().get("report"));
		dd.setTitleText("Report");
		parentStation.drop(dd, new SplitDockProperty(0.25, 0, 0.75, 0.9));
		
		simLoadBar = new SimulationControlBar();
		simLoadBar.init();
		dd = new DefaultDockable(simLoadBar);
		dd.setTitleText("Simulation Control Bar");
		dd.setTitleIcon(control.getIcons().get("monitor"));
		dd.setActionOffers(null);
		HierarchyDockActionSource hdas = (HierarchyDockActionSource)dd.getGlobalActionOffers();
		hdas.unbind();
		parentStation.drop(dd, new SplitDockProperty(0, 0.9, 1, 0.1));		
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulationEnvironment) {
		SimulationTreeController stc = new SimulationTreeController(new JTree());
		stc.simulationStarted(simulationEnvironment);
		ResourcenView rv = new ResourcenView(stc);
		rv.init();
		DefaultDockable dd = new DefaultDockable(new JScrollPane(rv));
		dd.setTitleText("Entities of '" + simulationEnvironment.getName() + "'");
		dd.setTitleIcon(control.getIcons().get("simulation"));
		parentStation.drop(dd, SplitDockProperty.WEST);
		
		resMap.add(dd);
	}

	@Override
	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {
		for(Dockable d : resMap) {
			DockStation st = d.getDockParent();
			if(st != null) {
				st.drag(d);
			}
		}
		resMap.clear();
	}
	
	public void registerDockableForCurrentSimulation(Dockable d) {
		resMap.add(d);
	}

	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {
	}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {
	}

	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment,
			boolean finished) {
	}

	@Override
	public void actionPerformed(Agent agent, Action act) {
	}
}
