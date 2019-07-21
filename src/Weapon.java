
public class Weapon extends Item {
	public Weapon(GameObject go) {
		super(go);
	}
	
	public String string() {
		return String.format("weapon{%s}", super.string());
	}
}
