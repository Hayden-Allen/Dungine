
//base class for template classes
public abstract class GameObjectBase {
	protected String key;	//identifier
	
	public GameObjectBase(String key) {
		this.key = key;
	}
	
	public String key() {
		return key;
	}
	public abstract GameObjectBase create(Parser p);	//each must define a creation method
}
