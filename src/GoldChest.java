
public class GoldChest extends RoomObject {
	private int gold;
	
	public GoldChest(int x, int y, int gold) {
		this.x = x;
		this.y = y;
		this.gold = gold;
		symbol = Console.symbols.get("Chest.Gold");
	}
	
	public void interact(Room r, Character c) {
		c.addGold(gold);
		r.removeObject(this);
	}
}
