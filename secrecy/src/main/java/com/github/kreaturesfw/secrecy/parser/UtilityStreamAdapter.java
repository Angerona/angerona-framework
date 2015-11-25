package com.github.kreaturesfw.secrecy.parser;

import java.io.Reader;

public class UtilityStreamAdapter extends com.github.kreaturesfw.core.parser.SimpleCharStream {
	private SimpleCharStream source;

	public UtilityStreamAdapter(SimpleCharStream source) {
		super((Reader) null);
		this.source = source;
	}

	protected void ExpandBuff(boolean wrapAround) {
		source.ExpandBuff(wrapAround);
	}

	protected void FillBuff() throws java.io.IOException {
		source.FillBuff();
	}

	/** Start. */
	public char BeginToken() throws java.io.IOException {
		return source.BeginToken();
	}

	protected void UpdateLineColumn(char c) {
		source.UpdateLineColumn(c);
	}

	/** Read a character. */
	public char readChar() throws java.io.IOException {
		return source.readChar();
	}

	@Deprecated
	/**
	 * @deprecated
	 * @see #getEndColumn
	 */
	public int getColumn() {
		return source.getColumn();
	}

	@Deprecated
	/**
	 * @deprecated
	 * @see #getEndLine
	 */
	public int getLine() {
		return source.getLine();
	}

	/** Get token end column number. */
	public int getEndColumn() {
		return source.getEndColumn();
	}

	/** Get token end line number. */
	public int getEndLine() {
		return source.getEndLine();
	}

	/** Get token beginning column number. */
	public int getBeginColumn() {
		return source.getBeginColumn();
	}

	/** Get token beginning line number. */
	public int getBeginLine() {
		return source.getBeginLine();
	}

	/** Backup a number of characters. */
	public void backup(int amount) {
		source.backup(amount);
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream, int startline, int startcolumn,
			int buffersize) {
		source.ReInit(dstream, startline, startcolumn, buffersize);
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream, int startline, int startcolumn) {
		ReInit(dstream, startline, startcolumn, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.Reader dstream) {
		ReInit(dstream, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn, int buffersize)
			throws java.io.UnsupportedEncodingException {
		ReInit(encoding == null ? new java.io.InputStreamReader(dstream)
				: new java.io.InputStreamReader(dstream, encoding), startline,
				startcolumn, buffersize);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, int startline,
			int startcolumn, int buffersize) {
		ReInit(new java.io.InputStreamReader(dstream), startline, startcolumn,
				buffersize);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding)
			throws java.io.UnsupportedEncodingException {
		ReInit(dstream, encoding, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream) {
		ReInit(dstream, 1, 1, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, String encoding,
			int startline, int startcolumn)
			throws java.io.UnsupportedEncodingException {
		ReInit(dstream, encoding, startline, startcolumn, 4096);
	}

	/** Reinitialise. */
	public void ReInit(java.io.InputStream dstream, int startline,
			int startcolumn) {
		ReInit(dstream, startline, startcolumn, 4096);
	}

	/** Get token literal value. */
	public String GetImage() {
		return source.GetImage();
	}

	/** Get the suffix. */
	public char[] GetSuffix(int len) {
		return source.GetSuffix(len);
	}

	/** Reset buffer when finished. */
	public void Done() {
		source.Done();
	}

	/**
	 * Method to adjust line and column numbers for the start of a token.
	 */
	public void adjustBeginLineColumn(int newLine, int newCol) {
		source.adjustBeginLineColumn(newLine, newCol);
	}
}
