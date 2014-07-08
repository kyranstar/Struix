package ui.windows.map.main;

public class UnhandledToolException extends RuntimeException {
	public UnhandledToolException(){
		super();
	}
	public UnhandledToolException(String message){
		super(message);
	}
	public UnhandledToolException(String message, Throwable t){
		super(message, t);
	}
}
