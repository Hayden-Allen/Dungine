
//a RoomObject that stores gold
public class GoldChest extends RoomObject {
	private int gold;
	
	public GoldChest(GameObject go) {
		super(go);
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.object("visual"));	//send to RoomObject
		//get GoldChest-specific values
		gold = go.attribute("gold");
	}
	
	public void interact(Room r, Character c) {
		c.addGold(gold);	//give Character gold
		Console.logn(Console.rsetting("chest.gold"), gold);	//print gold find string
		r.removeObject(this);	//remove from Room
	}

}
