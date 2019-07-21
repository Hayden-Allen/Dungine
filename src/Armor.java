
public class Armor extends Item {
	private int floor;
	
	public Armor(GameObject go) {
		super(go);
		floor = go.<GameObjectAttribute<Integer>>element("floor").value();
	}
	
	public String string() {
		return String.format("armor{%s floor %d}", super.string(), floor);
	}
}
