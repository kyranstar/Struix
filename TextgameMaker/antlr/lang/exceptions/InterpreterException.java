package lang.exceptions;

public class InterpreterException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InterpreterException() {
		super();
	}

	public InterpreterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InterpreterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterpreterException(String message) {
		super(message);
	}

	public InterpreterException(Throwable cause) {
		super(cause);
	}

	
}
