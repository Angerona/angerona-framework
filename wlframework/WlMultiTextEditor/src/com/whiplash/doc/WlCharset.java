package com.whiplash.doc;

import java.io.*;
import java.nio.charset.*;

import com.whiplash.config.*;

/**
 * This class contains some utility methods when working with char sets.
 * @author Matthias Thimm
 */
public enum WlCharset implements Serializable{
	
	WESTERN_ISOLATIN1		("Western (ISO Latin 1)", Charset.forName("ISO-8859-1")),
	WESTERN_ISOLATIN9		("Western (ISO Latin 9)", Charset.forName("ISO-8859-15")),
	WESTERN_MACOSROMAN		("Western (Mac OS Roman)", Charset.forName("MacRoman")),
	WESTERN_WINDOWSLATIN1	("Western (Windows Latin 1)", Charset.forName("windows-1252")),
	UNICODE_UTF8			("Unicode (UTF-8)", Charset.forName("UTF-8")),
	UNICODE_UTF16			("Unicode (UTF-16)", Charset.forName("UTF-16"));
	
	/** The display name of the char set. */
	private String name;
	/** The corresponding char set. */
	private Charset charset;
	
	/** Creates a new char set.
	 * @param name the display name of the char set.
	 * @param charset the actual char set.
	 */
	WlCharset(String name, Charset charset){
		this.name = name;
		this.charset = charset;
	}
	
	/** Returns the display name for the char set.
	 * @return the display name for the char set.
	 */
	public String getName(){
		return this.name;
	}
	
	/** Returns the actual char set of this char set.
	 * @return the actual char set of this char set.
	 */
	public Charset getCharset(){
		return this.charset;
	}
	
	/** Returns the default char set.
	 * @return the default char set.
	 */
	public static WlCharset defaultCharset(){
		return WlMteConfigurationOptions.CONF_DEFAULT_ENCODING.getValue();
	}
	
	/** Returns the char set corresponding to the given (display) name
	 * @param name a name.
	 * @return a char set.
	 */
	public static WlCharset forName(String name){
		for(WlCharset charset: WlCharset.values())
			if(charset.getName().equals(name))
				return charset;
		throw new RuntimeException("Unknown char set name.");
	}

}
