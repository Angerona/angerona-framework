package com.github.angerona.knowhow.graph.parameter;

import java.io.Serializable;

import com.github.angerona.fw.util.Utility;


public class Parameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 100849364453010345L;

	public static enum TYPE {
		T_AGENT,
		T_HONESTY,
		T_FORMULA,
		T_FILE
	}
	
	private TYPE type;
	
	private String identifier;
	
	public Parameter(String name) {
		this.identifier = name;
		if(identifier.startsWith("a_") || identifier.startsWith("T_AGENT")) {
			type = TYPE.T_AGENT;
		} else if(identifier.startsWith("p_") || identifier.startsWith("T_HONESTY")) {
			type = TYPE.T_HONESTY;
		} else if(identifier.startsWith("f_") || identifier.startsWith("T_FILE")) {
			type = TYPE.T_FILE;
		} else {
			type = TYPE.T_FORMULA;
		}
	}
	
	public boolean isVariable() {
		char begin = this.identifier.charAt(0);
		return (begin >= 65 && begin <= 90) || begin == '_';
	}
	
	public TYPE getType() {
		return type;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
		return identifier;
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode() * 7;
	}
	
	public boolean equals(Object other) {
		if(this == other)	return true;
		if(other == null || this.getClass() != other.getClass()) return false;
		
		return Utility.equals(identifier, ((Parameter)other).identifier);
	}
}
