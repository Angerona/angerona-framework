package com.github.kreaturesfw.ocf.gui;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.github.kreaturesfw.core.parser.ParseException;
import com.github.kreaturesfw.core.util.Model;

/**

 */
public interface OCFModel extends Model {
	
	void setConditionals(String input) throws ParseException;
	
	void calculateRankingFunction();
	
	void updateFilter(String text);
	
	public TableModel getTableModel();
	
	public TableRowSorter<RankingFunctionTableModel> getSorter();
}
