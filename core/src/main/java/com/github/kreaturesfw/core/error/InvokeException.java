package com.github.kreaturesfw.core.error;

import com.github.kreaturesfw.core.reflection.Context;


/**
 * This exception is thrown if something went wrong during the invoking of function defined in ASML 
 * format. 
 * 
 * @author Tim Janus
 */
public class InvokeException extends AngeronaException {
	/** kill warning */
	private static final long serialVersionUID = 3424965969366130708L;

	private Context context;
	
	private Type type;
	
	public enum Type {
		INTERNAL,
		PARAMETER,
		TYPE_MISMATCH,
		SCOPE_ERROR
	}
	
	public InvokeException(String message, Type type, Context context) {
		super(message);
		this.type = type;
		this.context = context;
	}

	public Type getErrorType() {
		return type;
	}
	
	@Override
	public String getMessage() {
		return "[" + type.toString() + "] " + super.getMessage();
	}

	public String temp() {
		return getMessage() + "\n" + context.toString() + "\n---";
	}
	
	public static InvokeException internalError(String message, Context context) {
		return new InvokeException(message, Type.INTERNAL, context);
	}
	
	public static InvokeException typeMismatch(String paramname, Class<?> type, Context context) {
		return new InvokeException("Cannot cast '" + paramname + "' to type '" + type + "'.", 
				Type.TYPE_MISMATCH, context);
	}
	
	public static InvokeException parameterFailure(String paramname, Context context) {
		return new InvokeException("Cannot find Parameter with name: "+paramname, 
				Type.PARAMETER, context);
	}
	
	public static InvokeException scopeError(String message, Context context) {
		return new InvokeException(message, Type.SCOPE_ERROR, context);
	}
}
