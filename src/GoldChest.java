
public class GoldChest extends RoomObject implements Createable {
	private int gold;
	
	public GoldChest(int x, int y, int gold) {
		this.x = x;
		this.y = y;
		this.gold = gold;
		symbol = Console.symbols.get("Chest.Gold");
	}
	public GoldChest(Parser p, Game g) {
		GameObject go = Console.<GameObject>template("gchest").create(p, g);
		fromGameObject(go);
	}
	public GoldChest(GameObject go) {
		fromGameObject(go);
	}
	
	public void fromGameObject(GameObject go) {
		super.fromGameObject(go.<GameObject>element("visual"));
		gold = go.<GameObjectAttribute<Integer>>element("gold").value();
	}
	public void interact(Room r, Character c) {
		c.addGold(gold);
		Console.log("Found %d gold!", gold);
		r.removeObject(this);
	}
	public String string() {
		return String.format("gchest{%s gold %d}", super.string(), gold);
	}
}
