package com.github.angerona.fw.conditional.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.PatternSyntaxException;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.sf.tweety.logics.cl.BruteForceCReasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.logic.conditional.ConditionalBeliefbase;
import com.github.angerona.fw.parser.ParseException;
import com.github.angerona.fw.util.ModelAdapter;

/**

 */
public class OCFModelAdapter extends ModelAdapter implements OCFModel {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(OCFModelAdapter.class);
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
		
		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
		
		log.info("compute c-representation (bruteforce)");
		long startTime = System.currentTimeMillis();
		RankingFunction ocf = creasoner.getCRepresentation();
		long duration = System.currentTimeMillis() - startTime;
		log.info("done. duration: {}ms", duration);
		
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
