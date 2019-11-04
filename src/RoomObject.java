
public abstract class RoomObject extends GameObjectClass {	//an Object stored in a Room
	private char symbol;	//single character graphical representation
	private int x, y;	//room coordinates x is [0, 3] y is [0, 1]
	
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
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void fromGameObject(GameObject go) {
		symbol = go.<java.lang.Character>attribute("symbol");
		x = go.<Integer>attribute("x");
		y = go.<Integer>attribute("y");
		
		if(x < 0 || x > 3)
			Console.parser.err(Parser.RO_OUT_OF_BOUNDS_X, x);
		if(y < 0 || y > 1)
			Console.parser.err(Parser.RO_OUT_OF_BOUNDS_Y, y);
	}
	public abstract void interact(Room r, Character c);
}
