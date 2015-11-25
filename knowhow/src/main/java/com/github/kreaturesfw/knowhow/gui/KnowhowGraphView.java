package com.github.kreaturesfw.knowhow.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JScrollPane;

import org.jgrapht.graph.DefaultEdge;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.report.ReportEntry;
import com.github.kreaturesfw.core.report.ReportListener;
import com.github.kreaturesfw.gui.base.EntityViewComponent;
import com.github.kreaturesfw.gui.nav.NavigationPanel;
import com.github.kreaturesfw.gui.nav.NavigationUser;
import com.github.kreaturesfw.knowhow.graph.GraphNode;
import com.github.kreaturesfw.knowhow.graph.KnowhowGraph;
import com.github.kreaturesfw.knowhow.graph.ext.JGraphXAdapter;
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
		List<ReportEntry> entries = Angerona.getInstance()
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
