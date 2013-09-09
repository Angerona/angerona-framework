package com.github.angerona.fw.aspgraph;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import angerona.fw.aspgraph.controller.MasterController;
import angerona.fw.aspgraph.view.GraphView;

import com.whiplash.res.DefaultResourceManager;
import com.whiplash.res.WlResourceManager;

/**
 * Main frame
 *
 */
public class App extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7767787946236323775L;
	
	private String path2clingo;
	private JMenuItem openFile;
	private JMenuItem setPath;
	private GraphView graphView;

	/**
	 * Creates a new App
	 * @param path2clingo Path to the file clingo.exe
	 * @param filename Filename of the file that contains the logic program
	 */
	public App() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		openFile = new JMenuItem("Open File");
		openFile.addActionListener(this);
		setPath = new JMenuItem("Set path to Clingo");
		setPath.addActionListener(this);
		menu.add(openFile);		
		menu.add(setPath);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		path2clingo = openSettings();
		/* No path to clingo set */
		if (path2clingo.equals("")){
			JOptionPane.showMessageDialog(this, "Please set path to clingo.", "No path to clingo set", JOptionPane.INFORMATION_MESSAGE);
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            path2clingo = file.getPath();
	            
	            /* Write settings file where path is saved */
	            try {
					FileOutputStream outputStream = new FileOutputStream("settings.txt");
					for (int i=0; i<path2clingo.length(); i++){
						outputStream.write((byte) path2clingo.charAt(i));
					}
					outputStream.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
		
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		graphView = MasterController.instance().getGraphView();
		this.add(graphView, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == openFile){
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            if (path2clingo != null) {
	            	this.remove(graphView);
	            	MasterController.execute(path2clingo, file.getPath());
	            	graphView = MasterController.instance().getGraphView();
	            	this.add(graphView);
	            	revalidate();
	            }
	            else JOptionPane.showMessageDialog(this, "No path to Clingo set. Please select Menu->Set path to Clingo.", "No path to Clingo", JOptionPane.ERROR_MESSAGE);
	        }
		} else if (e.getSource() == setPath){
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            path2clingo = file.getPath();
	            
	            /* Write settings file where path is saved */
	            try {
					FileOutputStream outputStream = new FileOutputStream("settings.txt");
					for (int i=0; i<path2clingo.length(); i++){
						outputStream.write((byte) path2clingo.charAt(i));
					}
					outputStream.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        }
		}
	}
	
    public static void main( String[] args )
    {
    	DefaultResourceManager resourceManager = null;
		try {
			resourceManager = new DefaultResourceManager(new File("./resources").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WlResourceManager.setDefaultResourceManager(resourceManager);
			App frame = new App();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(900, 550);
			frame.setVisible(true);
    }
    
    private String openSettings(){
    	byte b;
        String text = "";
        FileInputStream inputStream;
        File f = new File("settings.txt");
        if (f.exists()){
        try{
			inputStream = new FileInputStream(f);
        do{
          b = (byte) inputStream.read();
          text += (char) b;
        } while (b !=-1);
        inputStream.close();
        return text.substring(0, text.length()-1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
        return "";
    }
}
