
//base class for any class that can be created from a GameObject
//intended to be used for saving the state of a game, not implemented yet
public abstract class GameObjectClass {
	protected final GameObject from;	//the GameObject from which this object was created
	
	public GameObjectClass(GameObject go) {
		from = go;	//store creation object
		fromGameObject(go);	//call creation method
	}
	
	public abstract void fromGameObject(GameObject go);	//each class must define creation method
	public String toString() {
		return from.toString();
	}
}
