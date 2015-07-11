package com.github.angerona.fw.island.parsing;

import java.io.IOException;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ParserException extends IOException {

	private static final long serialVersionUID = 9115906685571782621L;

	public ParserException() {
		super("unexpected input");
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable cause) {
		super(cause);
	}

}
