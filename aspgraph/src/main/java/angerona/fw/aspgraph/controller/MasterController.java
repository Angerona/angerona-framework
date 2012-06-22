package angerona.fw.aspgraph.controller;

import javax.swing.JOptionPane;

import net.sf.tweety.logicprogramming.asplibrary.solver.Clingo;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

import angerona.fw.aspgraph.exceptions.NotValidProgramException;
import angerona.fw.aspgraph.view.EDGView;
import angerona.fw.aspgraph.view.EGView;
import angerona.fw.aspgraph.view.GraphView;

/**
 * MasterController who controls construction and visualization of graphs 
 * @author ella
 *
 */
public class MasterController {

	/**
	 * Instance of MasterController
	 */
	private static MasterController instance;
	
	/**
	 * Logic Program to which graphs are created
	 */
	private static Program p;
	
	private static GraphView graphView;
	
	private MasterController(){	graphView = new GraphView();}
	
	/**
	 * Return instance of MasterController
	 * @return Instance of MasterController
	 */
	public static MasterController instance(){
		if (instance == null) instance = new MasterController();
		return instance;
	}
	
	/**
	 * Starts construction and visualization of graphs
	 * @param path2clingo Path to file clingo.exe
	 * @param filename Name of file that defines the logic program
	 */
	public static void execute(String path2clingo, String filename){
		/* Load logic program from file */
		p = Program.loadFrom(filename);
		
		/* Instantiate controllers and views */
		EDGController edgContr = EDGController.instance();
		EGController egContr = EGController.instance();
		graphView = new GraphView();		
		EDGView edgView = graphView.getEDGPanel();
		EGView egView = graphView.getEGPanel();
		graphView.setProgramToPanel(p);
		
		/* Calculate answer sets */
		Solver solver = new Clingo(path2clingo);
		AnswerSetList list;
		try {
			list = solver.computeModels(p, 0);
			
			/* Instantiate EDGView */
			edgContr.createEDG(p,list);
			edgView.initComponents();
			edgView.setAnswerSets(list);
			
			/* Instantiate EGView */
			if (!list.isEmpty()){
				egContr.createEGs(edgContr.getEDGs());
				egView.initComponents();
				egView.setAnswerSets(list);
			}
			else egView.setNoAnswerSet();
		} catch (NotValidProgramException e1) {
			JOptionPane.showMessageDialog(graphView, "Program not suitable for representation as graph.", "Not valid program", JOptionPane.WARNING_MESSAGE);
		} catch (SolverException e) {
			JOptionPane.showMessageDialog(graphView, "Path to clingo is not valid. Please set a valid path by choosing Menu->Set path to Clingo.", "No Valid Path to Clingo", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public GraphView getGraphView(){
		return graphView;
	}
}
