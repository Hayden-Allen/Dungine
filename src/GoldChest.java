
public class GoldChest extends RoomObject {
	private int gold;
	
	public GoldChest(GameObject go) {
		super(go.object("visual"));
	}
	
	public void interact(Room r, Character c) {
		c.addGold(gold);
		Console.logn("You found %d gold in the chest!", gold);
		r.removeObject(this);
	}

}
