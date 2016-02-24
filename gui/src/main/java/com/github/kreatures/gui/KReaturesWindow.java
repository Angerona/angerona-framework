package com.github.kreatures.gui;

import interactive.InteractiveAgentNextActionRequester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import bibliothek.extension.gui.dock.theme.SmoothTheme;
import bibliothek.gui.DockController;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.StackDockStation;
import bibliothek.gui.dock.station.split.SplitDockProperty;
import bibliothek.gui.dock.station.stack.tab.layouting.TabPlacement;
import bibliothek.gui.dock.themes.NoStackTheme;
import bibliothek.gui.dock.util.IconManager;
import bibliothek.gui.dock.util.Priority;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.KReatures;
import com.github.kreatures.core.KReaturesEnvironment;
import com.github.kreatures.core.InteractiveAgent;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.gui.controller.SimulationTreeController;
import com.github.kreatures.gui.project.ProjectTreeMVPComponent;
import com.github.kreatures.gui.report.ReportTreeMVP;
import com.github.kreatures.gui.simctrl.SimulationControlBar;
import com.github.kreatures.gui.simctrl.SimulationControlBarMVPComponent;
import com.github.kreatures.gui.simctrl.SimulationControlMenu;
import com.github.kreatures.gui.simctrl.SimulationControlPresenter;
import com.github.kreatures.gui.view.ReportView;
import com.github.kreatures.gui.view.ResourcenView;
import com.github.kreatures.core.internal.PluginInstantiator;
import com.github.kreatures.core.internal.UIPluginInstatiator;
import com.github.kreatures.core.internal.ViewComponentFactory;
import com.github.kreatures.core.listener.FrameworkListener;
import com.github.kreatures.core.listener.SimulationListener;
import com.github.kreatures.core.serialize.CreateKReaturesXMLFileDefault;

/**
 * The main window of the KReatures UI - Extension. It is a Singleton. 
 * @author Tim Janus
 */
