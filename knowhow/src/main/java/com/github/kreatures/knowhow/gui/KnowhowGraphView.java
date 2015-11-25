package com.github.kreatures.knowhow.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;

import org.jgrapht.graph.DefaultEdge;

import com.github.kreatures.core.KReatures;
import com.github.kreatures.gui.base.EntityViewComponent;
import com.github.kreatures.gui.nav.NavigationPanel;
import com.github.kreatures.gui.nav.NavigationUser;
import com.github.kreatures.core.internal.Entity;
import com.github.kreatures.core.report.ReportEntry;
import com.github.kreatures.core.report.ReportListener;
import com.github.kreatures.knowhow.graph.GraphNode;
import com.github.kreatures.knowhow.graph.KnowhowGraph;
import com.github.kreatures.knowhow.graph.ext.JGraphXAdapter;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

public class KnowhowGraphView 
	extends EntityViewComponent 
	implements ReportListener, NavigationUser {

	/** serial version id */
	private static final long serialVersionUID = -4726331613410781822L;
	
	private JGraphXAdapter<GraphNode, DefaultEdge> graphX = new JGraphXAdapter<>();

	private KnowhowGraph actual;
	
	private NavigationPanel navPanel;
	
	private ReportEntry actEntry;
	
	@Override
	public void reportReceived(ReportEntry entry) {
		if(entry.getAttachment() != null && 
			entry.getAttachment().getGUID().equals(ref.getGUID())) {
			actEntry = entry;
			navPanel.setEntry(entry);
			actual = (KnowhowGraph)entry.getAttachment();
			updateView();
			
		}
	}
	
	private void updateView() {
		graphX.setDataSource(actual.getGraph());

		mxHierarchicalLayout layout = new mxHierarchicalLayout(graphX);
		layout.execute(graphX.getDefaultParent());
	}

	@Override
	public void init() {
		List<ReportEntry> entries = KReatures.getInstance()
				.getActualReport().getEntriesOf(ref);
		if (entries != null && entries.size() > 0) {
			actEntry = entries.get(entries.size() - 1);
		}
		actual = (KnowhowGraph) ref;
		
		this.setLayout(new BorderLayout());		
		this.add(new JScrollPane(graphX.generateDefaultGraphComponent()), 
				BorderLayout.CENTER);
		
		navPanel = new NavigationPanel(this);
		this.add(navPanel, BorderLayout.NORTH);
		navPanel.setEntry(actEntry);
		
		updateView();
	}

	@Override
	public void cleanup() {
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return KnowhowGraph.class;
	}

	@Override
	public Entity getAttachment() {
		return actual;
	}

	@Override
	public ReportEntry getCurrentEntry() {
		return actEntry;
	}

	@Override
	public void setCurrentEntry(ReportEntry entry) {
		reportReceived(entry);
	}

}
