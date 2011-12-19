package com.whiplash.doc;

import java.util.*;

import com.whiplash.doc.structure.*;

/** This class holds
 * record of changes made to some latex document  model.
 * @author Matthias Thimm
 */
public class LatexElementChange {

	/** The element that has been changed. */
	private LatexDocumentModel model;
	/** The children that have been added. */
	private List<AbstractLatexElement> addedChildren;
	/** The children that have been removed. */
	private List<AbstractLatexElement> removedChildren;
	
	/** Creates a new latex element change.
	 * @param element the element that has been changed.
	 */
	public LatexElementChange(LatexDocumentModel model){
		this.model = model;
		this.addedChildren = new ArrayList<AbstractLatexElement>();
		this.removedChildren = new ArrayList<AbstractLatexElement>();
	}

	/** Adds the given element to this change records added children.
	 * @param elem some element.
	 */
	public void addAddedChild(AbstractLatexElement elem){
		this.addedChildren.add(elem);
	}
	
	/** Adds the given element to this change records removed children.
	 * @param elem some element.
	 */
	public void addRemovedChild(AbstractLatexElement elem){
		this.removedChildren.add(elem);
	}
	
	/** Adds the given elements to this change records added children.
	 * @param elem some element.
	 */
	public void addAddedChildren(List<? extends AbstractLatexElement> elements){
		this.addedChildren.addAll(elements);
	}
	
	/** Adds the given elements to this change records removed children.
	 * @param elem some element.
	 */
	public void addRemovedChildren(List<? extends AbstractLatexElement> elements){
		this.removedChildren.addAll(elements);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentEvent.ElementChange#getChildrenAdded()
	 */
	public AbstractLatexElement[] getChildrenAdded() {
		return this.addedChildren.toArray(new AbstractLatexElement[0]);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentEvent.ElementChange#getChildrenRemoved()
	 */
	public AbstractLatexElement[] getChildrenRemoved() {
		return this.removedChildren.toArray(new AbstractLatexElement[0]);
	}

	/** Returns the model of this change record.
	 * @return the model of this change record.
	 */
	public LatexDocumentModel getModel() {
		return this.model;
	}

}
