
public abstract class GameObjectBase {
	protected String key;
	
	public GameObjectBase(String key) {
		this.key = key;
	}
	
	public String key() {
		return key;
	}
	public abstract GameObjectBase create(Parser p);
}
