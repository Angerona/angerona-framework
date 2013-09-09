/**
 * Taken from http://www.java2s.com/Code/Java/Swing-Components/SteppedComboBoxExample.htm
 */

package com.github.angerona.fw.aspgraph.view.util;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

class SteppedComboBoxUI extends MetalComboBoxUI {
  protected ComboPopup createPopup() {
    BasicComboPopup popup = new BasicComboPopup(comboBox) {

      /**
		 * 
		 */
		private static final long serialVersionUID = 1321659142939105310L;

	public void show() {
        Dimension popupSize = ((SteppedComboBox) comboBox)
            .getPopupSize();
        popupSize
            .setSize(popupSize.width,
                getPopupHeightForRowCount(comboBox
                    .getMaximumRowCount()));
        Rectangle popupBounds = computePopupBounds(0, comboBox
            .getBounds().height, popupSize.width, popupSize.height);
        scroller.setMaximumSize(popupBounds.getSize());
        scroller.setPreferredSize(popupBounds.getSize());
        scroller.setMinimumSize(popupBounds.getSize());
        list.invalidate();
        int selectedIndex = comboBox.getSelectedIndex();
        if (selectedIndex == -1) {
          list.clearSelection();
        } else {
          list.setSelectedIndex(selectedIndex);
        }
        list.ensureIndexIsVisible(list.getSelectedIndex());
        setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());

        show(comboBox, popupBounds.x, popupBounds.y);
      }
    };
    popup.getAccessibleContext().setAccessibleParent(comboBox);
    return popup;
  }
}