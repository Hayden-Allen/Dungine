
public abstract class RoomObject extends GameObjectClass {
	protected char symbol;
	protected int x, y;
	
	public RoomObject(GameObject go) {
		super(go);
	}

	public static RoomObject create(GameObject go) {
		switch(go.key()) {
		case "gchest": return new GoldChest(go);
		case "enemy": return new Enemy(go);
		}
		return null;
	}
	public char symbol() {
		return symbol;
	}
	public int x() {
		return x;
	}
	public int y() {
		return y;
	}
	public void fromGameObject(GameObject go) {
		symbol = go.<java.lang.Character>attribute("symbol");
		x = go.<Integer>attribute("x");
		y = go.<Integer>attribute("y");
	}
	public abstract void interact(Room r, Character c);
}
