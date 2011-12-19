package com.whiplash.doc;

import java.io.*;
import java.nio.charset.*;

import com.whiplash.control.*;

/**
 * Classes implementing this interface are capable of creating empty
 * text documents and text documents from file.
 * @author Matthias Thimm
 * @param <T> the actual text document class.
 */
public interface DocumentFactory<T extends TextFileDocument> {

	/** Creates a new text document using the given encoding.
	 * @param encoding an encoding.
	 * @param documentController the document controller calling this method.
	 * @return the text document.	 
	 */
	public T createDocument(Charset encoding, WlDocumentController<T> documentController);
	
	/** Creates a new text document using the given file and encoding.
	 * @param file a file.
	 * @param encoding an encoding
	 * @param documentController the document controller calling this method.
	 * @return the text document. 
	 * @throws IOException is thrown if the file could not be accessed.
	 */
	public T createDocument(File file, Charset encoding, WlDocumentController<T> documentController) throws IOException;
}