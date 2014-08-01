package lang.exceptions;

public class UndefinedFunctionException extends InterpreterException{

	private static final long serialVersionUID = 1L;

	public UndefinedFunctionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UndefinedFunctionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UndefinedFunctionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UndefinedFunctionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UndefinedFunctionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
