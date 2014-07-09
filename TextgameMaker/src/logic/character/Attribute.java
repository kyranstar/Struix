package logic.character;

public class Attribute<T> {
	
	private final String name;
	private T attr;
	
	public Attribute(String name, T value){
		this.name = name;
		this.attr = value;
	}
	public String getName(){
		return name;
	}
	public void setAttribute(T value){
		this.attr = value;
	}
	public T getAttribute(){
		return attr;
	}
}
