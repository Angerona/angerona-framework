package com.github.kreaturesfw.ocf.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.kreaturesfw.core.parser.ParseException;
import com.github.kreaturesfw.core.util.ModelAdapter;
import com.github.kreaturesfw.ocf.logic.ConditionalBeliefbase;

import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.kappa.KappaValue;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.cl.syntax.Conditional;

/**

 */
public class OCFModelAdapter extends ModelAdapter implements OCFModel {
	private TableRowSorter<RankingFunctionTableModel> sorter;
	
	private ConditionalBeliefbase bbase;

	private RankingFunctionTableModel tableModel;

	public OCFModelAdapter() {
		tableModel = new RankingFunctionTableModel();
		sorter = new TableRowSorter<RankingFunctionTableModel>(tableModel);
	}
	
	@Override
	public void setConditionals(String input) throws ParseException {
		bbase = new ConditionalBeliefbase();
		try {
			bbase.parse(new BufferedReader(new StringReader(input)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void calculateRankingFunction() {
		if(bbase == null) 
			return;
		
//		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
//		
//		log.info("compute c-representation (bruteforce)");
//		long startTime = System.currentTimeMillis();
//		RankingFunction ocf = creasoner.getCRepresentation();
//		long duration = System.currentTimeMillis() - startTime;
//		log.info("done. duration: {}ms", duration);
//		
//		tableModel.updateRankingFunction(ocf);
				
		Set<Conditional> conds = new HashSet<Conditional>();
		conds.addAll(bbase.getConditionalBeliefs());		
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(conds, true);
		Long before = System.currentTimeMillis();
		reasoner.prepare();
		Long duration = System.currentTimeMillis() - before;
		System.out.println("Generated in " + duration + "ms:");
		System.out.println("Conditional Structure:");
		System.out.println(reasoner.getConditionalStructure());
		
		System.out.println("Initial Kappa:");
		for(KappaValue kv : reasoner.getKappas()) {
			System.out.println(kv.fullString());
		}
		
		before = System.currentTimeMillis();
		reasoner.process();
		duration = System.currentTimeMillis() - before;
		System.out.println("Evaluated in " + duration + "ms:");
		for(KappaValue kv : reasoner.getKappas()) {
			System.out.println(kv.fullString());
		}
		System.out.println("Ranking-Function:");
		System.out.println(reasoner.getSemantic());
		System.out.println("");
		
		
		RankingFunction ocf = reasoner.getSemantic();
		
		tableModel.updateRankingFunction(ocf);
		
	}

	@Override
	public void updateFilter(String text) {
		RowFilter<RankingFunctionTableModel, Object> rf;
		try{
			rf = RowFilter.regexFilter(text);
		} catch (PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}
	
	@Override
	public TableModel getTableModel() {
		return tableModel;
	}
	
	@Override
	public TableRowSorter<RankingFunctionTableModel> getSorter() {
		return sorter;
	}

}
