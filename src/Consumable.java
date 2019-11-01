
public class Consumable extends Item {
	private int duration, hp;
	private boolean self;
	
	public Consumable(GameObject go) {
		super(go);
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);
		hp = go.attribute("hp");
		duration = go.attribute("duration");
		self = go.attribute("self");
	}
}
