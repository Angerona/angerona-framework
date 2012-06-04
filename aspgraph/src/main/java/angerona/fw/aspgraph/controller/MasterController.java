package angerona.fw.aspgraph.controller;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.solver.Clingo;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;
import angerona.fw.aspgraph.exceptions.NotValidProgramException;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;
import angerona.fw.aspgraph.util.AnswerSetTwoValued;
import angerona.fw.aspgraph.view.EDGView;
import angerona.fw.aspgraph.view.GraphView;

public class MasterController {

	private static MasterController instance;
	private static Program p;
	private static HashSet<AnswerSetTwoValued> anwerSets;
	
	private MasterController(){	}
	
	public MasterController instance(){
		if (instance == null) instance = new MasterController();
		return instance;
	}
	
	public static void execute(String filename){
		/* Load logic program from file */
		p = Program.loadFrom(filename);
		
		/* Instantiate controllers and views */
		EDGController edgContr = EDGController.instance();
		GraphView graphView = GraphView.instance();
		
		EDGView edgView = graphView.getEDGPanel();
		Solver solver = new Clingo("C:\\clingo.exe");
		AnswerSetList list;
		try {
			list = solver.computeModels(p, 0);
			edgContr.createEDG(p,list);
			edgView.initComponents();
			edgView.setAnswerSets(list);
		} catch (NotValidProgramException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void calculateAnswerSets(){
		Solver solver = new Clingo("C:\\clingo.exe");
		try {
			AnswerSetList list = solver.computeModels(p, 0);
			List<Atom> litList = new LinkedList<Atom>(p.getAtoms());
			for (AnswerSet a : list){
				
			}
		} catch (SolverException e) {
	}
}
}
