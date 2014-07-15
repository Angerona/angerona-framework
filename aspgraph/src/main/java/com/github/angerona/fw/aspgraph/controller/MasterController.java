package com.github.angerona.fw.aspgraph.controller;

import javax.swing.JOptionPane;

import net.sf.tweety.lp.asp.parser.ASPParser;
import net.sf.tweety.lp.asp.parser.ParseException;
import net.sf.tweety.lp.asp.solver.Clingo;
import net.sf.tweety.lp.asp.solver.Solver;
import net.sf.tweety.lp.asp.solver.SolverException;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.util.AnswerSetList;

import com.github.angerona.fw.aspgraph.exceptions.NotValidProgramException;
import com.github.angerona.fw.aspgraph.view.EDGView;
import com.github.angerona.fw.aspgraph.view.EGView;
import com.github.angerona.fw.aspgraph.view.GraphView;

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
		//p = Program.loadFrom(filename);
		try {
			p = ASPParser.parseProgram(filename);
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
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
	
	/**
	 * Return GraphView
	 * @return GraphView
	 */
	public GraphView getGraphView(){
		return graphView;
	}
}
