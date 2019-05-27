
public class GameObjectAttribute<E> extends GameObjectBase {
	private E value;
	
	public GameObjectAttribute(String key, E value) {
		this.key = key;
		this.value = value;
	}
	
	public E value(){
		return value;
	}
	@SuppressWarnings("unchecked")
	public GameObjectAttribute<E> create(Parser p) {
		E value = (E)p.<E>nextE(this.value);
		
		return new GameObjectAttribute<E>(key, value);
	}
	@Override
	public String toString() {
		return key + " " + (value instanceof String ? '\"' : "" ) + value + (value instanceof String ? '\"' : "" );
	}
}
