
public abstract class GameObjectClass {
	protected final GameObject from;
	
	public GameObjectClass(GameObject go) {
		from = go;
		fromGameObject(go);
	}
	
	public abstract void fromGameObject(GameObject go);
	public String toString() {
		return from.toString();
	}
}
