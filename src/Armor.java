
public class Armor extends Item {	//a type of Item that a Character can equip
	private int floor;	//the maximum amount of negative damage (healing) that an enemy's attack can do
	
	public Armor(GameObject go) {
		super(go);
	}
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go);	//send to Item
		//get Armor-specific values
		floor = go.attribute("floor");
	}
	public int floor() {
		return floor;
	}
}
