package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.config.*;
import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/**
 * This class models a preferences dialog window.
 * @author Matthias Thimm
 */
public class WlPreferencesWindow extends JDialog implements ActionListener {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** A panel providing the categories. */
	private PreferencesTabBar tabBar;
	/** A panel providing the actual content. */
	private JPanel contentPanel;
	
	/** Creates a new preferences window. */
	public WlPreferencesWindow(){
		this.setResizable(false);
		MacUtils.makeWindowLeopardStyle(this.getRootPane());
		this.tabBar = new PreferencesTabBar();
		this.tabBar.installWindowDraggerOnWindow(this);
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		// do layout
		this.setLayout(new BorderLayout());		
		this.contentPanel = new JPanel(new CardLayout());		
		this.add(tabBar.getComponent(), BorderLayout.NORTH);
		this.add(this.contentPanel, BorderLayout.CENTER);
		for(ConfigurationCategory cat: WlConfiguration.getConfigurationCategories()){
			if(cat.isVisible()){
				//AbstractButton button = WlLookAndFeel.createPreferencesWlButton(cat.getLocalizedCaption(), resourceManager.getIcon(cat.getIcon(), WlIconSize.SIZE_32x32), this, cat.getId());
				this.tabBar.addTab(cat.getLocalizedCaption(), resourceManager.getIcon(cat.getIcon(), WlIconSize.SIZE_32x32), this);//.addComponentToLeft(button);
				//this.buttons.add(button);				
				this.contentPanel.add(this.createPanelForCategory(cat),cat.getLocalizedCaption());
			}
		}
		if(WlConfiguration.getConfigurationCategories().size() > 0)
		this.tabBar.showTab(WlConfiguration.getConfigurationCategories().iterator().next().getLocalizedCaption());
		this.pack();
	}
	
	/** Creates a new panel for the given configuration category.
	 * @param cat some configuration category.
	 * @return a panel.
	 */
	private JPanel createPanelForCategory(ConfigurationCategory cat){
		JPanel catPanel = new JPanel();
		catPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		catPanel.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		catPanel.setLayout(new BoxLayout(catPanel,BoxLayout.Y_AXIS));
		for(ConfigurationOption option: cat.getOptions())
			if(option.isVisible())
				catPanel.add(this.createPanelForOption(option));		
		return catPanel;
	}
	
	/** Creates a new panel for the given configuration option.
	 * @param option some configuration option.
	 * @return a panel.
	 */
	private JPanel createPanelForOption(ConfigurationOption option){
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,5,2));
		panel.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		panel.add(new JLabel(option.getLocalizedCaption()));
		panel.add(option.getActionComponent());
		return panel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) this.contentPanel.getLayout();
		cl.show(this.contentPanel, e.getActionCommand());
	}
}
