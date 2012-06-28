/**
 * Taken from http://www.java2s.com/Code/Java/Swing-Components/SteppedComboBoxExample.htm
 */

package angerona.fw.aspgraph.view.util;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class SteppedComboBox<T> extends JComboBox<T> {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 2547264901536959222L;
	protected int popupWidth;

	  public SteppedComboBox(){
		  super();
		  setUI(new SteppedComboBoxUI());
		  popupWidth = 0;
	  }
	  
	  public SteppedComboBox(ComboBoxModel aModel) {
	    super(aModel);
	    setUI(new SteppedComboBoxUI());
	    popupWidth = 0;
	  }

	  public SteppedComboBox(final T[] items) {
	    super(items);
	    setUI(new SteppedComboBoxUI());
	    popupWidth = 0;
	  }

	  public SteppedComboBox(Vector items) {
	    super(items);
	    setUI(new SteppedComboBoxUI());
	    popupWidth = 0;
	  }

	  public void setPopupWidth(int width) {
	    popupWidth = width;
	  }

	  public Dimension getPopupSize() {
	    Dimension size = getSize();
	    if (popupWidth < 1)
	      popupWidth = size.width;
	    return new Dimension(popupWidth, size.height);
	  }
}

