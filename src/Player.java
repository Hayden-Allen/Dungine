
public class Player extends Character {
	public Player(Parser p) {
		GameObject go = Console.<GameObject>template("player").create(p);
		fromGameObject(go);
	}
	public Player(GameObject go) {
		fromGameObject(go);
	}
	
	public void addX(int x) {
		this.x += x;
	}
	public void addY(int y) {
		this.y += y;
	}
	public void interact(Room r, Character c) {	//TODO: print out player info
		
	}
	public void fromGameObject(GameObject go) {	//in this case, x and y represent world coordinates
		stats = new StatList(go.<GameObject>element("stats"));
		hp = go.<GameObjectAttribute<Integer>>element("hp").value();
		gold = go.<GameObjectAttribute<Integer>>element("gold").value();
		super.fromGameObject(go.<GameObject>element("visual"));
	}
	public String string() {	//TODO inventory
		return String.format("player{%s hp %d gold %d}", stats.string(), hp, gold);
	}
}
