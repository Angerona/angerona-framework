package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import com.whiplash.gui.laf.*;

/**
 * A text pane that displays line numbers for some accompanying text pane.
 * @author Matthias Thimm
 * TODO: the methods of this component are very inefficient (every time the thing is repainted completely)
 */
public class WlLineNumbersPane extends JTextPane implements ComponentListener {

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** The editor pane this pane is located in.*/
	private WlEditorPane<?> editorPane;	
	/** The text pane which lines are numbered.*/
	private WlTextPane textPane;	
	/** Whether the text pane has line wrap on */
	private boolean lineWrap;
	
	/** Maps line numbers to the position where they are located. */
	private Map<Integer,Position> number2Position;
	/** Maps line numbers to the position of the line breaks after that line number. */
	private Map<Integer,Position> number2Linebreaks;
	/** Maps line numbers to the number of line breaks they are followed by. */
	private Map<Integer,Integer> number2Numberoflinebreaks;
	
	/** Creates a new line numbers pane.
	 * @param editorPane the editor pane this pane is located in.
	 * @param textPane some text pane which lines are numbered. */
	public WlLineNumbersPane(WlEditorPane<?> editorPane, WlTextPane textPane){
		this.editorPane = editorPane;
		this.textPane = textPane;
		this.number2Position = new HashMap<Integer,Position>();
		this.number2Linebreaks = new HashMap<Integer,Position>();
		this.number2Numberoflinebreaks = new HashMap<Integer,Integer>();
		this.setBackground(WlLookAndFeel.DEFAULT_LINENUMBERSAREA_BACKGROUND_COLOR);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, WlLookAndFeel.DEFAULT_THINLINE_COLOR), BorderFactory.createEmptyBorder(4, 4, 3, 3)));
		this.setMinimumSize(new Dimension(17,30));
		this.setPreferredSize(new Dimension(17,30));
		this.setEditable(false);
		this.setEnabled(false);
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_RIGHT);		
		this.setParagraphAttributes(attributeSet, false);
		this.setLineWrap(this.textPane.getLineWrap());
		this.textPane.addComponentListener(this);
		this.addComponentListener(this);
	}
	
	/** Shows the line numbers. */
	protected void showLineNumbers(){		
		try {
			String txt = this.textPane.getText();
			int count = this.getNumberOfLineBreaks(txt);
			int currentCount = this.number2Position.size();
			int lastIndex = this.getDocument().getLength();
			while(currentCount < count + 1){
				Integer newLine = new Integer(currentCount + 1);
				this.getDocument().insertString(lastIndex, newLine + "\n", null);
				this.number2Position.put(newLine,this.getDocument().createPosition(lastIndex));
				this.number2Numberoflinebreaks.put(newLine, 1);
				this.number2Linebreaks.put(newLine, this.getDocument().createPosition(lastIndex + newLine.toString().length()));
				lastIndex += newLine.toString().length() + 1;
				currentCount++;
			}
			while(currentCount > count + 1){
				this.getDocument().remove(this.number2Position.get(currentCount).getOffset(), this.getDocument().getLength() - this.number2Position.get(currentCount).getOffset() );
				this.number2Position.remove(currentCount);
				this.number2Linebreaks.remove(currentCount);
				this.number2Numberoflinebreaks.remove(currentCount);
				currentCount--;
			}
			if(this.lineWrap){
				int fontSize = this.textPane.getFont().getSize();				
				int lastLineBreakY = -1;
				int idx = 0;
				int currentLine = 1;
				while (idx != -1){
					idx = txt.indexOf("\n", ++idx);
					if(idx != -1){
						int lbs = 1;
						int nextLineBreakY = this.textPane.modelToView(idx).y;
						while(nextLineBreakY >= lbs * fontSize + lastLineBreakY)
							lbs++;
						if(lbs > 1 && currentLine != 1) lbs--;
						int diff = this.number2Numberoflinebreaks.get(currentLine) - lbs; 
						this.number2Numberoflinebreaks.put(currentLine, lbs);
						if(diff > 0)
							this.getDocument().remove(this.number2Linebreaks.get(currentLine).getOffset(), diff);
						while(diff < 0){
							this.getDocument().insertString(this.number2Linebreaks.get(currentLine).getOffset() + 1, "\n", null);
							diff++;
						}
						currentLine++;
						lastLineBreakY = nextLineBreakY;
					}
				}
				
			}else{
				for(Integer lineNumber: this.number2Position.keySet()){
					while(this.number2Numberoflinebreaks.get(lineNumber) > 1){
						this.number2Numberoflinebreaks.put(lineNumber,this.number2Numberoflinebreaks.get(lineNumber) - 1);
						this.getDocument().remove(this.number2Linebreaks.get(lineNumber).getOffset(), 1);
					}
				}
			}						
		} catch (BadLocationException e) {
			// this should not happen
			e.printStackTrace();
		} catch (NullPointerException e) {
			//this happens when the model of the text pane is not ready -> just return
			return;
		}
		if(this.getMinimumSize().width != new Integer(this.number2Position.size()).toString().length() * 12 + 5){
			this.setMinimumSize(new Dimension(new Integer(this.number2Position.size()).toString().length() * 12 + 5, 1));
			this.setPreferredSize(new Dimension(new Integer(this.number2Position.size()).toString().length() * 12 + 5, 1));
			this.editorPane.adjustmentValueChanged(null);
		}		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JEditorPane#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth(){
		return false;
	}
	
	/** Sets the value for the line wrap setting.
	 * @param value some boolean.
	 */
	protected void setLineWrap(boolean value){
		this.lineWrap = value;
		this.showLineNumbers();
	}
	
	/** Returns the value for the line wrap setting.
	 * @return the value for the line wrap setting.
	 */
	protected boolean getLineWrap(){
		return this.lineWrap;
	}
	
	/** Returns the number of line breaks occurring in the given string.
	 * @param str a string.
	 * @return the number of line breaks occurring in the given string.
	 */
	private int getNumberOfLineBreaks(String str){
		int k = 0;
		int count = 0;
		do{
			k = str.indexOf("\n", k);
			if(k == -1) break;
			count++;
			k++;
		}while(true);
		return count;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) { this.editorPane.adjustmentValueChanged(null);}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) { this.editorPane.adjustmentValueChanged(null);}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		if(!this.lineWrap && !this.getText().equals(""))
			return;
		this.showLineNumbers();				
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
		this.editorPane.adjustmentValueChanged(null);
	}
}
 