package com.whiplash.doc;

import java.io.*;
import java.nio.charset.*;

import com.whiplash.control.*;

/**
 * The default document factory for Latex documents.
 * @author Matthias Thimm
 *
 */
public class LatexDocumentFactory implements DocumentFactory<LatexDocument> {

	/* (non-Javadoc)
	 * @see com.whiplash.doc.DocumentFactory#createDocument(java.nio.charset.Charset, com.whiplash.control.WlDocumentController)
	 */
	@Override
	public LatexDocument createDocument(Charset encoding, WlDocumentController<LatexDocument> documentController) {
		try{
			return new LatexDocument(encoding, documentController);
		} catch (IOException e) {
			// this cannot happen
			throw new RuntimeException(e);
		}		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.DocumentFactory#createDocument(java.io.File, java.nio.charset.Charset, com.whiplash.control.WlDocumentController)
	 */
	@Override
	public LatexDocument createDocument(File file, Charset encoding, WlDocumentController<LatexDocument> documentController) throws IOException {
		return new LatexDocument(file, encoding, documentController);		
	}

}
