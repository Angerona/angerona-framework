package com.github.kreatures.core.error;


/**
 * An exception indicating an error occurred by the conversion between 
 * to types.
 * @author Tim Janus
 */
public class ConversionException extends KReaturesException {
	/** kill warning */
	private static final long serialVersionUID = 5445004593982801604L;

	/**
	 * Ctor: Generates a message for the Conversion error 
	 * between souce and target.
	 * @param source	Type of the source for the conversion.
	 * @param target	Type of the target for the conversion.
	 */
	public ConversionException(Class<?> source, Class<?> target) {
		this(source, target, null);
	}
	
	/**
	 * Ctor: Generates a message for the conversion error between source and 
	 * target. It also adds the information saved in the inner exception to the 
	 * message.
	 * @param source	Type of the source for the conversion.
	 * @param target	Type of the target for the conversion.
	 * @param inner		The exception which has further information about the 
	 * 					conversion error.
	 */
	public ConversionException(Class<?> source, Class<?> target, Exception inner) {
		super("An error occurred during conversion between '" +  source.getName() + 
				"' to '" + target.getName() + "'" + 
				(inner == null ? "." : " - " + inner.getMessage()));
	}
}
