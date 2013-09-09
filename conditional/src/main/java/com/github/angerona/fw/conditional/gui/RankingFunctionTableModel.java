package com.github.angerona.fw.conditional.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.propositionallogic.semantics.PossibleWorld;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

public class RankingFunctionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 4368955786436906136L;

	private RankingFunction ocf;
	private List<PossibleWorld> datamodel;
	
	public RankingFunctionTableModel() {
		ocf = new RankingFunction(new PropositionalSignature());
		datamodel = new ArrayList<PossibleWorld>();
	}
	
	public RankingFunctionTableModel(RankingFunction ocf) {
		updateRankingFunction(ocf);
	}
	
	
	public void updateRankingFunction(RankingFunction ocf) {
		this.ocf = ocf;
		datamodel = new LinkedList<PossibleWorld>();
		for(PossibleWorld world : ocf.getPossibleWorlds()) {
			datamodel.add(world);
		}
		fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return ocf.getPossibleWorlds().size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int col) {
		switch(col) {
		case 0:
			return "rank";
			
		case 1:
			return "world";
		default:
			return super.getColumnName(col);
		}
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= datamodel.size())
			return null;
		
		PossibleWorld w = datamodel.get(rowIndex);
		return columnIndex == 0 ? ocf.rank(w) : formatWorldString(w);
		
	}
	
	public String formatWorldString(PossibleWorld w) {
		List<Proposition> propositions = new ArrayList<Proposition>();
		propositions.addAll(ocf.getSignature());
		Collections.sort(propositions);
		String result = "[";
		for(Proposition p : propositions) {
			if(!w.contains(p)) {
				result += "-";
			}
			result += p + ", ";
		}
		return result.substring(0, result.length()-2) + "]";
	}
	
	

}
