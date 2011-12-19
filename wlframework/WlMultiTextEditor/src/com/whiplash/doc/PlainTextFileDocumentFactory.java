package com.whiplash.doc;

import java.io.*;
import java.nio.charset.*;

import com.whiplash.control.*;

/**
 * The default text document factory for text documents.
 * @author Matthias Thimm
 */
public class PlainTextFileDocumentFactory implements DocumentFactory<PlainTextFileDocument> {

	/* (non-Javadoc)
	 * @see com.whiplash.doc.DocumentFactory#createDocument(java.nio.charset.Charset)
	 */
	@Override
	public PlainTextFileDocument createDocument(Charset encoding, WlDocumentController<PlainTextFileDocument> documentController) {
		try {
			return new PlainTextFileDocument(encoding, documentController);
		} catch (IOException e) {
			// this cannot happen
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.DocumentFactory#createDocument(java.io.File, java.nio.charset.Charset)
	 */
	@Override
	public PlainTextFileDocument createDocument(File file, Charset encoding, WlDocumentController<PlainTextFileDocument> documentController)	throws IOException {
		return new PlainTextFileDocument(file, encoding, documentController);
	}
	
}
