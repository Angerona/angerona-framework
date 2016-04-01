package com.github.kreatures.ocf.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import net.sf.tweety.ParserException;

import com.github.kreatures.gui.base.Presenter;
import com.github.kreatures.core.parser.ParseException;

/**

 */
public class OCFPresenter 
	extends Presenter<OCFModel, OCFView>
	implements ActionListener, DocumentListener {
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public OCFPresenter() {}
	
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public OCFPresenter(OCFModel model, OCFView view) {
		setModel(model);
		setView(view);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == view.getCalcButton()) {
			onRun();
		} else if( ev.getSource() == view.getFilterTextField()) {
			onFilter();
		}
	}

	private void onRun() {
		//TODO: start asynchronous
		try {
			model.setConditionals(view.getBeliefBaseTextArea().getText());
			model.calculateRankingFunction();
			view.getTable().getRowSorter().toggleSortOrder(0);
			view.getTable().getRowSorter().toggleSortOrder(0);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(view.getCalcButton(), "Could not parse ocfs, please check for syntax errors.", "Parser error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParserException e) {
			JOptionPane.showMessageDialog(view.getCalcButton(), "Could not parse ocfs, please check for syntax errors.", "Parser error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		
	}
	
	private void onFilter() {
		String filtertext = view.getFilterTextField().getText();
		model.updateFilter(filtertext);
		
	}

	@Override
	protected void forceUpdate() {
		
	}

	@Override
	protected void wireViewEvents() {
		view.getCalcButton().addActionListener(this);
		view.getFilterTextField().getDocument().addDocumentListener(this);
		view.getTable().setModel(model.getTableModel());
		view.getTable().setRowSorter(model.getSorter());
	}

	@Override
	protected void unwireViewEvents() {
		view.getCalcButton().removeActionListener(this);
		view.getFilterTextField().getDocument().removeDocumentListener(this);
		view.getTable().setModel(new RankingFunctionTableModel());
		view.getTable().setRowSorter(new TableRowSorter<>());
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		onFilter();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		onFilter();
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		onFilter();
		
	}
}
