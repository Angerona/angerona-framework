package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
		window.setBounds(100, 100, 640, 480);
		window.setVisible(true);
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(new JMenuItem("Exit"));
		menuBar.add(menuFile);
		
		window.pack();
	}

	public void init() throws ParserConfigurationException, SAXException,
			IOException {
		window.setTitle("Angerona - Simulation Monitor");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setBounds(0, 0, dim.width, dim.height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Angerona angerona = Angerona.getInstance();
		angerona.addAgentConfigFolder("config/agents");
		angerona.addBeliefbaseConfigFolder("config/beliefbases");
		angerona.addSimulationFolders("config/examples");
		angerona.bootstrap();
		
		ReportView rv = new ReportView();
		ResourcenView resv = new ResourcenView();
		SimulationLoadBar siml = new SimulationLoadBar();
		
		window.addWlComponent(rv, BorderLayout.CENTER);
		window.addWlComponent(resv , BorderLayout.WEST);
		window.addWlComponent(siml, BorderLayout.SOUTH);
		
		System.out.println("Report: " + rv.getMinimumSize());
		System.out.println("Resourcen: " + resv.getMinimumSize());
		System.out.println("Simulation: " + siml.getMinimumSize());
	}
	
	public void addComponentToCenter(WlComponent component) {
		window.addWlComponent(component, BorderLayout.CENTER);
	}
	
	
	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public static void expandAll(JTree tree, boolean expand) {
	    TreeNode root = (TreeNode)tree.getModel().getRoot();

	    // Traverse tree from root
	    expandAll(tree, new TreePath(root), expand);
	}
	private static void expandAll(JTree tree, TreePath parent, boolean expand) {
	    // Traverse children
	    TreeNode node = (TreeNode)parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration<TreeNode> e=node.children(); e.hasMoreElements(); ) {
	            TreeNode n = (TreeNode)e.nextElement();
	            TreePath path = parent.pathByAddingChild(n);
	            expandAll(tree, path, expand);
	        }
	    }

	    // Expansion or collapse must be done bottom-up
	    if (expand) {
	        tree.expandPath(parent);
	    } else {
	        tree.collapsePath(parent);
	    }
	}
}
