package ui.windows.map;

public class AlreadyExistsException extends IllegalStateException{
	private static final long serialVersionUID = 1L;
	
	public AlreadyExistsException(){
		super();
	}
	public AlreadyExistsException(String message){
		super(message);
	}
	public AlreadyExistsException(String message, Throwable t){
		super(message, t);
	}
}
