
public class Armor extends Item {
	private int floor;
	
	public Armor(GameObject go) {
		super(go);
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);
		floor = go.attribute("floor");
	}
}
