package angerona.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.TreeController;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationMonitor extends JFrame {

	/** kill warning */
	private static final long serialVersionUID = 714099953028717849L;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	private SimulationConfiguration actConfig;
	
	private JTextField txtSimStatus;
	
	private JButton btnRun;
	
	private String simulationDirectory;
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		new SimulationMonitor();
	}

	public SimulationMonitor() throws ParserConfigurationException, SAXException, IOException {
		setTitle("Angerona - Simulation Monitor");
		setBounds(0, 0, 500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		Angerona angerona = new Angerona();
		angerona.addAgentConfigFolder("config/agents");
		angerona.addBeliefbaseConfigFolder("config/beliefbases");
		angerona.addSimulationFolders("config/examples");
		angerona.bootstrap();
		
		JTree tree = new JTree();
		DefaultMutableTreeNode ar = new DefaultMutableTreeNode("Angerona Resourcen");
		new TreeController(ar, angerona);
		tree.setModel(new DefaultTreeModel(ar));
		expandAll(tree, true);
		this.add(tree, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		
		txtSimStatus = new JTextField();
		txtSimStatus.setMinimumSize(new Dimension(200, 30));
		bottom.add(txtSimStatus);
		
		JButton btnLoad = new JButton();
		btnLoad.setText("Load Simulation");
		btnLoad.setMinimumSize(new Dimension(100, 30));
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onLoadClicked();
			}
		});
		bottom.add(btnLoad);
		
		btnRun = new JButton("Run");
		btnRun.setText("Run");
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				environment.cleanupSimulation();
				environment.initSimulation(actConfig, simulationDirectory);
				environment.runTillNoMorePerceptionsLeft();
			}
		});
		bottom.add(btnRun);
		
		this.add(bottom, BorderLayout.SOUTH);
		updateConfigView();
		pack();
	}
	
	private void onLoadClicked() {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setCurrentDirectory(new File("."));
		int reval = fileDialog.showOpenDialog(this);
		if(reval == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			try {
				actConfig = environment.loadSimulation(file.getAbsolutePath(), false);
				simulationDirectory = file.getParent();
				updateConfigView();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateConfigView() {
		if(actConfig == null) {
			btnRun.setEnabled(false);
			txtSimStatus.setText("No Simulation loaded.");
		} else {
			btnRun.setEnabled(true);
			if(environment.isRunning()) {
				txtSimStatus.setText("Simulation '" + actConfig.getName() + "' running.");
			} else {
				txtSimStatus.setText("Simulation '" + actConfig.getName() + "' ready.");
			}
		}
	}
	
	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	public void expandAll(JTree tree, boolean expand) {
	    TreeNode root = (TreeNode)tree.getModel().getRoot();

	    // Traverse tree from root
	    expandAll(tree, new TreePath(root), expand);
	}
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
	    // Traverse children
	    TreeNode node = (TreeNode)parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration e=node.children(); e.hasMoreElements(); ) {
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
