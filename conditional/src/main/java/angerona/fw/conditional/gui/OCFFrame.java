package angerona.fw.conditional.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import angerona.fw.gui.base.ObservingPanel;

/**
 * 
 * @author Sebastian Homann
 */
public class OCFFrame extends ObservingPanel implements OCFView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;
	
		private JSplitPane spnSplitPane;
		private JPanel pnLeft;
		JLabel lblKbTitle;
		private JButton btnRun;
		private JTextArea txaConditionalInput;
		private JPanel pnLeftTop;
		
		private JPanel pnRight;
		private JPanel pnRightTop;
		JLabel lblFilter;
		private JTextField txtFilter;
		
		private JTable tblOCF;
		
		/** Default Ctor: Creating the Widget hierarchy */
		public OCFFrame() {
			setLayout(new BorderLayout());
			spnSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			this.add(spnSplitPane, BorderLayout.CENTER);
			
			pnLeft = new JPanel();
			pnLeft.setLayout(new BoxLayout(pnLeft, BoxLayout.PAGE_AXIS));
			pnLeftTop = new JPanel();
			pnLeftTop.setLayout(new BoxLayout(pnLeftTop, BoxLayout.LINE_AXIS));
			lblKbTitle = new JLabel("Conditionals:");
			btnRun = new JButton("Calculate");
			pnLeftTop.add(lblKbTitle);
			pnLeftTop.add(Box.createHorizontalGlue());
			pnLeftTop.add(btnRun);
			
			pnLeft.add(pnLeftTop);
			
			txaConditionalInput = new JTextArea();
			pnLeft.add(txaConditionalInput);
			spnSplitPane.setLeftComponent(pnLeft);

			pnRight = new JPanel();
			pnRight.setLayout(new BoxLayout(pnRight, BoxLayout.PAGE_AXIS));
			pnRightTop = new JPanel();
			pnRightTop.setLayout(new BoxLayout(pnRightTop, BoxLayout.LINE_AXIS));
			lblFilter = new JLabel("Filter:");
			txtFilter = new JTextField();
			pnRightTop.add(lblFilter);
			pnRightTop.add(txtFilter);
			pnRightTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtFilter.getPreferredSize().height));
			pnRight.add(pnRightTop);
			
			tblOCF = new JTable(new RankingFunctionTableModel());
//			tblOCF.setAutoCreateRowSorter(true);
			JScrollPane scrollPane = new JScrollPane(tblOCF);
			tblOCF.setFillsViewportHeight(true);
			
			
			pnRight.add(scrollPane);
			spnSplitPane.setRightComponent(pnRight);

		}

		public static void main(String[] args) {
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.add(new OCFFrame(), BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		}
		
	@Override
	public <T> void propertyChange(String propertyName, T oldValue, T newValue) {
		if(propertyName  == "simulationState") {

		} else if(propertyName == "simulationConfig") {
			
		}
	}

	@Override
	public AbstractButton getCalcButton() {
		return btnRun;
	}

	@Override
	public JTextArea getBeliefBaseTextArea() {
		return txaConditionalInput;
	}

	@Override
	public JTextField getFilterTextField() {
		return txtFilter;
	}

	@Override
	public JTable getTable() {
		return tblOCF;
	}



}
