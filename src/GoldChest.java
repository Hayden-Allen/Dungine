
public class GoldChest extends RoomObject implements Createable {
	private int gold;
	
	public GoldChest(int x, int y, int gold) {
		this.x = x;
		this.y = y;
		this.gold = gold;
		symbol = Console.symbols.get("Chest.Gold");
	}
	public GoldChest(Parser p) {
		GameObject go = Console.<GameObject>template("gchest").create(p);
		fromGameObject(go);
	}
	
	public void fromGameObject(GameObject go) {
		gold = go.<GameObjectAttribute<Integer>>element("gold").value();
	}
	public String string() {
		return String.format("gchest{gold %d}", gold);
	}
	public void interact(Room r, Character c) {
		c.addGold(gold);
		r.removeObject(this);
	}
}