public class KReaturesWindow extends WindowAdapter
	implements  
	SimulationListener,
	FrameworkListener
	{

	/** the root window of the application */
	private JFrame mainWindow;
	
	/** text are used to show error messages */
	private JTextArea txtErrorMsg;
	
	/** scroll pane that embeds the text are to show error messages */
	private JScrollPane errorMsgScroll;
	
	private DockController control;
	
	private StackDockStation mainStack;
	
	private SplitDockStation parentStation;
	
	private ReportView reportView;
	
	private List<Dockable> resMap = new LinkedList<>();
	
	/** a bar allowing the loading, running and initalization of simulations */
	private SimulationControlBar simLoadBar;
	
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(KReaturesWindow.class);
	
	/** unique instance of the KReaturesWindow (Singleton) */
	private static KReaturesWindow instance;
	
	/** @return reference to the unique instance of the KReaturesWindow */
	public static KReaturesWindow get() {
		if(instance == null) {
			instance = new KReaturesWindow();
		}
		return instance;
	}
	
	/** 
	 * Private Ctor: Singleton pattern.
	 */
	private KReaturesWindow() {}

	public void init() throws ParserConfigurationException, SAXException,
			IOException {
		LOG.trace("init() KReaturesWindow");
		
		// setup main window:
		mainWindow = new JFrame();
		mainWindow.addWindowListener(this);
		mainWindow.setTitle("KReatures");
		mainWindow.setBounds(100, 100, 400, 300);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setExtendedState(mainWindow.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		mainWindow.setIconImage(readImage("/kreatures/gui/icons/font.png"));
		
		// create the text area and the scroll pane for the error message dialog:
		txtErrorMsg = new JTextArea();
		txtErrorMsg.setLineWrap(true);
		txtErrorMsg.setWrapStyleWord(true);
		txtErrorMsg.setRows(5);
		txtErrorMsg.setColumns(40);
		errorMsgScroll = new JScrollPane(txtErrorMsg);
		
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
		
		JMenu simulationMenu = new JMenu("Simulation");
		SimulationControlMenu view = new SimulationControlMenu(simulationMenu);
		new SimulationControlPresenter(KReaturesGUIDataStorage.get().getSimulationControl(), view);
		menuBar.add(simulationMenu);
		
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

	private void initKReaturesFramework() throws ParserConfigurationException,
			SAXException, IOException {
		PluginInstantiator pi = PluginInstantiator.getInstance();
		pi.addListener(UIPluginInstatiator.getInstance());
		
		KReatures kreatures = KReatures.getInstance();
		kreatures.addFrameworkListener(this);
		kreatures.addSimulationListener(this);

		kreatures.bootstrap();
		pi.registerPlugin(new DefaultUIPlugin());
		/**
		 * @author donfack
		 * Here we have to create all abstract swarm config file 
		 * in order that there can be laoded after into KReatures.
		 */
		try {
			new CreateKReaturesXMLFileDefault();
			LOG.info("Abstract_Swarm configuration-file are successfull created.");
		} catch (Exception e) {
			LOG.error("There are some error when Abstract_Swarm configuration-file would be created.");
			e.printStackTrace();
		}
		
		kreatures.getProject().addDirectory(new File("config/agents"));
		kreatures.getProject().addDirectory(new File("config/beliefbases"));
		kreatures.getProject().addDirectory(new File("examples"));
	}
	
	/**
	 * @todo move somewhere else
	 * @param view
	 * @param title
	 * @return
	 */
	public Dockable openView(ViewComponent view) {
		DefaultDockable dd = new DefaultDockable(view.getPanel());
		view.decorate(dd);
		
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
	
	/** helper method: called if the 'Create Window...' menu item is clicked */
	private void onCreateWindowClicked() {
		UIPluginInstatiator uip = UIPluginInstatiator.getInstance();
		Map<String, Class<? extends ViewComponent>> viewMap = uip.getViewMap();
		String str = (String) JOptionPane.showInputDialog(null, 
				"Select a Window to create...",
				"Create Window",
				JOptionPane.PLAIN_MESSAGE,
				null,
				viewMap.keySet().toArray(),
				null);
		if(viewMap.containsKey(str)) {
			try {
				ViewComponent bc = viewMap.get(str).newInstance();
				openView(bc);
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Shows an error message box if its receives error messages from the KReatures Framework.
	 */
	@Override
	public void onError(String errorTitle, String errorMessage) {
		txtErrorMsg.setText(errorMessage);
		txtErrorMsg.setCaretPosition(0);
		JOptionPane.showMessageDialog(mainWindow, errorMsgScroll, 
				errorTitle, JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void onBootstrapDone() {	
	}

	/**
	 * The content for the docking frame framework is generated in the
	 * windowOpened method, this is called if the KReatures main window
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
		
		String iconFolder = "/com/github/kreatures/gui/icons/";
		registerIcon("ico_main", iconFolder + "font.png");
		registerIcon("close", iconFolder + "cross.png");
		registerIcon("report", iconFolder + "table.png");
		registerIcon("resources", iconFolder + "drive_link.png");
		registerIcon("monitor", iconFolder + "monitor.png");
		registerIcon("simulation", iconFolder + "world.png");
		registerIcon("report", iconFolder + "report.png");
		registerIcon("report_attachment", iconFolder + "report_disk.png");
		registerIcon("page_white", iconFolder + "page_white.png");
		registerIcon("agent", iconFolder + "user.png");
		
		// init KReatures
		try {
			initKReaturesFramework();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			onError("KReatures Initialization Failure", e.getMessage());
			e.printStackTrace();
		}
		
		
		reportView = new ReportView();
		
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

	public IconManager getIcons() {
		return control.getIcons();
	}
	
	private BufferedImage readImage(String resUrl) {
		InputStream is = KReaturesWindow.class.getResourceAsStream(resUrl);
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
	
	private void createDefaultPerspective() {
		parentStation.removeAllDockables();
		
		ViewComponent viewComp = ViewComponentFactory.get().createViewComponent(ProjectTreeMVPComponent.class);
		DefaultDockable dd = new DefaultDockable(viewComp.getPanel());
		viewComp.decorate(dd);
		parentStation.drop(dd, new SplitDockProperty(0, 0, 0.25, 0.9));
		
		dd = new DefaultDockable(reportView);
		reportView.decorate(dd);
		parentStation.drop(dd, new SplitDockProperty(0.25, 0, 0.75, 0.9));
		
		ReportTreeMVP reportTree = ViewComponentFactory.get().createViewComponent(ReportTreeMVP.class);
		dd = new DefaultDockable(reportTree.getPanel());
		reportTree.decorate(dd);
		parentStation.drop(dd, new SplitDockProperty(0.25, 0, 0.75, 0.9));
		
		viewComp = ViewComponentFactory.get().createViewComponent(SimulationControlBarMVPComponent.class);
		dd = new DefaultDockable(viewComp.getPanel());
		/*
		dd.setActionOffers(null);
		HierarchyDockActionSource hdas = (HierarchyDockActionSource)dd.getGlobalActionOffers();
		hdas.unbind();
		*/
		parentStation.drop(dd, new SplitDockProperty(0, 0.9, 1, 0.1));		
	}
	
	@Override
	public void simulationStarted(KReaturesEnvironment simulationEnvironment) {
		SimulationTreeController stc = new SimulationTreeController(new JTree());
		stc.simulationStarted(simulationEnvironment);
		
		ResourcenView rv = new ResourcenView(stc);
		DefaultDockable dd = new DefaultDockable(new JScrollPane(rv));
		dd.setTitleText("Entities of '" + simulationEnvironment.getName() + "'");
		dd.setTitleIcon(control.getIcons().get("simulation"));
		parentStation.drop(dd, SplitDockProperty.WEST);
		resMap.add(dd);
		
		//check if the scenario has an interactive agent and open the interactive View if needed
		for(Agent a : simulationEnvironment.getAgents()){
			if (a instanceof InteractiveAgent){
				((InteractiveAgent) a).setNextActionRequester(new InteractiveAgentNextActionRequester(simulationEnvironment));
			}
		}
	}

	@Override
	public void simulationDestroyed(KReaturesEnvironment simulationEnvironment) {
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
	public void agentAdded(final KReaturesEnvironment simulationEnvironment,
			Agent added) {
	}

	@Override
	public void agentRemoved(KReaturesEnvironment simulationEnvironment,
			Agent removed) {
	}

	@Override
	public void tickDone(KReaturesEnvironment simulationEnvironment) {
	}

	@Override
	public void actionPerformed(Agent agent, Action act) {
	}
	
	public JFrame getMainWindow(){
		return mainWindow;
	}

	@Override
	public void tickStarting(KReaturesEnvironment simulationEnvironment) {
		// TODO Auto-generated method stub
		
	}
}
