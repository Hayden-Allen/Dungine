
//contains a single value of a given type under a given name
public class GameObjectAttribute<E> extends GameObjectBase {
	private E value;
	
	public GameObjectAttribute(String key, E value) {	//name, value
		super(key);
		this.value = value;
	}
	
	public GameObjectAttribute<?> create(Parser p){	//use this as a template to create new attribute with different value
		//based on type of value, create new attribute
		if(value instanceof String)
			return new GameObjectAttribute<String>(key, p.nextString());
		if(value instanceof Integer)
			return new GameObjectAttribute<Integer>(key, p.nextInt());
		if(value instanceof java.lang.Character)
			return new GameObjectAttribute<java.lang.Character>(key, p.nextChar());
		return null;
	}
	public E value() {
		return value;
	}
	@Override
	public String toString() {
		String valueString = value.toString();
		if(value instanceof String)	//if value is a String, surround it with quotes
			valueString = "\"" + valueString + "\"";
		return key + ":" + valueString;	//key:value
	}
}
