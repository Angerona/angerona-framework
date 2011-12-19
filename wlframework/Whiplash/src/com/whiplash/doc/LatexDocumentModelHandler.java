package com.whiplash.doc;

import com.whiplash.doc.structure.*;
import com.whiplash.threads.*;

/**
 * Instances of this class are responsible for updating a Latex document model
 * of some Latex document.
 * @author Matthias Thimm
 *
 */
public class LatexDocumentModelHandler extends PostboxThread<LatexUpdateMessage> {

	/** The model this handler handles. */
	private LatexDocumentModel model;
	
	/** Creates a new model handler.
	 * @param model some document model.
	 */
	public LatexDocumentModelHandler(LatexDocumentModel model){
		super(100);
		this.model = model;	
		this.setPriority(MIN_PRIORITY);		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.threads.PostboxThread#gotMail(java.lang.Object)
	 */
	@Override
	public void gotMail(LatexUpdateMessage message) {		
		//LatexElementChange changeRecord = null;
		if(message instanceof LatexInsertMessage){
			LatexInsertMessage msg = (LatexInsertMessage) message;
			//changeRecord =
			this.model.insertString(msg.getOffset(), msg.getString());
		}else if(message instanceof LatexRemoveMessage){
			LatexRemoveMessage msg = (LatexRemoveMessage) message;
			//changeRecord =
			this.model.remove(msg.getOffset(), msg.getLength());			
		}else throw new RuntimeException("Unknown message type: " + message.getClass());
	}
}
