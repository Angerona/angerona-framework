package angerona.fw.error;

import angerona.fw.reflection.Context;

public class InvokeException extends AngeronaException {
	/** kill warning */
	private static final long serialVersionUID = 3424965969366130708L;

	private Context context;
	
	public InvokeException(String message, Context context) {
		super(message);
		this.context = context;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + "\n" + context.toString();
	}

	public static InvokeException createParameterException(String paramname, Context context) {
		return new InvokeException("Can't find Parameter with name: "+paramname, context);
	}
}
