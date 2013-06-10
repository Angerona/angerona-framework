package angerona.fw.conditional.gui;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import angerona.fw.parser.ParseException;
import angerona.fw.util.Model;

/**

 */
public interface OCFModel extends Model {
	
	void setConditionals(String input) throws ParseException;
	
	void calculateRankingFunction();
	
	void updateFilter(String text);
	
	public TableModel getTableModel();
	
	public TableRowSorter<RankingFunctionTableModel> getSorter();
}
